package amazon.q.coffee.shop.api.controller;

import amazon.q.coffee.shop.api.model.Product;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller("/api/products")
@Tag(name = "Products", description = "Product management endpoints")
public class ProductController {

    private final List<Product> products = new ArrayList<>();

    public ProductController() {
        // Add some sample products
        products.add(new Product(UUID.randomUUID(), "Espresso", "Strong coffee", new BigDecimal("2.50"), "COFFEE"));
        products.add(new Product(UUID.randomUUID(), "Cappuccino", "Coffee with steamed milk", new BigDecimal("3.50"), "COFFEE"));
        products.add(new Product(UUID.randomUUID(), "Chocolate Muffin", "Sweet pastry", new BigDecimal("2.75"), "PASTRY"));
    }

    @Get(produces = MediaType.APPLICATION_JSON)
    @Operation(summary = "Get all products",
            description = "Returns a list of all available products")
    @ApiResponse(
            responseCode = "200",
            description = "List of products",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Product.class))
    )
    public List<Product> getAllProducts() {
        return products;
    }

    @Get(uri = "/{id}", produces = MediaType.APPLICATION_JSON)
    @Operation(summary = "Get product by ID",
            description = "Returns a product with the specified ID")
    @ApiResponse(
            responseCode = "200",
            description = "Product found",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Product.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Product not found"
    )
    public HttpResponse<Product> getProductById(UUID id) {
        return products.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .map(HttpResponse::ok)
                .orElse(HttpResponse.notFound());
    }

    @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    @Operation(summary = "Create a new product",
            description = "Creates a new product with the provided details")
    @ApiResponse(
            responseCode = "201",
            description = "Product created successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Product.class))
    )
    public HttpResponse<Product> createProduct(@Body Product product) {
        product.setId(UUID.randomUUID());
        products.add(product);
        return HttpResponse.created(product);
    }
}