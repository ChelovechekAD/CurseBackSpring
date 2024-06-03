package it.academy.cursebackspring.services;

import it.academy.cursebackspring.dto.request.CreateProductDTO;
import it.academy.cursebackspring.dto.request.GetProductPageDTO;
import it.academy.cursebackspring.dto.request.UpdateProductDTO;
import it.academy.cursebackspring.dto.response.ProductDTO;
import it.academy.cursebackspring.dto.response.ProductsDTO;
import it.academy.cursebackspring.exceptions.CategoryNotFoundException;
import it.academy.cursebackspring.exceptions.ProductNotFoundException;
import it.academy.cursebackspring.exceptions.ProductUsedInOrdersException;
import it.academy.cursebackspring.models.Category;
import it.academy.cursebackspring.models.Product;
import it.academy.cursebackspring.repositories.CategoryRepos;
import it.academy.cursebackspring.repositories.OrderItemRepos;
import it.academy.cursebackspring.repositories.ProductRepos;
import it.academy.cursebackspring.services.impl.ProductServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@SpringBootTest
public class ProductServiceTests {


    @Mock
    CategoryRepos categoryRepos;

    @Mock
    ProductRepos productRepos;

    @Mock
    OrderItemRepos orderItemRepos;

    @InjectMocks
    ProductServiceImpl productService;

    private static Stream<Arguments> provideGetAllExistProductsData() {
        return Stream.of(
                Arguments.of(new GetProductPageDTO(10, 1, null, "name:P1")),
                Arguments.of(new GetProductPageDTO(10, 1, "category=1", null)),
                Arguments.of(new GetProductPageDTO(10, 1, null, null))

        );
    }

    private static Stream<Arguments> provideDeleteProductData() {
        return Stream.of(
                Arguments.of(1L, true, null),
                Arguments.of(2L, false, "This product already used in order(s)")

        );
    }

    private static Stream<Arguments> provideGetProductByIdData() {
        return Stream.of(
                Arguments.of(1L, true, null),
                Arguments.of(2L, false, "Product not found.")

        );
    }

    private static Stream<Arguments> provideUpdateProductData() {
        return Stream.of(
                Arguments.of(new UpdateProductDTO(1L, 1L, "JohnDoe", "Description",
                        2.2, "image.png"), true, null, null),
                Arguments.of(new UpdateProductDTO(1L, 2L, "JohnDoe", "Description",
                        2.2, "image.png"), false, "Requested catalog not exist.", CategoryNotFoundException.class),
                Arguments.of(new UpdateProductDTO(2L, 1L, "JohnDoe", "Description",
                        2.2, "image.png"), false, "Product not found.", ProductNotFoundException.class)
        );
    }

    private static Stream<Arguments> provideProductData() {
        return Stream.of(
                Arguments.of(new CreateProductDTO(1L, "JohnDoe", "Description",
                        2.2, "image.png"), true, ""),
                Arguments.of(new CreateProductDTO(2L, "JohnDoe", "Description",
                        2.2, "image.png"), false, "Requested catalog not exist.")
        );
    }

    @ParameterizedTest
    @MethodSource("provideProductData")
    public void addProductTest(CreateProductDTO dto, boolean isValid, String errorMessage) {
        when(categoryRepos.findById(any(Long.class))).then(invocation ->
                (dto.getCategoryId() == 1L) ? Optional.of(new Category(dto.getCategoryId(), "Test")) : Optional.empty());
        when(productRepos.save(any(Product.class))).thenReturn(new Product());

        if (isValid) {
            ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
            productService.addProduct(dto);
            verify(productRepos).save(productArgumentCaptor.capture());
            Product actualProduct = productArgumentCaptor.getValue();
            Assertions.assertEquals(
                    List.of(dto.getName(), dto.getDescription(), dto.getImageLink(), dto.getPrice()),
                    List.of(actualProduct.getName(), actualProduct.getDescription(), actualProduct.getImageLink(),
                            actualProduct.getPrice()));
        } else {
            Exception exception = Assertions.assertThrows(CategoryNotFoundException.class, () -> productService.addProduct(dto));
            Assertions.assertEquals(errorMessage, exception.getMessage());
        }

    }

    @ParameterizedTest
    @MethodSource("provideUpdateProductData")
    public <T extends Exception> void updateProductTest(UpdateProductDTO dto, boolean isValid, String errorMessage, Class<T> exClass) {
        when(categoryRepos.existsById(any(Long.class))).then(invocation -> {
            Long id = invocation.getArgument(0);
            return id != 2L;
        });
        when(productRepos.existsById(any(Long.class))).then(invocation -> {
            Long id = invocation.getArgument(0);
            return id != 2L;
        });

        when(productRepos.save(any(Product.class))).thenReturn(new Product());
        if (isValid) {
            ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
            productService.updateProduct(dto);
            verify(productRepos).save(productArgumentCaptor.capture());
            Product actualProduct = productArgumentCaptor.getValue();
            Assertions.assertEquals(
                    List.of(dto.getName(), dto.getDescription(), dto.getImageLink(), dto.getPrice()),
                    List.of(actualProduct.getName(), actualProduct.getDescription(), actualProduct.getImageLink(),
                            actualProduct.getPrice()));
        } else {
            Exception exception = Assertions.assertThrows(exClass, () -> productService.updateProduct(dto));
            Assertions.assertEquals(errorMessage, exception.getMessage());
        }
    }

    @ParameterizedTest
    @MethodSource("provideDeleteProductData")
    public void deleteProductTest(Long id, boolean isValid, String message) {
        when(orderItemRepos.existsByOrderItemPK_ProductId(nullable(Long.class))).then(invocation -> {
            Long idArg = invocation.getArgument(0);
            return idArg == 2L;
        });
        if (isValid) {
            ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
            productService.deleteProduct(id);
            verify(productRepos).deleteById(argumentCaptor.capture());
            Assertions.assertEquals(id, argumentCaptor.getValue());
        } else {
            Exception exception = Assertions.assertThrows(ProductUsedInOrdersException.class, () -> productService.deleteProduct(id));
            Assertions.assertEquals(message, exception.getMessage());
        }
    }

    @ParameterizedTest
    @MethodSource("provideGetProductByIdData")
    public void getProductByIdTest(Long productId, boolean isValid, String message) {
        when(productRepos.findById(any(Long.class))).then(invocation ->
                (productId == 1L) ? Optional.of(new Product()) : Optional.empty());
        if (isValid) {
            ProductDTO productDTO = productService.getProductById(productId);
            Assertions.assertNotNull(productDTO);
        } else {
            Exception exception = Assertions.assertThrows(ProductNotFoundException.class, () -> productService.getProductById(productId));
            Assertions.assertEquals(message, exception.getMessage());
        }
    }

    @ParameterizedTest
    @MethodSource("provideGetAllExistProductsData")
    public void getAllExistProductsTest(GetProductPageDTO dto) {
        when(productRepos.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(
                new PageImpl<>(List.of(new Product())
                ));
        when(productRepos.findAll(any(PageRequest.class))).thenReturn(
                new PageImpl<>(List.of(new Product(), new Product())
                ));
        ProductsDTO productsDTO = productService.getAllExistProducts(dto);
        if (dto.getNameFilter() != null || dto.getCategoryFilter() != null) {
            Assertions.assertEquals(1, productsDTO.getCountOfProducts());
        } else {
            Assertions.assertEquals(2, productsDTO.getCountOfProducts());
        }

    }

}
