package pl.edu.ug.kglab.VirtualThreadsTestApp.order.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Payment {

    private String method;

    private String transactionNumber;
}
