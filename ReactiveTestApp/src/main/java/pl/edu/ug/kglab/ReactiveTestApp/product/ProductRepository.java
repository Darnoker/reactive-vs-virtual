package pl.edu.ug.kglab.ReactiveTestApp.product;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pl.edu.ug.kglab.ReactiveTestApp.product.model.Product;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
}
