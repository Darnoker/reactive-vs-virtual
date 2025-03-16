package pl.edu.ug.kglab.ReactiveTestApp.service;

import org.springframework.stereotype.Service;
import pl.edu.ug.kglab.ReactiveTestApp.model.order.Order;
import pl.edu.ug.kglab.ReactiveTestApp.model.order.ProductOrder;
import pl.edu.ug.kglab.ReactiveTestApp.model.product.Product;
import pl.edu.ug.kglab.ReactiveTestApp.repository.OrderRepository;
import pl.edu.ug.kglab.ReactiveTestApp.repository.ProductRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public Mono<List<Product>> getListOfProductsForOrder(String orderId) {
        return orderRepository.findById(orderId)
                .flatMap(this::mapToProductsList);
    }

    private Mono<List<Product>> mapToProductsList(Order order) {
        return Flux.fromIterable(order.getProducts())
                .flatMap(productOrder -> productRepository.findById(productOrder.getProductId()))
                .collectList();
    }
}
