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
public class CartItemDTO {

    @Min(value = 1, message = Constants.QUANTITY_CANNOT_BE_LESS_THAN_VALIDATION_EXCEPTION)
    private Integer quantity;

    @Min(value = 1, message = Constants.PRODUCT_ID_VALIDATION_EXCEPTION)
    private Long productId;

    @Pattern(regexp = Constants.REG_EXP_PRODUCT_NAME)
    private String name;

    @NotBlank
    private String imageLink;

    @DecimalMin(value = "0.01", message = Constants.PRICE_CANNOT_BE_LESS_THAN_VALIDATION_EXCEPTION)
    private Double price;

    @DecimalMin(value = "0", message = Constants.RATING_MUST_BE_BETWEEN_VALIDATION_EXCEPTION)
    @DecimalMax(value = "10", message = Constants.RATING_MUST_BE_BETWEEN_VALIDATION_EXCEPTION)
    private Double rating;
}
