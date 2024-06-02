package it.academy.cursebackspring.dto.request;

import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.constraints.DecimalMin;
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
public class OrderItemDTO {

    @NotNull(message = Constants.PRODUCT_ID_CANNOT_BE_NULL_VALIDATION_EXCEPTION)
    @Min(value = Constants.MIN_PRODUCT_ID, message = Constants.PRODUCT_ID_VALIDATION_EXCEPTION)
    private Long productId;

    private Long orderId;

    @NotNull(message = Constants.QUANTITY_OF_ITEM_CANNOT_BE_NULL_VALIDATION_EXCEPTION)
    @Min(value = Constants.MIN_COUNT_OF_ITEM_IN_ORDER, message = Constants.QUANTITY_CANNOT_BE_LESS_THAN_VALIDATION_EXCEPTION)
    private Long quantity;

    @NotNull(message = Constants.PRICE_CANNOT_BE_NULL_VALIDATION_EXCEPTION)
    @DecimalMin(value = Constants.MIN_PRICE, message = Constants.PRICE_CANNOT_BE_LESS_THAN_VALIDATION_EXCEPTION)
    private Double price;
}
