package it.academy.cursebackspring.services;


import it.academy.cursebackspring.dto.request.CreateProductDTO;
import it.academy.cursebackspring.dto.request.GetProductPage;
import it.academy.cursebackspring.dto.request.UpdateProductDTO;
import it.academy.cursebackspring.dto.response.ProductDTO;
import it.academy.cursebackspring.dto.response.ProductsDTO;
import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import java.util.Map;

public interface ProductService {
    void addProduct(@Valid CreateProductDTO createProductDTO);

    void updateProduct(@Valid UpdateProductDTO updateProductDTO);

    void deleteProduct(@Valid @Min(value = 1, message = Constants.PRODUCT_ID_VALIDATION_EXCEPTION) Long id);

    ProductDTO getProductById(@Valid @Min(value = 1, message = Constants.PRODUCT_ID_VALIDATION_EXCEPTION) Long id);

    ProductsDTO getAllExistProducts(@Valid GetProductPage dto);

    ProductsDTO getAllExistProductByFilterParam(@Valid Integer pageNum,
                                                @Valid Integer countPerPage, @Valid Map<String, Object> paramMap);
}
