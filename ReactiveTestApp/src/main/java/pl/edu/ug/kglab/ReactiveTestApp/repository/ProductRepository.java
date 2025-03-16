package pl.edu.ug.kglab.ReactiveTestApp.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pl.edu.ug.kglab.ReactiveTestApp.model.product.Product;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
}
