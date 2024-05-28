package it.academy.cursebackspring.services.impl;


import it.academy.cursebackspring.dto.request.*;
import it.academy.cursebackspring.dto.response.OrderDTO;
import it.academy.cursebackspring.dto.response.OrderItemsDTO;
import it.academy.cursebackspring.dto.response.OrdersDTO;
import it.academy.cursebackspring.enums.OrderStatus;
import it.academy.cursebackspring.exceptions.OrderNotFoundException;
import it.academy.cursebackspring.exceptions.ProductNotFoundException;
import it.academy.cursebackspring.exceptions.UserNotFoundException;
import it.academy.cursebackspring.mappers.OrderItemMapper;
import it.academy.cursebackspring.mappers.OrderMapper;
import it.academy.cursebackspring.models.Order;
import it.academy.cursebackspring.models.OrderItem;
import it.academy.cursebackspring.models.Product;
import it.academy.cursebackspring.models.User;
import it.academy.cursebackspring.models.embedded.OrderItemPK;
import it.academy.cursebackspring.repositories.*;
import it.academy.cursebackspring.services.OrderService;
import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
@Validated
public class OrderServiceImpl implements OrderService {

    private final UserRepos userRepos;
    private final OrderRepos orderRepos;
    private final OrderItemRepos orderItemRepos;
    private final ProductRepos productRepos;
    private final CartItemRepos cartItemRepos;

    /**
     * Create the order and save order items, if one of the products (which used like order item) is not present,
     * a <code>ProductNotFoundException</code> is thrown
     *
     * @param createOrderDTO - important data for executing this method
     */
    public void createOrder(@Valid CreateOrderDTO createOrderDTO) {
        User user = userRepos.findById(createOrderDTO.getUserId()).orElseThrow(UserNotFoundException::new);
        Order order = OrderMapper.INSTANCE.toNewEntity(createOrderDTO, user);
        orderRepos.save(order);
        List<OrderItem> orderItemList = createOrderDTO.getOrderItems().stream()
                .map(orderItemDTO -> {
                    Product product = productRepos.findById(orderItemDTO.getProductId())
                            .orElseThrow(ProductNotFoundException::new);
                    return OrderItemMapper.INSTANCE.toEntity(orderItemDTO, order, product);
                })
                .toList();
        cartItemRepos.deleteAllByCartItemPK_UserId(user.getId());
        orderItemRepos.saveAll(orderItemList);
    }

    /**
     * This method is used for change status of order. If order is not found, a <code>OrderNotFoundException</code> is thrown
     *
     * @param dto contains id of order and order status
     */
    public void changeOrderStatus(@Valid UpdateOrderStatusDTO dto) {
        OrderStatus status = dto.getOrderStatus();
        Order order = orderRepos.findById(dto.getOrderId())
                .orElseThrow(OrderNotFoundException::new);
        order.setOrderStatus(status);
        orderRepos.save(order);
    }

    /**
     * Delete the order by order id, if not present, a <code>OrderNotFoundException</code> is thrown
     *
     * @param orderId id of order
     */
    public void deleteOrder(@Valid @Min(value = 1, message = Constants.ORDER_ID_VALIDATION_EXCEPTION) Long orderId) {
        Optional.of(orderRepos.existsById(orderId))
                .filter(p -> p)
                .orElseThrow(OrderNotFoundException::new);
        orderRepos.deleteByOrderId(orderId);
    }

    /**
     * Get a list of orders with total count of orders in database
     *
     * @param dto contains the page num and the count of elements per page
     * @return dto with list of the orders and total count of orders
     */
    @Transactional(readOnly = true)
    public OrdersDTO getListOfOrders(@Valid RequestDataDetailsDTO dto) {
        PageRequest pageRequest = PageRequest.of(dto.getPageNum(), dto.getCountPerPage());
        Page<Order> orders = orderRepos.findAll(pageRequest);
        List<OrderDTO> orderDTOList = orders.stream()
                .map(e -> OrderMapper.INSTANCE.toOrderDTO(e, orderItemRepos.countOrderItemsByOrderItemPK_OrderId(e.getId())))
                .toList();
        return new OrdersDTO(orderDTOList, orders.getTotalElements());
    }

    /**
     * Get a list of user orders with total count of user orders in database
     *
     * @param dto contains the page num and the count of elements per page and user id
     * @return dto with list of the orders and total count of orders
     */
    @Transactional(readOnly = true)
    public OrdersDTO getListOfUserOrders(@Valid GetUserOrderPageDTO dto) {
        Optional.of(userRepos.existsById(dto.getUserId()))
                .filter(p -> p)
                .orElseThrow(UserNotFoundException::new);
        Page<Order> orders = orderRepos.getOrdersByUserId(dto.getUserId(), PageRequest.of(dto.getPageNum(), dto.getCountPerPage()));
        List<OrderDTO> orderDTOList = orders.stream()
                .map(e -> OrderMapper.INSTANCE.toOrderDTO(e, orderItemRepos.countOrderItemsByOrderItemPK_OrderId(e.getId())))
                .toList();
        return new OrdersDTO(orderDTOList, orders.getTotalElements());
    }

    /**
     * Get a list of order items with total count of order items. If order is not found,
     * a <code>OrderNotFoundException</code> is thrown.
     *
     * @param dto important data for method execution
     * @return dto with the list of order items with total count of
     */
    @Transactional(readOnly = true)
    public OrderItemsDTO getOrderItems(@Valid GetOrderItemsDTO dto) {
        Optional.of(orderRepos.existsById(dto.getOrderId()))
                .filter(p -> p)
                .orElseThrow(OrderNotFoundException::new);
        Page<OrderItem> orderItems = orderItemRepos.getOrderItemsPageByOrderItemPK_OrderId(dto.getOrderId(),
                PageRequest.of(dto.getPageNum(), dto.getCountPerPage()));
        return OrderItemMapper.INSTANCE.toOrderItemsDTO(orderItems.stream().toList(), orderItems.getTotalElements());
    }

    /**
     * Delete the order item from order by product id and order id.
     * If order not found -> <code>OrderNotFoundException</code>.
     * If product not found -> <code>ProductNotFoundException</code>.
     *
     * @param dto contains order id and product id
     */
    public void deleteOrderItem(@Valid DeleteOrderItemDTO dto) {
        Order order = orderRepos.findById(dto.getOrderId())
                .orElseThrow(OrderNotFoundException::new);
        Product product = productRepos.findById(dto.getProductId())
                .orElseThrow(ProductNotFoundException::new);
        orderItemRepos.deleteById(new OrderItemPK(order, product));
    }

    /**
     * Add or update order items in order.
     * If order not found -> <code>OrderNotFoundException</code>.
     * If product not found -> <code>ProductNotFoundException</code>.
     *
     * @param orderItemDTO important data for method execution
     */
    public void addOrUpdateOrderItemToOrder(@Valid OrderItemDTO orderItemDTO) {
        Order order = orderRepos.findById(orderItemDTO.getOrderId())
                .orElseThrow(OrderNotFoundException::new);
        Product product = productRepos.findById(orderItemDTO.getProductId())
                .orElseThrow(ProductNotFoundException::new);
        OrderItem orderItem = OrderItemMapper.INSTANCE.toEntity(orderItemDTO, order, product);
        orderItemRepos.save(orderItem);
    }
}
