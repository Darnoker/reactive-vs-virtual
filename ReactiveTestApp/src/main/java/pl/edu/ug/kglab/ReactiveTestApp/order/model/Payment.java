package pl.edu.ug.kglab.ReactiveTestApp.order.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Payment {

    private String method;

    private String transactionNumber;
}
