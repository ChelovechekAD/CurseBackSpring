package it.academy.cursebackspring.services;

import it.academy.cursebackspring.dto.request.*;
import it.academy.cursebackspring.dto.response.OrderItemsDTO;
import it.academy.cursebackspring.dto.response.OrdersDTO;
import it.academy.cursebackspring.enums.OrderStatus;
import it.academy.cursebackspring.exceptions.OrderNotFoundException;
import it.academy.cursebackspring.exceptions.ProductNotFoundException;
import it.academy.cursebackspring.exceptions.UserNotFoundException;
import it.academy.cursebackspring.models.Order;
import it.academy.cursebackspring.models.OrderItem;
import it.academy.cursebackspring.models.Product;
import it.academy.cursebackspring.models.User;
import it.academy.cursebackspring.repositories.*;
import it.academy.cursebackspring.services.impl.OrderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceTests {

    @Mock
    UserRepos userRepos;

    @Mock
    OrderRepos orderRepos;

    @Mock
    OrderItemRepos orderItemRepos;

    @Mock
    ProductRepos productRepos;

    @Mock
    CartItemRepos cartItemRepos;

    @InjectMocks
    OrderServiceImpl orderService;

    private static Stream<Arguments> provideCreateOrderTestData() {
        return Stream.of(
                Arguments.of(new CreateOrderDTO(
                                1L, List.of(new OrderItemDTO(1L, null, 9L, 2.2))),
                        true, null, null),
                Arguments.of(new CreateOrderDTO(
                                1L, List.of(new OrderItemDTO(2L, null, 9L, 2.2))),
                        false, ProductNotFoundException.class, "Product not found."),
                Arguments.of(new CreateOrderDTO(
                                2L, List.of(new OrderItemDTO(1L, null, 9L, 2.2))),
                        false, UserNotFoundException.class, "User not found!")

        );
    }

    private static Stream<Arguments> provideChangeOrderStatusData() {
        return Stream.of(
                Arguments.of(new UpdateOrderStatusDTO(1L, OrderStatus.CANCELLED), true, null),
                Arguments.of(new UpdateOrderStatusDTO(2L, OrderStatus.CANCELLED), false, "Order not found!")

        );
    }

    private static Stream<Arguments> provideDeleteOrderData() {
        return Stream.of(
                Arguments.of(1L, true, null),
                Arguments.of(2L, false, "Order not found!")

        );
    }

    private static Stream<Arguments> provideGetListOfUserOrdersData() {
        return Stream.of(
                Arguments.of(new GetUserOrderPageDTO(0, 10, 1L), true, null),
                Arguments.of(new GetUserOrderPageDTO(0, 10, 2L), false, "User not found!")

        );
    }

    private static Stream<Arguments> provideGetOrderItemsData() {
        return Stream.of(
                Arguments.of(new GetOrderItemsDTO(0, 10, 1L), true, null),
                Arguments.of(new GetOrderItemsDTO(0, 10, 2L), false, "Order not found!")

        );
    }

    private static Stream<Arguments> provideDeleteOrderItemData() {
        return Stream.of(
                Arguments.of(new DeleteOrderItemDTO(1L, 1L), true, null, null),
                Arguments.of(new DeleteOrderItemDTO(2L, 1L), false,
                        ProductNotFoundException.class, "Product not found."),
                Arguments.of(new DeleteOrderItemDTO(1L, 2L), false,
                        OrderNotFoundException.class, "Order not found!")

        );
    }

    private static Stream<Arguments> provideAddOrUpdateOrderItemToOrderData() {
        return Stream.of(
                Arguments.of(new OrderItemDTO(1L, 1L, 1L, 2.2), true, null, null),
                Arguments.of(new OrderItemDTO(2L, 1L, 1L, 2.2), false,
                        ProductNotFoundException.class, "Product not found."),
                Arguments.of(new OrderItemDTO(1L, 2L, 1L, 2.2), false,
                        OrderNotFoundException.class, "Order not found!")

        );
    }

    @ParameterizedTest
    @MethodSource("provideCreateOrderTestData")
    public <T extends Exception> void createOrderTest(CreateOrderDTO dto, boolean isValid, Class<T> exClass, String message) {
        when(userRepos.findById(dto.getUserId())).then(invocation ->
                (invocation.getArgument(0, Long.class) == 1L) ? Optional.of(new User()) : Optional.empty());
        when(orderRepos.save(any(Order.class))).thenReturn(new Order());
        when(productRepos.findById(any(Long.class))).then(invocation ->
                (invocation.getArgument(0, Long.class) == 1L) ? Optional.of(new Product()) : Optional.empty());
        when(orderItemRepos.saveAll(any())).thenReturn(List.of());
        if (isValid) {
            orderService.createOrder(dto);
            verify(cartItemRepos, times(1)).deleteAllByCartItemPK_UserId(dto.getUserId());
            verify(orderItemRepos, times(1)).saveAll(any());
        } else {
            Exception exception = Assertions.assertThrows(exClass, () -> orderService.createOrder(dto));
            Assertions.assertEquals(message, exception.getMessage());
        }
    }

    @ParameterizedTest
    @MethodSource("provideChangeOrderStatusData")
    public void changeOrderStatusTest(UpdateOrderStatusDTO dto, boolean isValid, String message) {
        Order expectedOrder = new Order(1L, new User(),
                new Date(System.currentTimeMillis()), OrderStatus.SHIPPED);
        when(orderRepos.findById(dto.getOrderId())).then(invocation ->
                (invocation.getArgument(0, Long.class) == 1L) ? Optional.of(expectedOrder) : Optional.empty());
        if (isValid) {
            ArgumentCaptor<Order> orderArgumentCaptor = ArgumentCaptor.forClass(Order.class);
            orderService.changeOrderStatus(dto);
            verify(orderRepos).save(orderArgumentCaptor.capture());
            Order actualOrder = orderArgumentCaptor.getValue();
            Assertions.assertEquals(List.of(expectedOrder, dto.getOrderStatus()),
                    List.of(actualOrder, actualOrder.getOrderStatus()));
        } else {
            Exception exception = Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.changeOrderStatus(dto));
            Assertions.assertEquals(message, exception.getMessage());
        }
    }

    @ParameterizedTest
    @MethodSource("provideDeleteOrderData")
    public void deleteOrderTest(Long orderId, boolean isValid, String message) {
        when(orderRepos.existsById(orderId)).then(invocation -> invocation.getArgument(0, Long.class) == 1L);
        if (isValid) {
            orderService.deleteOrder(orderId);
            verify(orderRepos, times(1)).deleteByOrderId(orderId);
        } else {
            Exception exception = Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(orderId));
            Assertions.assertEquals(message, exception.getMessage());
        }
    }

    @Test
    public void getListOfOrdersTest() {
        RequestDataDetailsDTO dto = new RequestDataDetailsDTO(10, 0);
        when(orderRepos.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(new Order(), new Order())));
        OrdersDTO actualOut = orderService.getListOfOrders(dto);
        Assertions.assertEquals(List.of(2L, 2),
                List.of(actualOut.getCount(), actualOut.getOrderList().size()));
    }

    @ParameterizedTest
    @MethodSource("provideGetListOfUserOrdersData")
    public void getListOfUserOrdersTest(GetUserOrderPageDTO dto, boolean isValid, String message) {
        when(userRepos.existsById(dto.getUserId())).then(invocation -> invocation.getArgument(0, Long.class) == 1L);
        when(orderRepos.getOrdersByUserId(any(Long.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(new Order(), new Order())));
        if (isValid) {
            OrdersDTO actualOut = orderService.getListOfUserOrders(dto);
            Assertions.assertEquals(List.of(2L, 2),
                    List.of(actualOut.getCount(), actualOut.getOrderList().size()));
        } else {
            Exception exception = Assertions.assertThrows(UserNotFoundException.class, () -> orderService.getListOfUserOrders(dto));
            Assertions.assertEquals(message, exception.getMessage());
        }
    }

    @ParameterizedTest
    @MethodSource("provideGetOrderItemsData")
    public void getOrdersItemsTest(GetOrderItemsDTO dto, boolean isValid, String message) {
        when(orderRepos.existsById(dto.getOrderId())).then(invocation -> invocation.getArgument(0, Long.class) == 1L);
        when(orderItemRepos.getOrderItemsPageByOrderItemPK_OrderId(any(Long.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(new OrderItem(), new OrderItem())));
        if (isValid) {
            OrderItemsDTO actualOut = orderService.getOrderItems(dto);
            Assertions.assertEquals(List.of(2L, 2),
                    List.of(actualOut.getTotalCountOfItems(), actualOut.getOrderItemProductDTOList().size()));
        } else {
            Exception exception = Assertions.assertThrows(OrderNotFoundException.class,
                    () -> orderService.getOrderItems(dto));
            Assertions.assertEquals(message, exception.getMessage());
        }
    }

    @ParameterizedTest
    @MethodSource("provideDeleteOrderItemData")
    public <T extends Exception> void deleteOrderItem(DeleteOrderItemDTO dto, boolean isValid, Class<T> exClass, String message) {
        when(productRepos.findById(dto.getProductId())).then(invocation ->
                (invocation.getArgument(0, Long.class) == 1L) ? Optional.of(new Product()) : Optional.empty());
        when(orderRepos.findById(dto.getOrderId())).then(invocation ->
                (invocation.getArgument(0, Long.class) == 1L) ? Optional.of(new Order()) : Optional.empty());
        if (isValid) {
            orderService.deleteOrderItem(dto);
            verify(orderItemRepos, times(1)).deleteById(any());
        } else {
            Exception exception = Assertions.assertThrows(exClass, () -> orderService.deleteOrderItem(dto));
            Assertions.assertEquals(message, exception.getMessage());
        }
    }

    @ParameterizedTest
    @MethodSource("provideAddOrUpdateOrderItemToOrderData")
    public <T extends Exception> void addOrUpdateOrderItemToOrder(OrderItemDTO dto, boolean isValid,
                                                                  Class<T> exClass, String message) {
        when(productRepos.findById(dto.getProductId())).then(invocation ->
                (invocation.getArgument(0, Long.class) == 1L) ? Optional.of(new Product()) : Optional.empty());
        when(orderRepos.findById(dto.getOrderId())).then(invocation ->
                (invocation.getArgument(0, Long.class) == 1L) ? Optional.of(new Order()) : Optional.empty());
        if (isValid) {
            ArgumentCaptor<OrderItem> captor = ArgumentCaptor.forClass(OrderItem.class);
            orderService.addOrUpdateOrderItemToOrder(dto);
            verify(orderItemRepos, times(1)).save(captor.capture());
            OrderItem actualOrderItem = captor.getValue();
            Assertions.assertEquals(List.of(1L, 2.2),
                    List.of(actualOrderItem.getCount(), actualOrderItem.getPrice()));
        } else {
            Exception exception = Assertions.assertThrows(exClass, () -> orderService.addOrUpdateOrderItemToOrder(dto));
            Assertions.assertEquals(message, exception.getMessage());
        }
    }

}
