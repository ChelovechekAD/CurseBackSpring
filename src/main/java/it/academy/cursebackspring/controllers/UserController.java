package it.academy.cursebackspring.controllers;

import it.academy.cursebackspring.dto.request.GetOrderItemsDTO;
import it.academy.cursebackspring.dto.request.GetUserOrderPageDTO;
import it.academy.cursebackspring.dto.request.GetUserReviewsDTO;
import it.academy.cursebackspring.dto.request.UpdateUserDTO;
import it.academy.cursebackspring.dto.response.OrderItemsDTO;
import it.academy.cursebackspring.dto.response.OrdersDTO;
import it.academy.cursebackspring.dto.response.UserDTO;
import it.academy.cursebackspring.dto.response.UserReviewsDTO;
import it.academy.cursebackspring.services.OrderService;
import it.academy.cursebackspring.services.ReviewService;
import it.academy.cursebackspring.services.UserService;
import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/user/{userId}")
public class UserController {

    private final UserService userService;
    private final ReviewService reviewService;
    private final OrderService orderService;

    @GetMapping()
    @PreAuthorize(value = "#userId == authentication.principal.id")
    public ResponseEntity<UserDTO> getUser(@PathVariable
                                           @Valid
                                           @NotNull
                                           @Min(value = 1, message = Constants.USER_ID_VALIDATION_EXCEPTION)
                                           Long userId) {
        UserDTO userDTO = userService.getUserById(userId);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/reviews")
    @PreAuthorize(value = "#userId == authentication.principal.id")
    public ResponseEntity<UserReviewsDTO> getUserReviews(@PathVariable
                                                         @Valid
                                                         @NotNull
                                                         @Min(value = 1, message = Constants.USER_ID_VALIDATION_EXCEPTION) Long userId,
                                                         @Valid GetUserReviewsDTO dto) {
        dto.setUserId(userId);
        UserReviewsDTO reviewsDTO = reviewService.getAllUserReviews(dto);
        return ResponseEntity.ok(reviewsDTO);
    }

    @GetMapping("/orders")
    @PreAuthorize(value = "#userId == authentication.principal.id")
    public ResponseEntity<OrdersDTO> getUserOrders(@PathVariable
                                                   @Valid
                                                   @NotNull
                                                   @Min(value = 1, message = Constants.USER_ID_VALIDATION_EXCEPTION) Long userId,
                                                   GetUserOrderPageDTO dto) {
        dto.setUserId(userId);
        OrdersDTO ordersDTO = orderService.getListOfUserOrders(dto);
        return ResponseEntity.ok(ordersDTO);
    }

    @GetMapping("/orders/{orderId}/items")
    @PreAuthorize(value = "#userId == authentication.principal.id")
    public ResponseEntity<OrderItemsDTO> getUserOrderItems(@PathVariable(name = "userId")
                                                           @Valid
                                                           @NotNull
                                                           @Min(value = 1, message = Constants.USER_ID_VALIDATION_EXCEPTION) Long userId,
                                                           @PathVariable(name = "orderId")
                                                           @Valid
                                                           @NotNull
                                                           @Min(value = 1, message = Constants.ORDER_ID_VALIDATION_EXCEPTION) Long orderId,
                                                           @Valid GetOrderItemsDTO dto) {
        dto.setOrderId(orderId);
        OrderItemsDTO orderItemsDTO = orderService.getOrderItems(dto);
        return ResponseEntity.ok(orderItemsDTO);
    }

    @PutMapping("/update")
    @PreAuthorize(value = "#userId == authentication.principal.id")
    public ResponseEntity<?> updateUserInfo(@PathVariable
                                            @Valid
                                            @NotNull
                                            @Min(value = 1, message = Constants.USER_ID_VALIDATION_EXCEPTION) Long userId,
                                            @RequestBody @Valid UpdateUserDTO dto) {
        dto.setId(userId);
        userService.updateUser(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<?> deleteUser(@PathVariable(name = "userId")
                                        @Valid
                                        @NotNull
                                        @Min(value = 1, message = Constants.USER_ID_VALIDATION_EXCEPTION) Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

}
