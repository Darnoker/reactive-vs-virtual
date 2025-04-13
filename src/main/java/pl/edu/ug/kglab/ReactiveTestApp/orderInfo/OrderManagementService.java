package pl.edu.ug.kglab.ReactiveTestApp.orderInfo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.edu.ug.kglab.ReactiveTestApp.order.OrderService;
import pl.edu.ug.kglab.ReactiveTestApp.order.model.Order;
import pl.edu.ug.kglab.ReactiveTestApp.order.model.Payment;
import pl.edu.ug.kglab.ReactiveTestApp.order.model.ProductOrder;
import pl.edu.ug.kglab.ReactiveTestApp.order.model.Shipping;
import pl.edu.ug.kglab.ReactiveTestApp.product.ProductService;
import pl.edu.ug.kglab.ReactiveTestApp.product.model.Product;
import pl.edu.ug.kglab.ReactiveTestApp.user.UserService;
import pl.edu.ug.kglab.ReactiveTestApp.user.model.Address;
import pl.edu.ug.kglab.ReactiveTestApp.user.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderManagementService {

    private final OrderService orderService;

    private final UserService userService;

    private final ProductService productService;

    public OrderManagementService(OrderService orderService, UserService userService, ProductService productService) {
        this.orderService = orderService;
        this.userService = userService;
        this.productService = productService;
    }

    public Mono<Order> addNewOrder(CreateOrderRequest createOrderRequest) {
        log.info("Starting adding new order ...");
        Mono<Boolean> userExists = userService.doesUserExists(createOrderRequest.userId());
        Mono<Boolean> allProductsExist = Flux.fromIterable(createOrderRequest.products())
                .map(ProductOrder::getProductId)
                .flatMap(productService::doesProductExists)
                .all(Boolean::booleanValue);


        return userExists
                .zipWith(allProductsExist)
                .flatMap(tuple -> {
                    Boolean userExistsResult = tuple.getT1();
                    Boolean allProductsExistResult = tuple.getT2();

                    if (!userExistsResult) {
                        return Mono.error(new RuntimeException("User does not exist"));
                    }
                    if (!allProductsExistResult) {
                        return Mono.error(new RuntimeException("One or more products do not exist"));
                    }

                    Order order = new Order();
                    order.setUserId(createOrderRequest.userId());
                    order.setProducts(createOrderRequest.products());
                    order.setShipping(buildShipping(createOrderRequest));
                    order.setPayment(buildPayment(createOrderRequest));
                    order.setTotal(BigDecimal.valueOf(createOrderRequest.total()));
                    order.setOrderDate(LocalDate.now());
                    return orderService.addNewOrder(order)
                            .flatMap(orderService::deleteOrder)
                            .thenReturn(order);
                })
                .doOnSuccess((order -> log.info("Successfully created order {}", order.getId())))
                .doOnError((error) -> log.error("There was an error with creating new order {} ", error.getMessage()))
                .doOnTerminate(() -> log.warn("Adding new order terminated."))
                .doOnCancel(() -> log.warn("Adding new order cancelled."))
                .doOnDiscard(Order.class, discarded -> log.warn("Adding new order discarded: {}", discarded));

    }

    public Flux<UserOrdersResponse> getUsersAndOrdersCount() {
        return userService.getAllUsers()
                .flatMap(user ->
                        orderService.getAllOrdersForUser(user.getId())
                                .count()
                                .map(ordersCount -> new UserOrdersResponse(user.getId(), ordersCount))
                );
    }

    public Mono<UserProductInfo> getUserProductInfo(String orderId) {
        return orderService.getOrder(orderId)
                .flatMap(order -> Mono.zip(userService.getUser(order.getUserId()), orderService.getListOfProductsForOrder(orderId))
                        .flatMap(this::mapToUserProductInfo));
    }

    private Mono<UserProductInfo> mapToUserProductInfo(Tuple2<User, List<Product>> param) {
        UserProductInfo userProductInfo = new UserProductInfo();

        User user = param.getT1();
        List<Product> products = param.getT2();

        userProductInfo.setUserId(user.getId());
        userProductInfo.setName(user.getName());
        userProductInfo.setLastname(userProductInfo.getLastname());
        userProductInfo.setEmail(user.getEmail());

        List<ProductInfo> productInfos = products.stream()
                .map(this::getProductInfo)
                .toList();

        userProductInfo.setProductInfos(productInfos);

        return Mono.just(userProductInfo);
    }

    private ProductInfo getProductInfo(Product product) {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setName(product.getName());
        productInfo.setDescription(product.getDescription());

        return productInfo;
    }

    private Shipping buildShipping(CreateOrderRequest createOrderRequest) {
        return Shipping.builder()
                .address(buildAddress(createOrderRequest))
                .trackingNumber(UUID.randomUUID().toString())
                .method(createOrderRequest.shippingMethod())
                .build();
    }

    private Payment buildPayment(CreateOrderRequest createOrderRequest) {
        return Payment.builder()
                .method(createOrderRequest.paymentMethod())
                .transactionNumber(UUID.randomUUID().toString())
                .build();
    }

    private Address buildAddress(CreateOrderRequest createOrderRequest) {
        return Address.builder()
                .country(createOrderRequest.country())
                .city(createOrderRequest.city())
                .zipCode(createOrderRequest.zipCode())
                .street(createOrderRequest.street())
                .build();
    }


}
