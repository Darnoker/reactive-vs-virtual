package pl.edu.ug.kglab.ReactiveTestApp.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import pl.edu.ug.kglab.ReactiveTestApp.order.model.Order;
import pl.edu.ug.kglab.ReactiveTestApp.product.model.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/products/{orderId}")
    public Mono<List<Product>> getProductsForOrder(@PathVariable String orderId) {
        return orderService.getListOfProductsForOrder(orderId);
    }

    @GetMapping("/user/{userId}")
    public Flux<Order>  getAllOrdersOfUser(@PathVariable String userId) {
        return orderService.getAllOrdersForUser(userId);
    }

    @GetMapping("/ids/{limit}")
    public Flux<String> getFirstOrderIds(@PathVariable Integer limit) {
        return orderService.getFirstOrderIds(limit);
    }
}
