package it.academy.cursebackspring.services;

import it.academy.cursebackspring.dto.request.AddOrUpdateItemInCartDTO;
import it.academy.cursebackspring.dto.request.DeleteItemFromCartDTO;
import it.academy.cursebackspring.dto.response.CartItemsDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

public interface CartService {

    void addOrUpdateItemInCart(@Valid AddOrUpdateItemInCartDTO dto);

    void deleteItemFromCart(@Valid DeleteItemFromCartDTO dto);

    CartItemsDTO getAllCartByUserId(@Valid @Min(1) Long userId);
}
