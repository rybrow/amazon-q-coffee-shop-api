package amazon.q.coffee.shop.api.repository;

import amazon.q.coffee.shop.api.model.Order;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends CrudRepository<Order, UUID> {
    
    List<Order> findAll();
    
    List<Order> findByCustomerName(String customerName);
    
    List<Order> findByStatus(String status);
}