package it.academy.cursebackspring.services;

import it.academy.cursebackspring.dto.request.*;
import it.academy.cursebackspring.dto.response.OrderItemsDTO;
import it.academy.cursebackspring.dto.response.OrdersDTO;
import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

public interface OrderService {
    void createOrder(CreateOrderDTO createOrderDTO);

    void changeOrderStatus(UpdateOrderStatusDTO dto);

    void deleteOrder(Long orderId);

    OrdersDTO getListOfOrders(RequestDataDetailsDTO dto);

    OrderItemsDTO getOrderItems(GetOrderItemsDTO dto);

    void deleteOrderItem(DeleteOrderItemDTO dto);

    void addOrUpdateOrderItemToOrder(OrderItemDTO orderItemDTO);

    OrdersDTO getListOfUserOrders(GetUserOrderPageDTO dto);

}
