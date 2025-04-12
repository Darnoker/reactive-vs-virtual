package pl.edu.ug.kglab.ReactiveTestApp.product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import pl.edu.ug.kglab.ReactiveTestApp.product.dto.CreateReviewRequestBody;
import pl.edu.ug.kglab.ReactiveTestApp.product.model.Product;
import pl.edu.ug.kglab.ReactiveTestApp.product.model.Review;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final TransactionalOperator transactionalOperator;

    public ProductService(ProductRepository productRepository, TransactionalOperator transactionalOperator) {
        this.productRepository = productRepository;
        this.transactionalOperator = transactionalOperator;
    }

    public Mono<Review> createReview(CreateReviewRequestBody createReviewRequestBody) {
        Review review = new Review();
        review.setReviewer(createReviewRequestBody.reviewer());
        review.setRating(createReviewRequestBody.rating());
        review.setComment(createReviewRequestBody.comment());
        review.setReviewDate(LocalDate.now());

        return productRepository.findById(createReviewRequestBody.productId())
                .as(transactionalOperator::transactional)
                .flatMap(product -> mapAndSaveReview(product, review));
    }

    public Mono<Boolean> doesProductExists(String productId) {
        return productRepository.existsById(productId);
    }

    private Mono<Review> mapAndSaveReview(Product product, Review review) {
        List<Review> newReviews = new LinkedList<>(product.getReviews());
        newReviews.add(review);
        Product backup = new Product(product);
        product.setReviews(newReviews);

        return productRepository.save(product)
                .flatMap(savedProduct -> productRepository.save(backup))
                .thenReturn(review);
    }

}
