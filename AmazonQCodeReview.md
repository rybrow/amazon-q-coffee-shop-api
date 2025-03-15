# Using The Amazon Q Developer CLI in your CI/CD pipeline to automate your Code Reviews

Hello!! This is my first blog post on the AWS forums - My Organization has recently adopted Amazon Q Developer and with my DevOps brain having lived and breathed CI/CD for the past 5 years - Just wanted to share how I managed to get Amazon Q working within a GitHub actions pipeline.

## Why use Q in CI/CD ?

While this post is primarily a proof of concept on *how* you can use the Amazon Q Developer CLI within your CI/CD pipeline. I should probably talk about why you would want to put Q in your CI/CD pipeline.

1. The reason we all use AI, to increase efficiency.

If you can get Amazon Q to do automated and reliable code reviews, then for small non-critical projects, your team's velocity will definitely increase.

2. Solving the context switch problem.

Sometimes, there can be nothing more disruptive for a senior engineer who's plugged in, than a request from the team's graduate / junior to review their code. If Amazon Q can knock off the basics without having to interrupt the senior engineers,

3. Upskilling on best practices.

In my experience, there's been nothing more educationa than getting grilled by a Senior Engineer on a pull request. While it can be embarrasing, 99% of time, they have their reasons, even if you cant see them. Getting a similar experience out of Amazon Q will definitely help the "new kid" get up to date on best practices fast!

## Using Amazon Q in the CI/CD Context!

For this example - I will be using Github Actions, but the same approach can be done with any CI/CD pipeline, or even as part of a docker build process.

### Installation

Below is a sample Github Action for installing and running Amazon Q! This pipeline will specifically run on pull requests to the "main" branch. But you can use Github Actions [hyperlink this] to integrate this into any existing workflows you have.

``` yaml
name: Amazon Q Pipeline
on:
  pull_request:
    branches:
      - main
jobs:
  AmazonQCodeReview:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout repository
        uses: actions/checkout@v3
      - name: Install Amazon Q
        run: |
          curl --proto '=https' --tlsv1.2 -sSf https://desktop-release.q.us-east-1.amazonaws.com/latest/amazon-q.deb -o amazon-q.deb
          sudo apt install -y ./amazon-q.deb
          rm amazon-q.deb
      - name: Run Amazon Q
        run: q chat "Hello Amazon Q! What capabilities do you offer??"
```

So, lets see how it runs??

Turns out, by default. Not well... We need to login!

```
Run q chat "Hello Amazon Q! What capabilities do you offer??"
error: You are not logged in, please log in with q login
```

### Persisting the Authentication with Amazon Q Developer

Right, so with most "modern" CI/CD pipelines, we have disposable agents that either run on VMs or Docker Containers, meaning, these are usually stateless. So by default, our Amazon Q Developer will be always logged out.

 If you are hosting your own CI/CD infrastructure, you may have stateful agents that you could manually log in to and reauthenticate with when needed. But I will also take the liberty of showing you how you can persist Amazon Q Developer's session between builds.

 #### Logging into Amazon Q Developer CLI

 You can initiate the login process by running the `q login` command. This opens an interactive terminal session below, which isn't the most ideal from a CI/CD perspective, as we have to provide manual actions in the terminal to authenticate.

 For this walkthrough, run the q login command on your local machine, and NOT the CI/CD pipeline.

```
? Select login method ›
❯ Use for Free with Builder ID
  Use with Pro license
```

Follow the prompts on your interactive terminal to authenticate with Q.

My recommendation for regular use is to use a Service account with a Pro license - however if you are trialing this, a free account will do. Just be aware of the service quotas of the Free Amazon Q Developer offering, as this may not be suitable for day to day usage within your organization.

Once logged into Amazon Q cli - check you are logged in with the command `q chat "Hello Amazon Q!"`

You should see something similar to the below.

```
Hi, I'm Amazon Q. Ask me anything.

Things to try
• Fix the build failures in this project.
• List my s3 buckets in us-west-2.
• Write unit tests for my application.
• Help me understand my git status

/acceptall    Toggles acceptance prompting for the session.
/help         Show the help dialogue
/quit         Quit the application


> Hello Amazon Q!

Hello! I'm Amazon Q, your AI assistant from AWS. How can I help you today? I can assist with AWS services, coding tasks, infrastructure
management, or any other technical questions you might have. Let me know what you're working on, and I'll do my best to help.
```

#### Persisting the Authentication

For Linux (Ubuntu), the local credentials for Amazon Q developer are stored in the following location.

    ~/.local/share/amazon-q/

To Authenticate the CI/CD context, we will need to persist these to some shared location.

