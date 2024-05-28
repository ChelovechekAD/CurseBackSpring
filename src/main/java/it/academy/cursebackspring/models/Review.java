package it.academy.cursebackspring.models;

import it.academy.cursebackspring.models.embedded.ReviewPK;
import it.academy.cursebackspring.utilities.Constants;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Table(name = "reviews")
public class Review implements Serializable {

    @EmbeddedId
    private ReviewPK reviewPK;

    @Column(nullable = false)
    @DecimalMin(value = "0", message = Constants.RATING_MUST_BE_BETWEEN_VALIDATION_EXCEPTION)
    @DecimalMax(value = "10", message = Constants.RATING_MUST_BE_BETWEEN_VALIDATION_EXCEPTION)
    private Double rating;

    @Column(length = 500)
    private String description;


}
