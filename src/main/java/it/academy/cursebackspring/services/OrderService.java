package it.academy.cursebackspring.services;

import it.academy.cursebackspring.dto.request.*;
import it.academy.cursebackspring.dto.response.OrderItemsDTO;
import it.academy.cursebackspring.dto.response.OrdersDTO;
import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

public interface OrderService {
    void createOrder(@Valid CreateOrderDTO createOrderDTO);

    void changeOrderStatus(@Valid UpdateOrderStatusDTO dto);

    void deleteOrder(@Valid @Min(value = 1, message = Constants.ORDER_ID_VALIDATION_EXCEPTION) Long orderId);

    OrdersDTO getListOfOrders(@Valid RequestDataDetailsDTO dto);

    OrderItemsDTO getOrderItems(@Valid GetOrderItemsDTO dto);

    void deleteOrderItem(@Valid DeleteOrderItemDTO dto);

    void addOrUpdateOrderItemToOrder(@Valid OrderItemDTO orderItemDTO);

    OrdersDTO getListOfUserOrders(@Valid GetUserOrderPageDTO dto);

}
