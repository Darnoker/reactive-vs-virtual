package pl.edu.ug.kglab.ReactiveTestApp.model.order;

import lombok.Data;

@Data
public class Payment {

    private String method;

    private String transactionNumber;
}
