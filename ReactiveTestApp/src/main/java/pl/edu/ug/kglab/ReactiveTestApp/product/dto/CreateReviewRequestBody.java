package pl.edu.ug.kglab.ReactiveTestApp.product.dto;

public record CreateReviewRequestBody(String productId, String reviewer, Integer rating, String comment) {
}
