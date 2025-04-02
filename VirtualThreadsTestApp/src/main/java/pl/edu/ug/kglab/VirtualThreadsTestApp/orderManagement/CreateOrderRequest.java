package pl.edu.ug.kglab.VirtualThreadsTestApp.orderManagement;


import pl.edu.ug.kglab.VirtualThreadsTestApp.order.model.ProductOrder;

import java.util.List;

public record CreateOrderRequest(String userId, List<ProductOrder> products, Double total, String street, String city,
                                 String country, String zipCode, String shippingMethod, String paymentMethod) {

}
