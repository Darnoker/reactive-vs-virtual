package pl.edu.ug.kglab.ReactiveTestApp.model.product;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Review {

    private String reviewer;

    private Integer rating;

    private String comment;

    private LocalDate reviewDate;
}
