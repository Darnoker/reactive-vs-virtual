package pl.edu.ug.kglab.ReactiveTestApp.product;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pl.edu.ug.kglab.ReactiveTestApp.product.model.Product;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {

    Mono<Boolean> existsById(String id);
}
