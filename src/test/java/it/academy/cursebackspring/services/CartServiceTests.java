package it.academy.cursebackspring.services;

import it.academy.cursebackspring.dto.request.AddOrUpdateItemInCartDTO;
import it.academy.cursebackspring.dto.request.DeleteItemFromCartDTO;
import it.academy.cursebackspring.dto.response.CartItemDTO;
import it.academy.cursebackspring.dto.response.CartItemsDTO;
import it.academy.cursebackspring.exceptions.ProductNotFoundException;
import it.academy.cursebackspring.exceptions.UserNotFoundException;
import it.academy.cursebackspring.models.CartItem;
import it.academy.cursebackspring.models.Product;
import it.academy.cursebackspring.models.User;
import it.academy.cursebackspring.models.embedded.CartItemPK;
import it.academy.cursebackspring.repositories.CartItemRepos;
import it.academy.cursebackspring.repositories.ProductRepos;
import it.academy.cursebackspring.repositories.UserRepos;
import it.academy.cursebackspring.services.impl.CartServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@SpringBootTest
public class CartServiceTests {

    @Mock
    CartItemRepos cartItemRepos;

    @Mock
    ProductRepos productRepos;

    @Mock
    UserRepos userRepos;

    @InjectMocks
    CartServiceImpl cartService;

    @ParameterizedTest
    @MethodSource("provideAddOrUpdateInCartData")
    public <T extends Exception> void addOrUpdateItemInCartTest(AddOrUpdateItemInCartDTO dto, boolean isValid,
                                                                Class<T> exClass, String message) {
        when(productRepos.findById(any(Long.class))).then(invocation ->
                (invocation.getArgument(0, Long.class) == 1L) ? Optional.of(new Product()) : Optional.empty());
        when(userRepos.findById(any(Long.class))).then(invocation ->
                (invocation.getArgument(0, Long.class) == 1L) ? Optional.of(new User()) : Optional.empty());
        if (isValid) {
            ArgumentCaptor<CartItem> captor = ArgumentCaptor.forClass(CartItem.class);
            cartService.addOrUpdateItemInCart(dto);
            verify(cartItemRepos).save(captor.capture());
            Assertions.assertEquals(dto.getQuantity(), captor.getValue().getQuantity());
        } else {
            Exception exception = Assertions.assertThrows(exClass, () -> cartService.addOrUpdateItemInCart(dto));
            Assertions.assertEquals(message, exception.getMessage());
        }

    }

    @ParameterizedTest
    @MethodSource("provideDeleteItemFromCartData")
    public <T extends Exception> void deleteItemFromCart(DeleteItemFromCartDTO dto, boolean isValid,
                                       Class<T> exClass, String message) {
        when(productRepos.findById(any(Long.class))).then(invocation ->
                (invocation.getArgument(0, Long.class) == 1L) ? Optional.of(new Product()) : Optional.empty());
        when(userRepos.findById(any(Long.class))).then(invocation ->
                (invocation.getArgument(0, Long.class) == 1L) ? Optional.of(new User()) : Optional.empty());
        if (isValid) {
            cartService.deleteItemFromCart(dto);
            verify(cartItemRepos).deleteById(any(CartItemPK.class));
        } else {
            Exception exception = Assertions.assertThrows(exClass, () -> cartService.deleteItemFromCart(dto));
            Assertions.assertEquals(message, exception.getMessage());
        }
    }

    @ParameterizedTest
    @MethodSource("provideGetAllCartByUserIdData")
    public void getAllCartByUserIdTest(Long userId, boolean isValid, String message){
        when(userRepos.existsById(any(Long.class))).then(invocation ->
                invocation.getArgument(0, Long.class) == 1L);
        when(cartItemRepos.findCartItemsByCartItemPK_UserId(any(Long.class))).then(invocation ->
                List.of(new CartItem(), new CartItem()));
        if (isValid) {
            CartItemsDTO actualDTO = cartService.getAllCartByUserId(userId);
            List<CartItemDTO> actualListItemDTO = actualDTO.getCartItemDTOList();
            Assertions.assertEquals(2, actualListItemDTO.size());
        } else {
            Exception exception = Assertions.assertThrows(UserNotFoundException.class,
                    () -> cartService.getAllCartByUserId(userId));
            Assertions.assertEquals(message, exception.getMessage());
        }
    }

    private static Stream<Arguments> provideAddOrUpdateInCartData() {
        return Stream.of(
                Arguments.of(new AddOrUpdateItemInCartDTO(9, 1L, 1L), true, null, null),
                Arguments.of(new AddOrUpdateItemInCartDTO(9, 2L, 1L), false,
                        ProductNotFoundException.class, "Product not found."),
                Arguments.of(new AddOrUpdateItemInCartDTO(9, 1L, 2L), false,
                        UserNotFoundException.class, "User not found!")
        );
    }
    private static Stream<Arguments> provideGetAllCartByUserIdData() {
        return Stream.of(
                Arguments.of(1L, true, null),
                Arguments.of(2L, false, "User not found!")
        );
    }
    private static Stream<Arguments> provideDeleteItemFromCartData() {
        return Stream.of(
                Arguments.of(new DeleteItemFromCartDTO( 1L, 1L), true, null, null),
                Arguments.of(new DeleteItemFromCartDTO(2L, 1L), false,
                        ProductNotFoundException.class, "Product not found."),
                Arguments.of(new DeleteItemFromCartDTO(1L, 2L), false,
                        UserNotFoundException.class, "User not found!")
        );
    }
}
