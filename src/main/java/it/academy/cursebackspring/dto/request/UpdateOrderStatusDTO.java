package it.academy.cursebackspring.dto.request;

import it.academy.cursebackspring.enums.OrderStatus;
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
public class UpdateOrderStatusDTO {

    @NotNull(message = Constants.ORDER_ID_CANNOT_BE_NULL_VALIDATION_EXCEPTION)
    @Min(value = Constants.MIN_ORDER_ID, message = Constants.ORDER_ID_VALIDATION_EXCEPTION)
    private Long orderId;

    @NotNull(message = Constants.ORDER_STATUS_CANNOT_BE_NULL_VALIDATION_EXCEPTION)
    private OrderStatus orderStatus;

}
