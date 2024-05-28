package it.academy.cursebackspring.controllers;

import it.academy.cursebackspring.dto.request.AddOrUpdateItemInCartDTO;
import it.academy.cursebackspring.dto.request.CreateOrderDTO;
import it.academy.cursebackspring.dto.request.DeleteItemFromCartDTO;
import it.academy.cursebackspring.dto.response.CartItemsDTO;
import it.academy.cursebackspring.services.CartService;
import it.academy.cursebackspring.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cart/")
public class CartController {

    private final CartService cartService;
    private final OrderService orderService;

    @PostMapping({"/add/item", "/update/item"})
    public ResponseEntity<?> addItemToCart(@RequestBody AddOrUpdateItemInCartDTO dto) {
        cartService.addOrUpdateItemInCart(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add/order")
    public ResponseEntity<?> addNewOrder(@RequestBody CreateOrderDTO dto) {
        orderService.createOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/delete/item")
    public ResponseEntity<?> deleteItemFromCart(@RequestBody DeleteItemFromCartDTO dto) {
        cartService.deleteItemFromCart(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> getAllUserCart(@RequestParam("userId") Long id) {
        CartItemsDTO cartItemsDTO = cartService.getAllCartByUserId(id);
        return ResponseEntity.ok(cartItemsDTO);
    }

}
