package amazon.q.coffee.shop.api;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
    info = @Info(
        title = "Coffee Shop API",
        version = "1.0",
        description = "API for managing a coffee shop",
        license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"),
        contact = @Contact(name = "Coffee Shop API Support", email = "support@coffeeshop.example.com")
    )
)
public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class, args);
    }
}