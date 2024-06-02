package it.academy.cursebackspring.mappers;

import it.academy.cursebackspring.dto.request.CreateProductDTO;
import it.academy.cursebackspring.dto.request.UpdateProductDTO;
import it.academy.cursebackspring.dto.response.ProductDTO;
import it.academy.cursebackspring.dto.response.ProductsDTO;
import it.academy.cursebackspring.models.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mappings({
            @Mapping(target = "category", ignore = true),
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "rating", defaultValue = "0", ignore = true)
    })
    Product toEntityFromCreateDTO(CreateProductDTO dto);

    @Mappings({
            @Mapping(target = "rating", ignore = true),
            @Mapping(target = "category", ignore = true)
    })
    Product toEntityFromUpdateDTO(UpdateProductDTO dto);

    @Mapping(target = "categoryId", source = "product.category.id")
    ProductDTO toDTOFromEntity(Product product);

    default ProductsDTO toDTOFromEntityList(List<Product> products, Long totalCount) {
        List<ProductDTO> productDTOS = products.stream()
                .map(ProductMapper.INSTANCE::toDTOFromEntity)
                .toList();
        return new ProductsDTO(productDTOS, totalCount);
    }


}
