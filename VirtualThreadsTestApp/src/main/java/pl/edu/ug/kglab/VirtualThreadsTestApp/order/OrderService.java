package pl.edu.ug.kglab.VirtualThreadsTestApp.order;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import pl.edu.ug.kglab.VirtualThreadsTestApp.order.model.Order;
import pl.edu.ug.kglab.VirtualThreadsTestApp.product.ProductRepository;
import pl.edu.ug.kglab.VirtualThreadsTestApp.product.model.Product;


import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public Optional<Order> getOrder(String orderId) {
        return orderRepository.findById(orderId);
    }

    public List<Product> getListOfProductsForOrder(String orderId) {
        return orderRepository.findById(orderId).stream()
                .flatMap(order -> mapToProductsList(order).stream())
                .toList();
    }

    public Order addNewOrder(Order order) {
        return orderRepository.save(order);
    }

    public void deleteOrder(Order order) {
        orderRepository.delete(order);
    }


    public List<Order> getAllOrdersForUser(String userId) {
        ObjectId userObjectId = new ObjectId(userId);
        return orderRepository.findOrdersByUserId(userObjectId);
    }


    private List<Product> mapToProductsList(Order order) {
        return order.getProducts().stream()
                .map(productOrder ->
                        productRepository.findById(productOrder.getProductId())
                                .orElseThrow()
                )
                .toList();
    }

    public List<String> getFirstOrderIds(int limit) {
        log.info("Pobieranie {} pierwszych id orderow...", limit);
        return orderRepository.findAll().stream()
                .limit(limit)
                .map(Order::getId)
                .toList();
    }
}
