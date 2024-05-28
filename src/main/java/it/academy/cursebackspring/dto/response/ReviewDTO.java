package it.academy.cursebackspring.dto.response;

import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDTO {

    @Min(value = 1, message = Constants.USER_ID_VALIDATION_EXCEPTION)
    private Long userId;

    @NotBlank(message = Constants.NAME_CANNOT_BE_BLANK_VALIDATION_EXCEPTION)
    private String name;

    @NotBlank(message = Constants.SURNAME_CANNOT_BE_BLANK_VALIDATION_EXCEPTION)
    private String surname;

    @NotBlank
    private String description;

    @DecimalMin(value = "0", message = Constants.RATING_MUST_BE_BETWEEN_VALIDATION_EXCEPTION)
    @DecimalMax(value = "10", message = Constants.RATING_MUST_BE_BETWEEN_VALIDATION_EXCEPTION)
    private Double rating;

}
