package it.academy.cursebackspring.controllers;

import it.academy.cursebackspring.dto.request.AddOrUpdateItemInCartDTO;
import it.academy.cursebackspring.dto.request.CreateOrderDTO;
import it.academy.cursebackspring.dto.request.DeleteItemFromCartDTO;
import it.academy.cursebackspring.dto.response.CartItemsDTO;
import it.academy.cursebackspring.services.CartService;
import it.academy.cursebackspring.services.OrderService;
import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize(value = "#dto.userId == authentication.principal.id")
    public ResponseEntity<?> addItemToCart(@RequestBody @Valid AddOrUpdateItemInCartDTO dto) {
        cartService.addOrUpdateItemInCart(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add/order")
    @PreAuthorize(value = "#dto.userId == authentication.principal.id")
    public ResponseEntity<?> addNewOrder(@RequestBody @Valid CreateOrderDTO dto) {
        orderService.createOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/delete/item")
    @PreAuthorize(value = "#userId == authentication.principal.id")
    public ResponseEntity<?> deleteItemFromCart(@RequestParam(name = Constants.PRODUCT_ID_PARAM_KEY)
                                                @Valid
                                                @NotNull(message = Constants.PRODUCT_ID_CANNOT_BE_NULL_VALIDATION_EXCEPTION)
                                                @Min(value = Constants.MIN_PRODUCT_ID,
                                                        message = Constants.PRODUCT_ID_VALIDATION_EXCEPTION) Long productId,
                                                @RequestParam(name = Constants.USER_ID_PARAM_KEY)
                                                @Valid
                                                @NotNull(message = Constants.USER_ID_CANNOT_BE_NULL_VALIDATION_EXCEPTION)
                                                @Min(value = Constants.MIN_USER_ID,
                                                        message = Constants.USER_ID_VALIDATION_EXCEPTION) Long userId) {
        cartService.deleteItemFromCart(new DeleteItemFromCartDTO(productId, userId));
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @PreAuthorize(value = "#id == authentication.principal.id")
    public ResponseEntity<?> getAllUserCart(@RequestParam(Constants.USER_ID_PARAM_KEY)
                                            @Valid
                                            @NotNull
                                            @Min(value = Constants.MIN_USER_ID,
                                                    message = Constants.USER_ID_VALIDATION_EXCEPTION) Long id) {
        CartItemsDTO cartItemsDTO = cartService.getAllCartByUserId(id);
        return ResponseEntity.ok(cartItemsDTO);
    }

}
