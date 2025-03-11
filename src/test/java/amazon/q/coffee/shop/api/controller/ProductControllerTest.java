package amazon.q.coffee.shop.api.controller;

import amazon.q.coffee.shop.api.model.Product;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
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
public class ProductControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void testGetAllProducts() {
        var request = HttpRequest.GET("/api/products");
        var response = client.toBlocking().exchange(request, Argument.listOf(Product.class));
        
        assertEquals(HttpStatus.OK.getCode(), response.code());
        assertNotNull(response.body());
        List<Product> products = response.body();
        assertFalse(products.isEmpty());
        assertEquals(3, products.size());
    }

    @Test
    void testCreateProduct() {
        Product newProduct = new Product();
        newProduct.setName("Green Tea");
        newProduct.setDescription("Refreshing green tea");
        newProduct.setPrice(new BigDecimal("2.25"));
        newProduct.setCategory("TEA");
        
        var request = HttpRequest.POST("/api/products", newProduct);
        var response = client.toBlocking().exchange(request, Product.class);
        
        assertEquals(HttpStatus.CREATED.getCode(), response.code());
        assertNotNull(response.body());
        assertEquals("Green Tea", response.body().getName());
        assertNotNull(response.body().getId());
    }
}