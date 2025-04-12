package pl.edu.ug.kglab.ReactiveTestApp.orderInfo;

import org.springframework.web.bind.annotation.*;
import pl.edu.ug.kglab.ReactiveTestApp.order.model.Order;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/order/manage")
public class OrderManagementController {

    private final OrderManagementService orderManagementService;

    public OrderManagementController(OrderManagementService orderManagementService) {
        this.orderManagementService = orderManagementService;
    }

    @GetMapping("/users/all/orders/count")
    public Flux<UserOrdersResponse> getUserOrderData() {
        return orderManagementService.getUsersAndOrdersCount();
    }

    @GetMapping("/{orderId}")
    public Mono<UserProductInfo> getUserProductInfo(@PathVariable String orderId) {
        return orderManagementService.getUserProductInfo(orderId);
    }

    @PostMapping("/add")
    public Mono<Order> addNewOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        return orderManagementService.addNewOrder(createOrderRequest);
    }
}
