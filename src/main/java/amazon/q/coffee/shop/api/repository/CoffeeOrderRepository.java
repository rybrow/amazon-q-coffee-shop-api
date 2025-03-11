package amazon.q.coffee.shop.api.repository;

import amazon.q.coffee.shop.api.model.CoffeeOrder;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface CoffeeOrderRepository extends CrudRepository<CoffeeOrder, Long> {
    List<CoffeeOrder> findAll();
    List<CoffeeOrder> findByCustomerName(String customerName);
}