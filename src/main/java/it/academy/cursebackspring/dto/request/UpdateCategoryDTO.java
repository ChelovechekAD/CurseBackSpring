package it.academy.cursebackspring.dto.request;

import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCategoryDTO {

    @Min(value = 1, message = Constants.CATEGORY_ID_VALIDATION_EXCEPTION)
    private Long categoryId;

    @NotBlank(message = Constants.CATEGORY_NAME_VALIDATION_EXCEPTION)
    private String categoryName;

}
