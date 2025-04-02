package pl.edu.ug.kglab.VirtualThreadsTestApp.order.model;

import lombok.Builder;
import lombok.Data;
import pl.edu.ug.kglab.VirtualThreadsTestApp.user.model.Address;

@Data
@Builder
public class Shipping {

    private Address address;

    private String method;

    private String trackingNumber;
}
