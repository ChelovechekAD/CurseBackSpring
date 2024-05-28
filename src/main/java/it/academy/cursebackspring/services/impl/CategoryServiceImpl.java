package it.academy.cursebackspring.services.impl;

import it.academy.cursebackspring.dto.request.UpdateCategoryDTO;
import it.academy.cursebackspring.dto.response.CategoriesDTO;
import it.academy.cursebackspring.exceptions.CategoryDeleteException;
import it.academy.cursebackspring.exceptions.CategoryExistException;
import it.academy.cursebackspring.exceptions.CategoryNotFoundException;
import it.academy.cursebackspring.exceptions.ProductUsedInOrdersException;
import it.academy.cursebackspring.mappers.CategoryMapper;
import it.academy.cursebackspring.models.Category;
import it.academy.cursebackspring.repositories.CategoryRepos;
import it.academy.cursebackspring.repositories.OrderItemRepos;
import it.academy.cursebackspring.repositories.ProductRepos;
import it.academy.cursebackspring.services.CategoryService;
import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
@Validated
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepos categoryRepos;
    private final ProductRepos productRepos;
    private final OrderItemRepos orderItemRepos;

    /**
     * Save new category, if category with this name already exist then throw <code>CategoryExistException</code>
     *
     * @param categoryName name of the new category
     */
    public void addCategory(@Valid @NotBlank(message = Constants.CATEGORY_NAME_VALIDATION_EXCEPTION) String categoryName) {
        Optional.of(categoryRepos.existsByCategoryName(categoryName))
                .filter(p -> !p)
                .orElseThrow(CategoryExistException::new);
        Category category = Category.builder()
                .categoryName(categoryName)
                .build();
        categoryRepos.save(category);
    }

    /**
     * Delete a category by id, if the category is present, else throw <code>CategoryNotFoundException</code>.
     * But if products with this category exist, a <code>CategoryDeleteException</code> is thrown.
     * If you still want to delete, you can use the root flag. But if any of these products are used in
     * orders, a <code>ProductUsedInOrdersException</code> is thrown.
     *
     * @param categoryId id of the category
     * @param root       flag, used for delete a category with existing products
     */
    public void deleteCategory(@Valid @Min(value = 1, message = Constants.CATEGORY_ID_VALIDATION_EXCEPTION) Long categoryId,
                               @NonNull Boolean root) {
        Optional.of(categoryRepos.existsById(categoryId))
                .filter(p -> p)
                .orElseThrow(CategoryNotFoundException::new);
        boolean prodExist = productRepos.existsProductByCategoryId(categoryId);
        if (prodExist) {
            if (!root) {
                throw new CategoryDeleteException(Constants.CATEGORY_PRODUCT_EXIST_EXCEPTION_MESSAGE);
            }
            Optional.of(orderItemRepos.existsByOrderItemPK_Product_CategoryId(categoryId))
                    .filter(p -> !p)
                    .orElseThrow(ProductUsedInOrdersException::new);
        }
        productRepos.deleteAllByCategory_Id(categoryId);
        categoryRepos.deleteById(categoryId);
    }

    /**
     * Update the category name by the category id. If the category is not found,
     * a <code>CategoryNotFoundException</code> is thrown
     *
     * @param dto include new category name and category id
     */
    public void updateCategory(@Valid UpdateCategoryDTO dto) {
        Category category = categoryRepos.findById(dto.getCategoryId())
                .orElseThrow(CategoryNotFoundException::new);
        category.setCategoryName(dto.getCategoryName());
        categoryRepos.save(category);
    }

    /**
     * Return all existing categories
     *
     * @return all existing categories
     */
    @Transactional(readOnly = true)
    public CategoriesDTO getAllCategories() {
        List<Category> list = categoryRepos.findAll();
        return CategoryMapper.INSTANCE.toDTOFromEntityList(list);
    }

}
