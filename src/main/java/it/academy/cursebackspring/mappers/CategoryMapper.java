package it.academy.cursebackspring.mappers;

import it.academy.cursebackspring.dto.request.CategoryDTO;
import it.academy.cursebackspring.dto.response.CategoriesDTO;
import it.academy.cursebackspring.models.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDTO toDTOFromEntity(Category category);

    default CategoriesDTO toDTOFromEntityList(List<Category> categories) {
        return new CategoriesDTO(categories.stream().map(CategoryMapper.INSTANCE::toDTOFromEntity).toList());
    }


}
