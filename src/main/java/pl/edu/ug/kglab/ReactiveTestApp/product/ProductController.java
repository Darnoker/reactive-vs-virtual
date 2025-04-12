package pl.edu.ug.kglab.ReactiveTestApp.product;

import org.springframework.web.bind.annotation.*;
import pl.edu.ug.kglab.ReactiveTestApp.product.dto.CreateReviewRequestBody;
import pl.edu.ug.kglab.ReactiveTestApp.product.model.Review;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/add-review")
    public Mono<Review> addReview(@RequestBody CreateReviewRequestBody reviewRequestBody) {
        return productService.createReview(reviewRequestBody);
    }
}
