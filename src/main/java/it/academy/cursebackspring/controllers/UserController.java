package it.academy.cursebackspring.controllers;

import it.academy.cursebackspring.dto.request.GetOrderItemsDTO;
import it.academy.cursebackspring.dto.request.GetUserOrderPageDTO;
import it.academy.cursebackspring.dto.request.GetUserReviewsDTO;
import it.academy.cursebackspring.dto.request.UpdateUserDTO;
import it.academy.cursebackspring.dto.response.OrderItemsDTO;
import it.academy.cursebackspring.dto.response.OrdersDTO;
import it.academy.cursebackspring.dto.response.UserDTO;
import it.academy.cursebackspring.dto.response.UserReviewsDTO;
import it.academy.cursebackspring.models.User;
import it.academy.cursebackspring.services.OrderService;
import it.academy.cursebackspring.services.ReviewService;
import it.academy.cursebackspring.services.UserService;
import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final ReviewService reviewService;
    private final OrderService orderService;

    @GetMapping()
    public ResponseEntity<UserDTO> getUser() {
        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        UserDTO userDTO = userService.getUserById(userId);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/reviews")
    public ResponseEntity<UserReviewsDTO> getUserReviews(@Valid GetUserReviewsDTO dto) {
        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        dto.setUserId(userId);
        UserReviewsDTO reviewsDTO = reviewService.getAllUserReviews(dto);
        return ResponseEntity.ok(reviewsDTO);
    }

    @GetMapping("/orders")
    public ResponseEntity<OrdersDTO> getUserOrders(@Valid GetUserOrderPageDTO dto) {
        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        dto.setUserId(userId);
        OrdersDTO ordersDTO = orderService.getListOfUserOrders(dto);
        return ResponseEntity.ok(ordersDTO);
    }

    @GetMapping("/orders/{orderId}/items")
    public ResponseEntity<OrderItemsDTO> getUserOrderItems(@PathVariable(name = "orderId")
                                                           @Valid
                                                           @NotNull
                                                           @Min(value = 1, message = Constants.ORDER_ID_VALIDATION_EXCEPTION) Long orderId,
                                                           @Valid GetOrderItemsDTO dto) {
        dto.setOrderId(orderId);
        OrderItemsDTO orderItemsDTO = orderService.getOrderItems(dto);
        return ResponseEntity.ok(orderItemsDTO);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUserInfo(@RequestBody @Valid UpdateUserDTO dto) {
        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        dto.setId(userId);
        userService.updateUser(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser() {
        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

}
