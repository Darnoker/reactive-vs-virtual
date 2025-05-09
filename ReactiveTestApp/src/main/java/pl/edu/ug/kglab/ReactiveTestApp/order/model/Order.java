package pl.edu.ug.kglab.ReactiveTestApp.order.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    private String userId;

    private List<ProductOrder> products;

    private LocalDate orderDate;

    private BigDecimal total;

    private Shipping shipping;

    private Payment payment;
}
