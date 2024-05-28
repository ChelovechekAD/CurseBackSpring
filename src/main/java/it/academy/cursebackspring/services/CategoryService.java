package it.academy.cursebackspring.services;

import it.academy.cursebackspring.dto.request.UpdateCategoryDTO;
import it.academy.cursebackspring.dto.response.CategoriesDTO;
import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

public interface CategoryService {
    void addCategory(@Valid @NotBlank(message = Constants.CATEGORY_NAME_VALIDATION_EXCEPTION) String categoryName);

    CategoriesDTO getAllCategories();

    void deleteCategory(@Valid @Min(value = 1, message = Constants.CATEGORY_ID_VALIDATION_EXCEPTION) Long categoryId,
                        @NonNull Boolean root);

    void updateCategory(@Valid UpdateCategoryDTO dto);
}
