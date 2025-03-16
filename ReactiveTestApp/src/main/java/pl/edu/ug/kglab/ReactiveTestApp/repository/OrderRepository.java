package pl.edu.ug.kglab.ReactiveTestApp.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pl.edu.ug.kglab.ReactiveTestApp.model.order.Order;

public interface OrderRepository extends ReactiveMongoRepository<Order, String> {
}
