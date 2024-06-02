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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepos categoryRepos;
    private final ProductRepos productRepos;
    private final OrderItemRepos orderItemRepos;

    /**
     * Save new category, if category with this name already exist then throw <code>CategoryExistException</code>
     *
     * @param categoryName name of the new category
     */
    @Override
    public void addCategory(String categoryName) {
        if (categoryRepos.existsByCategoryName(categoryName)){
            throw new CategoryExistException();
        }
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
    @Override
    public void deleteCategory(Long categoryId, Boolean root) {
        if (!categoryRepos.existsById(categoryId)){
            throw new CategoryNotFoundException();
        }
        boolean prodExist = productRepos.existsProductByCategoryId(categoryId);
        if (prodExist) {
            if (!root) {
                throw new CategoryDeleteException(Constants.CATEGORY_PRODUCT_EXIST_EXCEPTION_MESSAGE);
            }
            if (orderItemRepos.existsByOrderItemPK_Product_CategoryId(categoryId)){
                throw new ProductUsedInOrdersException();
            }
            productRepos.deleteAllByCategory_Id(categoryId);
        }
        categoryRepos.deleteById(categoryId);
    }

    /**
     * Update the category name by the category id. If the category is not found,
     * a <code>CategoryNotFoundException</code> is thrown
     *
     * @param dto include new category name and category id
     */
    @Override
    public void updateCategory(UpdateCategoryDTO dto) {
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
    @Override
    @Transactional(readOnly = true)
    public CategoriesDTO getAllCategories() {
        List<Category> list = categoryRepos.findAll();
        return CategoryMapper.INSTANCE.toDTOFromEntityList(list);
    }

}
