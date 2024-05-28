package it.academy.cursebackspring.dto.response;


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
public class ProductDTO implements Serializable {


    @Min(value = 1, message = Constants.PRODUCT_ID_VALIDATION_EXCEPTION)
    private Long id;

    @Min(value = 1, message = Constants.CATEGORY_ID_VALIDATION_EXCEPTION)
    private Long categoryId;

    @Pattern(regexp = Constants.REG_EXP_PRODUCT_NAME)
    private String name;

    @NotBlank
    private String description;

    @DecimalMin(value = "0.01", message = Constants.PRICE_CANNOT_BE_LESS_THAN_VALIDATION_EXCEPTION)
    private Double price;

    @NotBlank
    private String imageLink;

    @Size(max = 10, message = Constants.RATING_MUST_BE_BETWEEN_VALIDATION_EXCEPTION)
    private Double rating;
}
