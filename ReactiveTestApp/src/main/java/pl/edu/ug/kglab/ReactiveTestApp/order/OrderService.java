package pl.edu.ug.kglab.ReactiveTestApp.order;

import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.cglib.core.Local;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.ug.kglab.ReactiveTestApp.order.model.Order;
import pl.edu.ug.kglab.ReactiveTestApp.order.model.Payment;
import pl.edu.ug.kglab.ReactiveTestApp.order.model.Shipping;
import pl.edu.ug.kglab.ReactiveTestApp.product.model.Product;
import pl.edu.ug.kglab.ReactiveTestApp.product.ProductRepository;
import pl.edu.ug.kglab.ReactiveTestApp.user.model.Address;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public Mono<Order> getOrder(String orderId) {
        return orderRepository.findById(orderId);
    }

    public Mono<List<Product>> getListOfProductsForOrder(String orderId) {
        return orderRepository.findById(orderId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Order not found with id: " + orderId)))
                .flatMap(this::mapToProductsList);
    }

    public Mono<Order> addNewOrder(Order order) {
        return orderRepository.save(order);
    }

    public Mono<Void> deleteOrder(Order order) {
        return orderRepository.delete(order);
    }


    public Flux<Order> getAllOrdersForUser(String userId) {
        ObjectId userObjectId = new ObjectId(userId);
        return orderRepository.findOrdersByUserId(userObjectId);
    }


    private Mono<List<Product>> mapToProductsList(Order order) {
        return Flux.fromIterable(order.getProducts())
                .flatMap(productOrder -> productRepository.findById(productOrder.getProductId()))
                .collectList();
    }

    public Flux<String> getFirstOrderIds(int limit) {
        log.info("Pobieranie {} pierwszych id orderow...", limit);
        return orderRepository.findAll()
                .take(limit)
                .map(order -> order.getId() + "\n");
    }
}
