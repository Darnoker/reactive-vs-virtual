package pl.edu.ug.kglab.VirtualThreadsTestApp.orderManagement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.ug.kglab.VirtualThreadsTestApp.order.model.Order;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/order/manage")
@Slf4j
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
    public CompletableFuture<ResponseEntity<Order>> addNewOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        return CompletableFuture.supplyAsync(() -> orderManagementService.addNewOrder(createOrderRequest))
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> {
                    log.error("Wystąpił błąd podczas dodawania zamówienia", ex);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                });
    }
}
