package pl.edu.ug.kglab.ReactiveTestApp.model.user;

import lombok.Data;

@Data
public class Address {

    private String street;

    private String city;

    private String country;

    private String zipCode;
}
