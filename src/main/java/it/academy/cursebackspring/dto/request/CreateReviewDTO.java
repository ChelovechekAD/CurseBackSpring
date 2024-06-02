package it.academy.cursebackspring.dto.request;

import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateReviewDTO implements Serializable {

    @NotBlank(message = Constants.DESCRIPTION_CANNOT_BE_NULL_OR_BLANK)
    private String description;

    @NotNull(message = Constants.RATING_CANNOT_BE_NULL_VALIDATION_EXCEPTION)
    @DecimalMin(value = Constants.MIN_RATING, message = Constants.RATING_MUST_BE_BETWEEN_VALIDATION_EXCEPTION)
    @DecimalMax(value = Constants.MAX_RATING, message = Constants.RATING_MUST_BE_BETWEEN_VALIDATION_EXCEPTION)
    private Double rating;

    private Long productId;

    @NotNull(message = Constants.USER_ID_CANNOT_BE_NULL_VALIDATION_EXCEPTION)
    @Min(value = Constants.MIN_USER_ID, message = Constants.USER_ID_VALIDATION_EXCEPTION)
    private Long userId;
}
