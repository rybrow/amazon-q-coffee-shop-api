package amazon.q.coffee.shop.api.controller;

import amazon.q.coffee.shop.api.model.CoffeeOrder;
import amazon.q.coffee.shop.api.repository.CoffeeOrderRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@Controller("/api/orders")
public class OrderController {

    private final CoffeeOrderRepository orderRepository;

    @Inject
    public OrderController(CoffeeOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Get
    public List<CoffeeOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    @Get("/{id}")
    public HttpResponse<CoffeeOrder> getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(HttpResponse::ok)
                .orElse(HttpResponse.notFound());
    }

    @Get("/customer/{name}")
    public List<CoffeeOrder> getOrdersByCustomerName(String name) {
        return orderRepository.findByCustomerName(name);
    }

    @Post
    public HttpResponse<CoffeeOrder> createOrder(@Body CoffeeOrder order) {
        CoffeeOrder savedOrder = orderRepository.save(order);
        return HttpResponse.created(savedOrder);
    }

    @Put("/{id}")
    public HttpResponse<CoffeeOrder> updateOrder(Long id, @Body CoffeeOrder order) {
        Optional<CoffeeOrder> existingOrder = orderRepository.findById(id);
        if (existingOrder.isEmpty()) {
            return HttpResponse.notFound();
        }
        
        order.setId(id);
        CoffeeOrder updatedOrder = orderRepository.update(order);
        return HttpResponse.ok(updatedOrder);
    }

    @Put("/{id}/status")
    public HttpResponse<CoffeeOrder> updateOrderStatus(Long id, @Body StatusUpdateRequest request) {
        Optional<CoffeeOrder> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isEmpty()) {
            return HttpResponse.notFound();
        }
        
        CoffeeOrder order = optionalOrder.get();
        order.setStatus(request.getStatus());
        CoffeeOrder updatedOrder = orderRepository.update(order);
        return HttpResponse.ok(updatedOrder);
    }

    @Delete("/{id}")
    public HttpResponse<?> deleteOrder(Long id) {
        orderRepository.deleteById(id);
        return HttpResponse.noContent();
    }

    public static class StatusUpdateRequest {
        private CoffeeOrder.OrderStatus status;

        public CoffeeOrder.OrderStatus getStatus() {
            return status;
        }

        public void setStatus(CoffeeOrder.OrderStatus status) {
            this.status = status;
        }
    }
}