package it.academy.cursebackspring.dto.response;

import it.academy.cursebackspring.enums.OrderStatus;
import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {

    @NotNull
    @Min(value = 1, message = Constants.ORDER_ID_VALIDATION_EXCEPTION)
    private Long id;

    @NotNull
    private Date date;

    @NotNull
    private OrderStatus orderStatus;

    @NotNull
    @Min(value = 0)
    private Long countOfItems;

}
