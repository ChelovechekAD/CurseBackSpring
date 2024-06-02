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
public class CreateProductDTO implements Serializable {

    @NotNull(message = Constants.CATEGORY_ID_CANNOT_BE_NULL_VALIDATION_EXCEPTION)
    @Min(value = Constants.MIN_CATEGORY_ID, message = Constants.CATEGORY_ID_VALIDATION_EXCEPTION)
    private Long categoryId;

    @NotBlank(message = Constants.PRODUCT_NAME_CANNOT_BE_BLANK_OR_NULL)
    @Pattern(regexp = Constants.REG_EXP_PRODUCT_NAME, message = Constants.PRODUCT_NAME_NOT_MATCH_PATTERN_VALIDATION_EXCEPTION)
    private String name;

    @NotBlank(message = Constants.DESCRIPTION_CANNOT_BE_NULL_OR_BLANK)
    private String description;

    @NotNull(message = Constants.PRICE_CANNOT_BE_NULL_VALIDATION_EXCEPTION)
    @DecimalMin(value = Constants.MIN_PRICE, message = Constants.PRICE_CANNOT_BE_LESS_THAN_VALIDATION_EXCEPTION)
    private Double price;

    @NotBlank(message = Constants.IMAGE_LINK_CANNOT_BE_EMPTY_VALIDATION_EXCEPTION)
    private String imageLink;

}
