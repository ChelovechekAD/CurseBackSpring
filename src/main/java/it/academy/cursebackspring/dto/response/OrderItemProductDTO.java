package it.academy.cursebackspring.dto.response;

import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemProductDTO {

    @Min(value = 1, message = Constants.PRODUCT_ID_VALIDATION_EXCEPTION)
    private Long productId;

    @Min(value = 0, message = Constants.COUNT_CANNOT_BE_LESS_THAN_VALIDATION_EXCEPTION)
    private Long count;

    @DecimalMin(value = "0.01", message = Constants.PRICE_CANNOT_BE_LESS_THAN_VALIDATION_EXCEPTION)
    private Double price;

    @NotBlank
    @Pattern(regexp = Constants.REG_EXP_PRODUCT_NAME, message = Constants.PRODUCT_NAME_NOT_MATCH_PATTERN_VALIDATION_EXCEPTION)
    private String name;

    @NotBlank(message = Constants.IMAGE_LINK_CANNOT_BE_EMPTY_VALIDATION_EXCEPTION)
    private String imageLink;
}

