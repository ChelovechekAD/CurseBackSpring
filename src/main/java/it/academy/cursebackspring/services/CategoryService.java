package it.academy.cursebackspring.services;

import it.academy.cursebackspring.dto.request.UpdateCategoryDTO;
import it.academy.cursebackspring.dto.response.CategoriesDTO;
import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

public interface CategoryService {
    void addCategory(String categoryName);

    CategoriesDTO getAllCategories();

    void deleteCategory(Long categoryId, Boolean root);

    void updateCategory(UpdateCategoryDTO dto);
}
