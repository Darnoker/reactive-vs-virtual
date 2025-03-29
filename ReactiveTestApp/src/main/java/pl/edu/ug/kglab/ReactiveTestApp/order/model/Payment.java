package pl.edu.ug.kglab.ReactiveTestApp.order.model;

import lombok.Data;

@Data
public class Payment {

    private String method;

    private String transactionNumber;
}
