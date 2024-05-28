package it.academy.cursebackspring.services.impl;

import it.academy.cursebackspring.dto.request.AddOrUpdateItemInCartDTO;
import it.academy.cursebackspring.dto.request.DeleteItemFromCartDTO;
import it.academy.cursebackspring.dto.response.CartItemsDTO;
import it.academy.cursebackspring.exceptions.ProductNotFoundException;
import it.academy.cursebackspring.exceptions.UserNotFoundException;
import it.academy.cursebackspring.mappers.CartItemMapper;
import it.academy.cursebackspring.models.CartItem;
import it.academy.cursebackspring.models.Product;
import it.academy.cursebackspring.models.User;
import it.academy.cursebackspring.models.embedded.CartItemPK;
import it.academy.cursebackspring.repositories.CartItemRepos;
import it.academy.cursebackspring.repositories.ProductRepos;
import it.academy.cursebackspring.repositories.UserRepos;
import it.academy.cursebackspring.services.CartService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class CartServiceImpl implements CartService {

    private final CartItemRepos cartItemRepos;
    private final ProductRepos productRepos;
    private final UserRepos userRepos;

    /**
     * Allows the user to add or update an item in their own cart.
     * If item is present then update, else add.
     *
     * @param dto important data for executing this method
     */
    public void addOrUpdateItemInCart(@Valid AddOrUpdateItemInCartDTO dto) {
        Product product = productRepos.findById(dto.getProductId())
                .orElseThrow(ProductNotFoundException::new);
        User user = userRepos.findById(dto.getUserId())
                .orElseThrow(UserNotFoundException::new);
        CartItemPK cartItemPK = new CartItemPK(user, product);
        CartItem cartItem = CartItemMapper.INSTANCE.createEntity(cartItemPK, dto.getQuantity());
        cartItemRepos.save(cartItem);
    }

    /**
     * Allows the user to delete an item from their own cart
     *
     * @param dto important data for executing this method
     */
    public void deleteItemFromCart(@Valid DeleteItemFromCartDTO dto) {
        Product product = productRepos.findById(dto.getProductId())
                .orElseThrow(ProductNotFoundException::new);
        User user = userRepos.findById(dto.getUserId())
                .orElseThrow(UserNotFoundException::new);
        CartItemPK cartItemPK = new CartItemPK(user, product);
        cartItemRepos.deleteById(cartItemPK);
    }

    /**
     * Return all items from user cart by user id
     *
     * @param userId user identifier
     * @return dto which contains all items from user cart
     */
    @Transactional(readOnly = true)
    public CartItemsDTO getAllCartByUserId(@Valid @Min(1) Long userId) {
        Optional.of(userRepos.existsById(userId))
                .filter(p -> p)
                .orElseThrow(UserNotFoundException::new);
        List<CartItem> cartItems = cartItemRepos.findCartItemsByCartItemPK_UserId(userId);
        return CartItemMapper.INSTANCE.toDTOFromEntityList(cartItems);
    }
}
