package amazon.q.coffee.shop.api.repository;

import amazon.q.coffee.shop.api.model.Coffee;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface CoffeeRepository extends CrudRepository<Coffee, Long> {
    List<Coffee> findAll();
}