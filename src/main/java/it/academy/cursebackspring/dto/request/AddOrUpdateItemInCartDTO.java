package it.academy.cursebackspring.dto.request;

import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddOrUpdateItemInCartDTO {

    @Min(value = 1, message = Constants.QUANTITY_CANNOT_BE_LESS_THAN_VALIDATION_EXCEPTION)
    private Integer quantity;

    @Min(value = 1, message = Constants.PRODUCT_ID_VALIDATION_EXCEPTION)
    private Long productId;

    @Min(value = 1, message = Constants.USER_ID_VALIDATION_EXCEPTION)
    private Long userId;
}
