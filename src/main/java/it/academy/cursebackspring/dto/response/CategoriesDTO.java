package it.academy.cursebackspring.dto.response;

import it.academy.cursebackspring.dto.request.CategoryDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoriesDTO {

    @NotNull
    private List<CategoryDTO> categoryDTOList;

}
