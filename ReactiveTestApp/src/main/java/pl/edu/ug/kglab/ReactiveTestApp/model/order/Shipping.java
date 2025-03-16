package pl.edu.ug.kglab.ReactiveTestApp.model.order;

import lombok.Data;
import pl.edu.ug.kglab.ReactiveTestApp.model.user.Address;

@Data
public class Shipping {

    private Address address;

    private String method;

    private String trackingNumber;
}
