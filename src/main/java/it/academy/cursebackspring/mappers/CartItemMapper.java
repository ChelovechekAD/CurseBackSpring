package it.academy.cursebackspring.mappers;

import it.academy.cursebackspring.dto.response.CartItemDTO;
import it.academy.cursebackspring.dto.response.CartItemsDTO;
import it.academy.cursebackspring.models.CartItem;
import it.academy.cursebackspring.models.embedded.CartItemPK;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CartItemMapper {

    CartItemMapper INSTANCE = Mappers.getMapper(CartItemMapper.class);

    CartItem createEntity(CartItemPK cartItemPK, Integer quantity);

    @Mappings({
            @Mapping(source = "cartItem.cartItemPK.product.id", target = "productId"),
            @Mapping(source = "cartItem.cartItemPK.product.name", target = "name"),
            @Mapping(source = "cartItem.cartItemPK.product.imageLink", target = "imageLink"),
            @Mapping(source = "cartItem.cartItemPK.product.price", target = "price"),
            @Mapping(source = "cartItem.cartItemPK.product.rating", target = "rating"),
            @Mapping(source = "cartItem.quantity", target = "quantity")
    })
    CartItemDTO toDTOFromEntity(CartItem cartItem);

    default CartItemsDTO toDTOFromEntityList(List<CartItem> cartItems) {
        List<CartItemDTO> cartItemDTOList = cartItems.stream()
                .map(CartItemMapper.INSTANCE::toDTOFromEntity)
                .toList();
        return new CartItemsDTO(cartItemDTOList);
    }

}
