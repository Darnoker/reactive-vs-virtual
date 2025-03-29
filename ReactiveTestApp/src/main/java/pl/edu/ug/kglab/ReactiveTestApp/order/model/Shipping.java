package pl.edu.ug.kglab.ReactiveTestApp.order.model;

import lombok.Data;
import pl.edu.ug.kglab.ReactiveTestApp.user.model.Address;

@Data
public class Shipping {

    private Address address;

    private String method;

    private String trackingNumber;
}
