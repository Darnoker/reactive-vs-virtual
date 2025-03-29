package pl.edu.ug.kglab.ReactiveTestApp.product.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Data
@Document(collection = "products")
public class Product {

    @Id
    private String id;

    private String name;

    private String description;

    private BigDecimal price;

    private Category category;

    private List<String> tags;

    private List<Review> reviews;

}
