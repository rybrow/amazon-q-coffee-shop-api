package amazon.q.coffee.shop.api;

import amazon.q.coffee.shop.api.model.Coffee;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class CoffeeControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    public void testGetAllCoffees() {
        HttpRequest<Object> request = HttpRequest.GET("/api/coffees");
        List<Coffee> coffees = client.toBlocking().retrieve(request, List.class);
        assertNotNull(coffees);
        assertFalse(coffees.isEmpty());
    }

    @Test
    public void testGetCoffeeById() {
        // First get all coffees to get a valid ID
        HttpRequest<Object> listRequest = HttpRequest.GET("/api/coffees");
        List<Coffee> coffees = client.toBlocking().retrieve(listRequest, List.class);
        
        // Get the first coffee's ID
        int firstCoffeeId = (Integer) ((java.util.LinkedHashMap) coffees.get(0)).get("id");
        
        // Now get that specific coffee
        HttpRequest<Object> request = HttpRequest.GET("/api/coffees/" + firstCoffeeId);
        HttpResponse<Coffee> response = client.toBlocking().exchange(request, Coffee.class);
        
        assertEquals(HttpStatus.OK, response.status());
        assertNotNull(response.body());
    }

    @Test
    public void testCreateCoffee() {
        Coffee coffee = new Coffee();
        coffee.setName("Mocha");
        coffee.setPrice(new BigDecimal("4.75"));
        coffee.setDescription("Chocolate-flavored variant of a latte");
        
        HttpRequest<Coffee> request = HttpRequest.POST("/api/coffees", coffee);
        HttpResponse<Coffee> response = client.toBlocking().exchange(request, Coffee.class);
        
        assertEquals(HttpStatus.CREATED, response.status());
        assertNotNull(response.body());
        assertEquals("Mocha", response.body().getName());
        assertNotNull(response.body().getId());
    }
}