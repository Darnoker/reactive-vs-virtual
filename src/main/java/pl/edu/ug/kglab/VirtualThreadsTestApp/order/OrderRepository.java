package pl.edu.ug.kglab.VirtualThreadsTestApp.order;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import pl.edu.ug.kglab.VirtualThreadsTestApp.order.model.Order;

import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {

    List<Order> findOrdersByUserId(ObjectId userId);
}
