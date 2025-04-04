package pl.edu.ug.kglab.VirtualThreadsTestApp.product;

import org.springframework.web.bind.annotation.*;
import pl.edu.ug.kglab.VirtualThreadsTestApp.product.dto.CreateReviewRequestBody;
import pl.edu.ug.kglab.VirtualThreadsTestApp.product.model.Review;

import java.util.List;
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

    @GetMapping("/list/{limit}")
    public CompletableFuture<List<String>> getFirstProducts(@PathVariable Integer limit) {
        return CompletableFuture.supplyAsync(() -> productService.getFirstProducts(limit));
    }
}
