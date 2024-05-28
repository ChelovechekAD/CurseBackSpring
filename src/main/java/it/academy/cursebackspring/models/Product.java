package it.academy.cursebackspring.models;

import it.academy.cursebackspring.utilities.Constants;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "category_id")
    private Category category;

    @Column(nullable = false)
    private String name;

    @DecimalMin(value = "0", message = Constants.RATING_MUST_BE_BETWEEN_VALIDATION_EXCEPTION)
    @DecimalMax(value = "10", message = Constants.RATING_MUST_BE_BETWEEN_VALIDATION_EXCEPTION)
    @Column(nullable = false)
    @Builder.Default
    private Double rating = 0d;

    @Column(length = 1500, nullable = false)
    private String description;

    @Column(nullable = false)
    @DecimalMin(value = "0.01", message = Constants.PRICE_CANNOT_BE_LESS_THAN_VALIDATION_EXCEPTION)
    private Double price;

    @Column(nullable = false, name = "image_link")
    private String imageLink;


}
