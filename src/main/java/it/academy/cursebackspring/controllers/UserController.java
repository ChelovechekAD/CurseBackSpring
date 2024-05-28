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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/{userId}")
public class UserController {

    private final UserService userService;
    private final ReviewService reviewService;
    private final OrderService orderService;

    @GetMapping()
    @PreAuthorize(value = "#userId == authentication.principal.id")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long userId) {
        UserDTO userDTO = userService.getUserById(userId);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/reviews")
    @PreAuthorize(value = "#userId == authentication.principal.id")
    public ResponseEntity<UserReviewsDTO> getUserReviews(@PathVariable Long userId, GetUserReviewsDTO dto) {
        dto.setUserId(userId);
        UserReviewsDTO reviewsDTO = reviewService.getAllUserReviews(dto);
        return ResponseEntity.ok(reviewsDTO);
    }

    @GetMapping("/orders")
    @PreAuthorize(value = "#userId == authentication.principal.id")
    public ResponseEntity<OrdersDTO> getUserOrders(@PathVariable Long userId, GetUserOrderPageDTO dto) {
        dto.setUserId(userId);
        OrdersDTO ordersDTO = orderService.getListOfUserOrders(dto);
        return ResponseEntity.ok(ordersDTO);
    }

    @GetMapping("/orders/{orderId}/items")
    @PreAuthorize(value = "#userId == authentication.principal.id")
    public ResponseEntity<OrderItemsDTO> getUserOrderItems(@PathVariable(name = "userId") Long userId,
                                                           @PathVariable(name = "orderId") Long orderId,
                                                           GetOrderItemsDTO dto) {
        dto.setOrderId(orderId);
        OrderItemsDTO orderItemsDTO = orderService.getOrderItems(dto);
        return ResponseEntity.ok(orderItemsDTO);
    }

    @PutMapping("/update")
    @PreAuthorize(value = "#userId == authentication.principal.id")
    public ResponseEntity<?> updateUserInfo(@PathVariable(name = "userId") Long userId, @RequestBody UpdateUserDTO dto) {
        dto.setId(userId);
        userService.updateUser(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    @PreAuthorize("#userId == authentication.principal.id")
    public ResponseEntity<?> deleteUser(@PathVariable(name = "userId") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

}
