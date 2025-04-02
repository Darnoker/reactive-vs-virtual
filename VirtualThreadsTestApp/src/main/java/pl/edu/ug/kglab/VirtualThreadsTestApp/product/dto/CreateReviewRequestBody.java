package pl.edu.ug.kglab.VirtualThreadsTestApp.product.dto;

public record CreateReviewRequestBody(String productId, String reviewer, Integer rating, String comment) {
}
