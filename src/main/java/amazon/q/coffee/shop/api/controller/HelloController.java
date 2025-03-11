package amazon.q.coffee.shop.api.controller;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller("/api")
@Tag(name = "Hello", description = "Sample hello world endpoints")
public class HelloController {

    @Get(uri = "/hello", produces = MediaType.APPLICATION_JSON)
    @Operation(summary = "Get a hello world message",
            description = "Returns a simple hello world message")
    @ApiResponse(
            responseCode = "200",
            description = "Hello World Response",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = HelloResponse.class))
    )
    public HelloResponse hello() {
        return new HelloResponse("Hello World from Coffee Shop API!");
    }

    @Get(uri = "/hello/{name}", produces = MediaType.APPLICATION_JSON)
    @Operation(summary = "Get a personalized hello message",
            description = "Returns a hello message with the provided name")
    @ApiResponse(
            responseCode = "200",
            description = "Personalized Hello Response",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = HelloResponse.class))
    )
    public HelloResponse helloName(String name) {
        return new HelloResponse(String.format("Hello %s from Coffee Shop API!", name));
    }

    public static class HelloResponse {
        private final String message;

        public HelloResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}