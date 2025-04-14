package pl.edu.ug.kglab.VirtualThreadsTestApp.product.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Data
@Document(collection = "products")
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    private String id;

    private String name;

    private String description;

    private BigDecimal price;

    private Category category;

    private List<String> tags;

    private List<Review> reviews;

    public Product(Product other) {
        this.id = other.id;
        this.name = other.name;
        this.description = other.description;
        this.price = other.price;
        this.category = other.category;
        this.tags = other.tags;
        this.reviews = other.reviews;
    }
}
