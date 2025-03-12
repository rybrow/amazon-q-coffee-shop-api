package amazon.q.coffee.shop.api.controller;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest
public class HelloControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void testHelloEndpoint() {
        var request = HttpRequest.GET("/api/hello");
        var response = client.toBlocking().exchange(request, HelloController.HelloResponse.class);
        
        assertEquals(200, response.code());
        assertNotNull(response.body());
        assertEquals("Hello World from Coffee Shop API!", response.body().getMessage());
    }

    @Test
    void testHelloNameEndpoint() {
        var request = HttpRequest.GET("/api/hello/John");
        var response = client.toBlocking().exchange(request, HelloController.HelloResponse.class);
        
        assertEquals(200, response.code());
        assertNotNull(response.body());
        assertEquals("Hello John from Coffee Shop API!", response.body().getMessage());
    }
}