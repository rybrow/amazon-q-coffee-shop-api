package amazon.q.coffee.shop.api.repository;

import amazon.q.coffee.shop.api.model.Product;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends CrudRepository<Product, UUID> {
    
    List<Product> findAll();
    
    List<Product> findByCategory(String category);
}