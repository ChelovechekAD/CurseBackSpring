package it.academy.cursebackspring.services.impl;


import it.academy.cursebackspring.dto.request.CreateProductDTO;
import it.academy.cursebackspring.dto.request.GetProductPage;
import it.academy.cursebackspring.dto.request.UpdateProductDTO;
import it.academy.cursebackspring.dto.response.ProductDTO;
import it.academy.cursebackspring.dto.response.ProductsDTO;
import it.academy.cursebackspring.exceptions.CategoryNotFoundException;
import it.academy.cursebackspring.exceptions.ProductNotFoundException;
import it.academy.cursebackspring.exceptions.ProductUsedInOrdersException;
import it.academy.cursebackspring.mappers.ProductMapper;
import it.academy.cursebackspring.models.Category;
import it.academy.cursebackspring.models.Product;
import it.academy.cursebackspring.repositories.CategoryRepos;
import it.academy.cursebackspring.repositories.OrderItemRepos;
import it.academy.cursebackspring.repositories.ProductRepos;
import it.academy.cursebackspring.services.ProductService;
import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
@Validated
public class ProductServiceImpl implements ProductService {

    private final CategoryRepos categoryRepos;
    private final ProductRepos productRepos;
    private final OrderItemRepos orderItemRepos;

    /**
     * Add a new product, if category is not found, a <code>CategoryNotFoundException</code> is thrown.
     *
     * @param createProductDTO important data for method execution
     */
    public void addProduct(@Valid CreateProductDTO createProductDTO) {
        Category category = categoryRepos.findById(createProductDTO.getCategoryId())
                .orElseThrow(CategoryNotFoundException::new);
        Product product = ProductMapper.INSTANCE.toEntityFromCreateDTO(createProductDTO);
        product.setCategory(category);
        productRepos.save(product);
    }

    /**
     * Update existing product by product id,
     * if category is not found, a <code>CategoryNotFoundException</code> is thrown,
     * if product is not found a <code>ProductNotFoundException</code> is thrown.
     *
     * @param updateProdDTO important data for method execution
     */
    public void updateProduct(@Valid UpdateProductDTO updateProdDTO) {
        Optional.of(categoryRepos.existsById(updateProdDTO.getId()))
                .filter(p -> p)
                .orElseThrow(CategoryNotFoundException::new);
        Optional.of(productRepos.existsById(updateProdDTO.getId()))
                .filter(p -> p)
                .orElseThrow(ProductNotFoundException::new);
        Product product = ProductMapper.INSTANCE.toEntityFromUpdateDTO(updateProdDTO);
        productRepos.save(product);
    }

    /**
     * Delete the product by product id, if product is used in orders, a <code>ProductUserInOrdersException</code> is thrown.
     *
     * @param id product id
     */
    public void deleteProduct(@Valid @Min(value = 1, message = Constants.PRODUCT_ID_VALIDATION_EXCEPTION) Long id) {
        Optional.of(orderItemRepos.existsByOrderItemPK_ProductId(id))
                .filter(p -> !p)
                .orElseThrow(ProductUsedInOrdersException::new);
        productRepos.deleteById(id);
    }

    /**
     * Get the product by id, if product is not found, a <code>ProductNotFoundException</code> is thrown.
     *
     * @param id product id
     * @return dto with product info
     */
    @Transactional(readOnly = true)
    public ProductDTO getProductById(@Valid @Min(value = 1, message = Constants.PRODUCT_ID_VALIDATION_EXCEPTION) Long id) {
        Product product = productRepos.findById(id)
                .orElseThrow(ProductNotFoundException::new);
        return ProductMapper.INSTANCE.toDTOFromEntity(product);
    }

    /**
     * Get the page of products, using filter by category if category id is not null,
     * method return a page of products filtered on category id.
     *
     * @param dto important data for method execution
     * @return dto which contains list of products and total count of products.
     */
    @Transactional(readOnly = true)
    public ProductsDTO getAllExistProducts(@Valid GetProductPage dto) {
        PageRequest pageRequest = PageRequest.of(dto.getPageNum(), dto.getCountPerPage());
        Page<Product> products = (dto.getCategoryId() != null) ?
                productRepos.findAllByCategoryId(pageRequest, dto.getCategoryId()) :
                productRepos.findAll(pageRequest);
        return ProductMapper.INSTANCE.toDTOFromEntityList(products.stream().toList(), products.getTotalElements());
    }


    public ProductsDTO getAllExistProductByFilterParam(@Valid Integer pageNum,
                                                       @Valid Integer countPerPage, @Valid Map<String, Object> paramMap) {
        return null;
    }
}
