package it.academy.cursebackspring.controllers;

import it.academy.cursebackspring.dto.request.AddOrUpdateItemInCartDTO;
import it.academy.cursebackspring.dto.request.CreateOrderDTO;
import it.academy.cursebackspring.dto.request.DeleteItemFromCartDTO;
import it.academy.cursebackspring.dto.response.CartItemsDTO;
import it.academy.cursebackspring.models.User;
import it.academy.cursebackspring.services.CartService;
import it.academy.cursebackspring.services.OrderService;
import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/cart/")
public class CartController {

    private final CartService cartService;
    private final OrderService orderService;

    @PostMapping({"/add/item", "/update/item"})
    public ResponseEntity<?> addItemToCart(@RequestBody @Valid AddOrUpdateItemInCartDTO dto) {
        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        dto.setUserId(userId);
        cartService.addOrUpdateItemInCart(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add/order")
    public ResponseEntity<?> addNewOrder(@RequestBody @Valid CreateOrderDTO dto) {
        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        dto.setUserId(userId);
        orderService.createOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/delete/item")
    public ResponseEntity<?> deleteItemFromCart(@RequestParam
                                                @Valid
                                                @NotNull(message = Constants.PRODUCT_ID_CANNOT_BE_NULL_VALIDATION_EXCEPTION)
                                                @Min(value = Constants.MIN_PRODUCT_ID,
                                                        message = Constants.PRODUCT_ID_VALIDATION_EXCEPTION) Long productId
    ) {
        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        cartService.deleteItemFromCart(new DeleteItemFromCartDTO(productId, userId));
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<CartItemsDTO> getAllUserCart() {
        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        CartItemsDTO cartItemsDTO = cartService.getAllCartByUserId(userId);
        return ResponseEntity.ok(cartItemsDTO);
    }

}
