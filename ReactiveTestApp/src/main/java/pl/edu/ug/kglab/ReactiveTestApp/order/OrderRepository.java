package pl.edu.ug.kglab.ReactiveTestApp.order;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pl.edu.ug.kglab.ReactiveTestApp.order.model.Order;

public interface OrderRepository extends ReactiveMongoRepository<Order, String> {
}
