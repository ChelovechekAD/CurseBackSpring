package it.academy.cursebackspring.services;

import it.academy.cursebackspring.dto.request.CategoryDTO;
import it.academy.cursebackspring.dto.request.UpdateCategoryDTO;
import it.academy.cursebackspring.dto.response.CategoriesDTO;
import it.academy.cursebackspring.exceptions.CategoryDeleteException;
import it.academy.cursebackspring.exceptions.CategoryExistException;
import it.academy.cursebackspring.exceptions.CategoryNotFoundException;
import it.academy.cursebackspring.exceptions.ProductUsedInOrdersException;
import it.academy.cursebackspring.models.Category;
import it.academy.cursebackspring.repositories.CategoryRepos;
import it.academy.cursebackspring.repositories.OrderItemRepos;
import it.academy.cursebackspring.repositories.ProductRepos;
import it.academy.cursebackspring.services.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
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
public class CategoryServiceTests {

    @Mock
    CategoryRepos categoryRepos;

    @Mock
    ProductRepos productRepos;

    @Mock
    OrderItemRepos orderItemRepos;

    @InjectMocks
    CategoryServiceImpl categoryService;

    @ParameterizedTest
    @MethodSource("provideAddCategoryData")
    public void addCategoryTest(String name, boolean isValid, String message) {
        when(categoryRepos.existsByCategoryName(name)).then(invocation ->
                invocation.getArgument(0) == "Existed category");
        when(categoryRepos.save(any(Category.class))).thenReturn(new Category());
        if (isValid) {
            ArgumentCaptor<Category> categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);
            categoryService.addCategory(name);
            verify(categoryRepos).save(categoryArgumentCaptor.capture());
            Category actualCategoryObject = categoryArgumentCaptor.getValue();
            Assertions.assertEquals(name, actualCategoryObject.getCategoryName());
        } else {
            Exception exception = Assertions.assertThrows(CategoryExistException.class, () -> categoryService.addCategory(name));
            Assertions.assertEquals(message, exception.getMessage());
        }
    }

    @ParameterizedTest
    @MethodSource("provideDeleteCategoryData")
    public <T extends Exception> void deleteCategory(Long categoryId, boolean root, boolean isValid, Class<T> exClass, String message) {
        when(categoryRepos.existsById(categoryId)).then(invocation ->
                invocation.getArgument(0, Long.class) != 2L);
        when(productRepos.existsProductByCategoryId(categoryId)).then(invocation ->
                invocation.getArgument(0, Long.class) == 3L || invocation.getArgument(0, Long.class) == 4L);
        when(orderItemRepos.existsByOrderItemPK_Product_CategoryId(categoryId)).then(invocation ->
                invocation.getArgument(0, Long.class) == 4L);
        if (isValid) {
            categoryService.deleteCategory(categoryId, root);
            if (categoryId != 1L) {
                verify(productRepos, times(1)).deleteAllByCategory_Id(categoryId);
            }
            verify(categoryRepos, times(1)).deleteById(categoryId);
        }else {
            Exception exception = Assertions.assertThrows(exClass, () -> categoryService.deleteCategory(categoryId, root));
            Assertions.assertEquals(message, exception.getMessage());
        }
    }

    @ParameterizedTest
    @MethodSource("provideUpdateCategoryData")
    public void updateCategory(UpdateCategoryDTO dto, boolean isValid, String message){
        Category existedCategory = new Category(1L, "Test");
        when(categoryRepos.findById(dto.getCategoryId())).then(invocation ->
                (invocation.getArgument(0, Long.class) == 1L) ? Optional.of(existedCategory) : Optional.empty());
        if (isValid) {
            ArgumentCaptor<Category> categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);
            categoryService.updateCategory(dto);
            verify(categoryRepos).save(categoryArgumentCaptor.capture());
            Category actualCategory = categoryArgumentCaptor.getValue();
            Assertions.assertEquals(List.of(existedCategory, existedCategory.getCategoryName()),
                    List.of(actualCategory, dto.getCategoryName()));
        }else {
            Exception exception = Assertions.assertThrows(CategoryNotFoundException.class, () -> categoryService.updateCategory(dto));
            Assertions.assertEquals(message, exception.getMessage());
        }
    }

    @Test
    public void getAllCategoriesTest(){
        when(categoryRepos.findAll()).thenReturn(List.of(new Category(1L, "1"),
                new Category(2L, "2")));
        CategoriesDTO actualCategoriesDTO = categoryService.getAllCategories();
        List<CategoryDTO> actualListCategoryDTO = actualCategoriesDTO.getCategoryDTOList();
        Assertions.assertEquals(List.of(2, 1L, "1", 2L, "2"),
                List.of(actualListCategoryDTO.size(),
                        actualListCategoryDTO.get(0).getCategoryId(),
                        actualListCategoryDTO.get(0).getCategoryName(),
                        actualListCategoryDTO.get(1).getCategoryId(),
                        actualListCategoryDTO.get(1).getCategoryName()));
    }


    private static Stream<Arguments> provideAddCategoryData() {
        return Stream.of(
                Arguments.of("New category", true, null),
                Arguments.of("Existed category", false, "Category already exist!")
        );
    }
    private static Stream<Arguments> provideUpdateCategoryData() {
        return Stream.of(
                Arguments.of(new UpdateCategoryDTO(1L, "New name"), true, null),
                Arguments.of(new UpdateCategoryDTO(2L, "Asd"), false, "Requested catalog not exist.")
        );
    }
    private static Stream<Arguments> provideDeleteCategoryData() {
        return Stream.of(
                Arguments.of(1L, false, true, null, null),
                Arguments.of(1L, true, true, null, null),
                Arguments.of(2L, true, false, CategoryNotFoundException.class, "Requested catalog not exist."),
                Arguments.of(2L, false, false, CategoryNotFoundException.class, "Requested catalog not exist."),
                Arguments.of(3L, true, true, null, null),
                Arguments.of(3L, false, false, CategoryDeleteException.class, "Products with this category exist!" +
                        " You need to use root flag to delete all!"),
                Arguments.of(4L, false, false, CategoryDeleteException.class, "Products with this category exist!" +
                        " You need to use root flag to delete all!"),
                Arguments.of(4L, true, false, ProductUsedInOrdersException.class, "This product already used in order(s)")
        );
    }

}
