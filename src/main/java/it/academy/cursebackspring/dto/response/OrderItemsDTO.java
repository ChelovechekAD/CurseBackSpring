package it.academy.cursebackspring.dto.response;

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
public class OrderItemsDTO {

    @NotNull
    List<OrderItemProductDTO> orderItemProductDTOList;

    @NotNull
    @Min(value = 0, message = "Count of order items cannot be less than 0.")
    Long totalCountOfItems;
}
