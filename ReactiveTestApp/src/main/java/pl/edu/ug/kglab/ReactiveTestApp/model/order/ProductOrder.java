package pl.edu.ug.kglab.ReactiveTestApp.model.order;

import lombok.Data;

@Data
public class ProductOrder {

    private String productId;

    private Integer quantity;

    private Details details;

}
