package pl.edu.ug.kglab.VirtualThreadsTestApp.orderManagement;

import org.springframework.web.bind.annotation.*;
import pl.edu.ug.kglab.VirtualThreadsTestApp.order.model.Order;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/order/manage")
public class OrderManagementController {

    private final OrderManagementService orderManagementService;

    public OrderManagementController(OrderManagementService orderManagementService) {
        this.orderManagementService = orderManagementService;
    }

    @GetMapping("/{orderId}")
    public CompletableFuture<UserProductInfo> getUserProductInfo(@PathVariable String orderId) {
        return CompletableFuture.supplyAsync(() -> orderManagementService.getUserProductInfo(orderId));
    }

    @PostMapping("/add")
    public CompletableFuture<Order> addNewOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        return CompletableFuture.supplyAsync(() -> orderManagementService.addNewOrder(createOrderRequest));
    }
}
