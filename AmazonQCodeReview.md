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