I have used S3 for this example as that was the quickest. But this does contain sensitive data, so I would consider implementing additional security measures through S3 VPC endpoints (if hosting your own runners), or by using something like secrets manager or SSM parameter store.

I uploaded these to S3 by running the below.

```
aws s3 sync ~/.local/share/amazon-q/ s3://<amazon-q-bucket>/authentication
```

#### Pulling the Authentication in the Github Actions Context

Now we have the authentication persisted, we can authenticate the CI/CD pipeline with Amazon Q Developer

Lets update our pipeline to do just that.

This assumes we have pre-existing authentication with AWS that can access our S3 bucket.

``` yaml
name: Amazon Q Pipeline
on:
  pull_request:
    branches:
      - main
permissions:
  id-token: write
jobs:
  AmazonQCodeReview:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout repository
        uses: actions/checkout@v3
      - name: Install Amazon Q
        run: |
          curl --proto '=https' --tlsv1.2 -sSf https://desktop-release.q.us-east-1.amazonaws.com/latest/amazon-q.deb -o amazon-q.deb
          sudo apt install -y ./amazon-q.deb
          rm amazon-q.deb
      - name: Configure AWS credentials
        uses: aws-actions/configure-aws-credentials@v4
        with:
          role-to-assume: arn:aws:iam::<account-id>:role/<your-github-actions-role>
          aws-region: <your-region>
      - name: Pull Amazon Q Authentication
        run: aws s3 sync s3://<amazon-q-bucket>/authentication ~/.local/share/amazon-q/
      - name: Run Amazon Q
        run: q chat "Hello Amazon Q! What capabilities do you offer??"
```

The output from the "Run Amazon Q" step should show something similar to the following now.

```
Hello Amazon Q! What capabilities do you offer??
Hello! I'm Amazon Q, an AI assistant built by AWS to help you with various
tasks. Here are my key capabilities:

• I can interact with your local filesystem to read, write, and list files or
directories
• I can execute bash commands on your system
• I can make AWS CLI calls to manage and query AWS resources
• I provide AWS and software-focused assistance and recommendations
• I can help with infrastructure code and configurations
• I can guide you on best practices for AWS services
• I can analyze and optimize resource usage
• I can troubleshoot issues and errors
• I can assist with CLI commands and automation tasks
• I can write and modify software code
• I can help test and debug software

I'm designed to be practical and provide actionable information. Is there
something specific you'd like help with today?
(To exit, press Ctrl+C or Ctrl+D again or type /quit)
```

### Running Slash Commands via the Amazon Q CLI

To invoke slash commands via the amaozn Q cli we need to run the `q chat` command in a special format

    q chat -- "--command /\<command> \<prompt>"

e.g

    q chat -- "--command /review Do a code review for any Critical, or High security issues in my workspace"

however running this leads to a prompt in the Amazon Q context. e.g

```
Let me check if there are any controllers or services in the application:



Execute shell command
▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔▔
I will run the following shell command:
find /mnt/d/Work/Rybrow/amazon-q-coffee-shop-api/src/main/java -type f -name "*.java" | grep -v "model"

Enter y to run this tool, otherwise continue chatting.
```

This isnt' compatible with the CI/CD pipeline as its non-interactive so we need to use the --accept-all flag to 'Auto Approve'any commands to be run.

So, the command we run in Github Actions is.

    q chat --accept-all -- "--command /review Do a code review for any Critical, or High security issues in my workspace"

Now our github actions pipeline looks like this!

``` yaml
name: Amazon Q Pipeline
on:
  pull_request:
    branches:
      - main
permissions:
  id-token: write
jobs:
  AmazonQCodeReview:
    runs-on: ubuntu-latest
    steps:
      - name: Checkout repository
        uses: actions/checkout@v3
      - name: Install Amazon Q
        run: |
          curl --proto '=https' --tlsv1.2 -sSf https://desktop-release.q.us-east-1.amazonaws.com/latest/amazon-q.deb -o amazon-q.deb
          sudo apt install -y ./amazon-q.deb
          rm amazon-q.deb
      - name: Configure AWS credentials
        uses: aws-actions/configure-aws-credentials@v4
        with:
          role-to-assume: arn:aws:iam::<account-id>:role/<your-github-actions-role>
          aws-region: <your-region>
      - name: Pull Amazon Q Authentication
        run: aws s3 sync s3://<amazon-q-bucket>/authentication ~/.local/share/amazon-q/
      - name: Run Amazon Q
        run: q chat --accept-all -- "--command /review Do a code review for any Critical, or High security issues in my workspace"
```