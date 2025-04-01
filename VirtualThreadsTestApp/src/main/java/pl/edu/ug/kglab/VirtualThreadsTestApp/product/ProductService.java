package pl.edu.ug.kglab.VirtualThreadsTestApp.product;

import org.springframework.stereotype.Service;
import pl.edu.ug.kglab.VirtualThreadsTestApp.product.dto.CreateReviewRequestBody;
import pl.edu.ug.kglab.VirtualThreadsTestApp.product.model.Product;
import pl.edu.ug.kglab.VirtualThreadsTestApp.product.model.Review;


import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;


    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Review createReview(CreateReviewRequestBody createReviewRequestBody) {
        Review review = new Review();
        review.setReviewer(createReviewRequestBody.reviewer());
        review.setRating(createReviewRequestBody.rating());
        review.setComment(createReviewRequestBody.comment());
        review.setReviewDate(LocalDate.now());


        Product productToSave = productRepository.findById(createReviewRequestBody.productId())
                .orElseThrow();

        Product backup = new Product(productToSave);

        productRepository.save(addReviewToProduct(productToSave, review));
        productRepository.save(backup);

        return review;
    }

    public boolean doesProductExists(String productId) {
        return productRepository.existsById(productId);
    }

    private Product addReviewToProduct(Product product, Review review) {
        List<Review> newReviews = new LinkedList<>(product.getReviews());
        newReviews.add(review);
        product.setReviews(newReviews);

        return product;
    }
}
