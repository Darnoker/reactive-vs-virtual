package pl.edu.ug.kglab.VirtualThreadsTestApp.product;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.ug.kglab.VirtualThreadsTestApp.product.dto.CreateReviewRequestBody;
import pl.edu.ug.kglab.VirtualThreadsTestApp.product.model.Review;

import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/add-review")
    public CompletableFuture<Review> addReview(@RequestBody CreateReviewRequestBody reviewRequestBody) {
        return CompletableFuture.supplyAsync(() -> productService.createReview(reviewRequestBody));
    }
}
