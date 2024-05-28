package it.academy.cursebackspring.dto.request;

import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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

    @Min(value = 1, message = Constants.CATEGORY_ID_VALIDATION_EXCEPTION)
    private Long categoryId;

    @NotBlank
    @Pattern(regexp = Constants.REG_EXP_PRODUCT_NAME, message = Constants.PRODUCT_NAME_NOT_MATCH_PATTERN_VALIDATION_EXCEPTION)
    private String name;

    @NotBlank
    private String description;

    @DecimalMin(value = "0.01", message = Constants.PRICE_CANNOT_BE_LESS_THAN_VALIDATION_EXCEPTION)
    private Double price;

    @NotBlank(message = Constants.IMAGE_LINK_CANNOT_BE_EMPTY_VALIDATION_EXCEPTION)
    private String imageLink;

}
