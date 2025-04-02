package pl.edu.ug.kglab.VirtualThreadsTestApp.orderManagement;

import org.springframework.stereotype.Service;
import pl.edu.ug.kglab.VirtualThreadsTestApp.order.OrderService;
import pl.edu.ug.kglab.VirtualThreadsTestApp.order.model.Order;
import pl.edu.ug.kglab.VirtualThreadsTestApp.order.model.Payment;
import pl.edu.ug.kglab.VirtualThreadsTestApp.order.model.Shipping;
import pl.edu.ug.kglab.VirtualThreadsTestApp.product.ProductService;
import pl.edu.ug.kglab.VirtualThreadsTestApp.product.model.Product;
import pl.edu.ug.kglab.VirtualThreadsTestApp.user.UserService;
import pl.edu.ug.kglab.VirtualThreadsTestApp.user.model.Address;
import pl.edu.ug.kglab.VirtualThreadsTestApp.user.model.User;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class OrderManagementService {

    private final OrderService orderService;

    private final UserService userService;

    private final ProductService productService;

    public OrderManagementService(OrderService orderService, UserService userService, ProductService productService) {
        this.orderService = orderService;
        this.userService = userService;
        this.productService = productService;
    }

    public Order addNewOrder(CreateOrderRequest createOrderRequest) {
        boolean userExist = userService.doesUserExist(createOrderRequest.userId());
        boolean productExists = createOrderRequest.products().stream()
                .allMatch(productOrder -> productService.doesProductExists(productOrder.getProductId()));

        if (!userExist) {
            throw new RuntimeException("User does not exist");
        }
        if (!productExists) {
            throw new RuntimeException("One or more products do not exist");
        }

        Order order = new Order();
        order.setUserId(createOrderRequest.userId());
        order.setProducts(createOrderRequest.products());
        order.setShipping(buildShipping(createOrderRequest));
        order.setPayment(buildPayment(createOrderRequest));
        order.setTotal(BigDecimal.valueOf(createOrderRequest.total()));
        order.setOrderDate(LocalDate.now());

        orderService.addNewOrder(order);
        orderService.deleteOrder(order);

        return order;
    }

    public UserProductInfo getUserProductInfo(String orderId) {
        Order order = orderService.getOrder(orderId)
                .orElseThrow();

        User user = userService.getUser(order.getUserId())
                .orElseThrow();

        return getUserProductInfo(user, orderService.getListOfProductsForOrder(orderId));
    }

    private UserProductInfo getUserProductInfo(User user, List<Product> products) {
        UserProductInfo userProductInfo = new UserProductInfo();

        userProductInfo.setUserId(user.getId());
        userProductInfo.setName(user.getName());
        userProductInfo.setLastname(userProductInfo.getLastname());
        userProductInfo.setEmail(user.getEmail());

        List<ProductInfo> productInfos = products.stream()
                .map(this::getProductInfo)
                .toList();

        userProductInfo.setProductInfos(productInfos);

        return userProductInfo;
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
