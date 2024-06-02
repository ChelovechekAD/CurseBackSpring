package it.academy.cursebackspring.dto.request;

import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderDTO {

    @NotNull(message = Constants.USER_ID_CANNOT_BE_NULL_VALIDATION_EXCEPTION)
    @Min(value = Constants.MIN_USER_ID, message = Constants.USER_ID_VALIDATION_EXCEPTION)
    private Long userId;

    @NotNull
    private List<OrderItemDTO> orderItems;

}
