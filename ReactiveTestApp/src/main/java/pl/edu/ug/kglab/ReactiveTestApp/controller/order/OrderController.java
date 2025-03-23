package pl.edu.ug.kglab.ReactiveTestApp.controller.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.ug.kglab.ReactiveTestApp.model.product.Product;
import pl.edu.ug.kglab.ReactiveTestApp.service.OrderService;
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

    @GetMapping("/ids/{limit}")
    public Flux<String> getFirstOrderIds(@PathVariable Integer limit) {
        return orderService.getFirstOrderIds(limit);
    }
}
