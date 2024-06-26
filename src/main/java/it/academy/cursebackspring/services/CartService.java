package it.academy.cursebackspring.services;

import it.academy.cursebackspring.dto.request.AddOrUpdateItemInCartDTO;
import it.academy.cursebackspring.dto.request.DeleteItemFromCartDTO;
import it.academy.cursebackspring.dto.response.CartItemsDTO;

public interface CartService {

    void addOrUpdateItemInCart(AddOrUpdateItemInCartDTO dto);

    void deleteItemFromCart(DeleteItemFromCartDTO dto);

    CartItemsDTO getAllCartByUserId(Long userId);
}
