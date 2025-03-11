package amazon.q.coffee.shop.api.controller;

import amazon.q.coffee.shop.api.model.Product;
import amazon.q.coffee.shop.api.repository.ProductRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller("/api/products")
public class ProductController {

    private final ProductRepository productRepository;

    @Inject
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Get
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Get("/{id}")
    public HttpResponse<Product> getProductById(UUID id) {
        return productRepository.findById(id)
                .map(HttpResponse::ok)
                .orElse(HttpResponse.notFound());
    }

    @Get("/category/{category}")
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    @Post
    public HttpResponse<Product> createProduct(@Body Product product) {
        Product savedProduct = productRepository.save(product);
        return HttpResponse.created(savedProduct);
    }

    @Put("/{id}")
    public HttpResponse<Product> updateProduct(UUID id, @Body Product product) {
        Optional<Product> existingProduct = productRepository.findById(id);
        
        if (existingProduct.isEmpty()) {
            return HttpResponse.notFound();
        }
        
        product.setId(id);
        Product updatedProduct = productRepository.update(product);
        return HttpResponse.ok(updatedProduct);
    }

    @Delete("/{id}")
    public HttpResponse<?> deleteProduct(UUID id) {
        productRepository.deleteById(id);
        return HttpResponse.noContent();
    }
}