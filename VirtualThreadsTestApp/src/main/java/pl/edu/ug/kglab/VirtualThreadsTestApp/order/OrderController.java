package pl.edu.ug.kglab.VirtualThreadsTestApp.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.ug.kglab.VirtualThreadsTestApp.order.model.Order;
import pl.edu.ug.kglab.VirtualThreadsTestApp.product.model.Product;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/products/{orderId}")
    public CompletableFuture<List<Product>> getProductsForOrder(@PathVariable String orderId) {
        return CompletableFuture.supplyAsync(() -> orderService.getListOfProductsForOrder(orderId));
    }

    @GetMapping("/user/{userId}")
    public CompletableFuture<List<Order>> getAllOrdersOfUser(@PathVariable String userId) {
        return CompletableFuture.supplyAsync(() -> orderService.getAllOrdersForUser(userId));
    }

    @GetMapping("/ids/{limit}")
    public CompletableFuture<List<String>> getFirstOrderIds(@PathVariable Integer limit) {
        return CompletableFuture.supplyAsync(() -> orderService.getFirstOrderIds(limit));
    }
}
