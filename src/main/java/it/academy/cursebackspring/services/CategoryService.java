package it.academy.cursebackspring.services;

import it.academy.cursebackspring.dto.request.UpdateCategoryDTO;
import it.academy.cursebackspring.dto.response.CategoriesDTO;

public interface CategoryService {
    void addCategory(String categoryName);

    CategoriesDTO getAllCategories();

    void deleteCategory(Long categoryId, Boolean root);

    void updateCategory(UpdateCategoryDTO dto);
}
