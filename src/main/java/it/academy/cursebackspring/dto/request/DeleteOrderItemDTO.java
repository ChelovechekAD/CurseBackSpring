package it.academy.cursebackspring.dto.request;

import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeleteOrderItemDTO {

    @NotNull(message = Constants.PRODUCT_ID_CANNOT_BE_NULL_VALIDATION_EXCEPTION)
    @Min(value = Constants.MIN_PRODUCT_ID, message = Constants.PRODUCT_ID_VALIDATION_EXCEPTION)
    private Long productId;

    @NotNull(message = Constants.ORDER_ID_CANNOT_BE_NULL_VALIDATION_EXCEPTION)
    @Min(value = Constants.MIN_ORDER_ID, message = Constants.ORDER_ID_VALIDATION_EXCEPTION)
    private Long orderId;

}
