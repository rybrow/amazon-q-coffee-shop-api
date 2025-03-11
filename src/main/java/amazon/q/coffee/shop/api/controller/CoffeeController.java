package amazon.q.coffee.shop.api.controller;

import amazon.q.coffee.shop.api.model.Coffee;
import amazon.q.coffee.shop.api.repository.CoffeeRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@Controller("/api/coffees")
public class CoffeeController {

    private final CoffeeRepository coffeeRepository;

    @Inject
    public CoffeeController(CoffeeRepository coffeeRepository) {
        this.coffeeRepository = coffeeRepository;
    }

    @Get
    public List<Coffee> getAllCoffees() {
        return coffeeRepository.findAll();
    }

    @Get("/{id}")
    public HttpResponse<Coffee> getCoffeeById(Long id) {
        return coffeeRepository.findById(id)
                .map(HttpResponse::ok)
                .orElse(HttpResponse.notFound());
    }

    @Post
    public HttpResponse<Coffee> createCoffee(@Body Coffee coffee) {
        Coffee savedCoffee = coffeeRepository.save(coffee);
        return HttpResponse.created(savedCoffee);
    }

    @Put("/{id}")
    public HttpResponse<Coffee> updateCoffee(Long id, @Body Coffee coffee) {
        Optional<Coffee> existingCoffee = coffeeRepository.findById(id);
        if (existingCoffee.isEmpty()) {
            return HttpResponse.notFound();
        }
        
        coffee.setId(id);
        Coffee updatedCoffee = coffeeRepository.update(coffee);
        return HttpResponse.ok(updatedCoffee);
    }

    @Delete("/{id}")
    public HttpResponse<?> deleteCoffee(Long id) {
        coffeeRepository.deleteById(id);
        return HttpResponse.noContent();
    }
}