package it.academy.cursebackspring.dto.response;

import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserReviewInfoDTO {

    @NotBlank
    private String description;

    @NotNull
    @DecimalMin(value = "0", message = Constants.RATING_MUST_BE_BETWEEN_VALIDATION_EXCEPTION)
    @DecimalMax(value = "10", message = Constants.RATING_MUST_BE_BETWEEN_VALIDATION_EXCEPTION)
    private Double rating;

    @NotNull
    @Min(value = 1, message = Constants.PRODUCT_ID_VALIDATION_EXCEPTION)
    private Long productId;

    @NotBlank(message = Constants.IMAGE_LINK_CANNOT_BE_EMPTY_VALIDATION_EXCEPTION)
    private String imageLink;

    @Pattern(regexp = Constants.REG_EXP_PRODUCT_NAME)
    private String name;

}
