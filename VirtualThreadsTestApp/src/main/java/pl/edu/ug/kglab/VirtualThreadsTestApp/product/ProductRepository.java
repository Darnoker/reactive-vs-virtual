package pl.edu.ug.kglab.VirtualThreadsTestApp.product;


import org.springframework.data.mongodb.repository.MongoRepository;
import pl.edu.ug.kglab.VirtualThreadsTestApp.product.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

    boolean existsById(String id);
}
