package it.academy.cursebackspring.services.impl;


import it.academy.cursebackspring.dto.request.CreateProductDTO;
import it.academy.cursebackspring.dto.request.GetProductPageDTO;
import it.academy.cursebackspring.dto.request.ParamFilterDTO;
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
import it.academy.cursebackspring.repositories.specifications.ProductSpecification;
import it.academy.cursebackspring.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED)
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final CategoryRepos categoryRepos;
    private final ProductRepos productRepos;
    private final OrderItemRepos orderItemRepos;

    /**
     * Add a new product, if category is not found, a <code>CategoryNotFoundException</code> is thrown.
     *
     * @param createProductDTO important data for method execution
     */
    @Override
    public void addProduct(CreateProductDTO createProductDTO) {
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
    @Override
    public void updateProduct(UpdateProductDTO updateProdDTO) {
        if (!categoryRepos.existsById(updateProdDTO.getCategoryId())) {
            throw new CategoryNotFoundException();
        }
        if (!productRepos.existsById(updateProdDTO.getId())) {
            throw new ProductNotFoundException();
        }
        Product product = ProductMapper.INSTANCE.toEntityFromUpdateDTO(updateProdDTO);
        productRepos.save(product);
    }

    /**
     * Delete the product by product id, if product is used in orders, a <code>ProductUserInOrdersException</code> is thrown.
     *
     * @param id product id
     */
    @Override
    public void deleteProduct(Long id) {
        if (orderItemRepos.existsByOrderItemPK_ProductId(id)) {
            throw new ProductUsedInOrdersException();
        }
        productRepos.deleteById(id);
    }

    /**
     * Get the product by id, if product is not found, a <code>ProductNotFoundException</code> is thrown.
     *
     * @param id product id
     * @return dto with product info
     */
    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        Product product = productRepos.findById(id)
                .orElseThrow(ProductNotFoundException::new);
        return ProductMapper.INSTANCE.toDTOFromEntity(product);
    }

    /**
     * Get the page of products, using filter by category or name,
     * method return a filtered page of products.
     *
     * @param dto important data for method execution
     * @return dto which contains list of products and total count of products.
     */
    @Override
    @Transactional(readOnly = true)
    public ProductsDTO getAllExistProducts(GetProductPageDTO dto) {
        PageRequest pageRequest = PageRequest.of(dto.getPageNum(), dto.getCountPerPage());
        Page<Product> products;
        List<ParamFilterDTO> filters = new ArrayList<>();
        if (dto.getNameFilter() != null) {
            filters.add(toParamFilter(dto.getNameFilter()));
        }
        if (dto.getCategoryFilter() != null) {
            filters.add(toParamFilter(dto.getCategoryFilter()));
        }

        if (filters.size() > 0) {
            Specification<Product> spec = new ProductSpecification(filters.get(0));
            filters.remove(0);
            for (ParamFilterDTO paramFilterDTO : filters) {
                spec = spec.and(new ProductSpecification(paramFilterDTO));
            }
            products = productRepos.findAll(spec, pageRequest);
        } else {
            products = productRepos.findAll(pageRequest);
        }
        return ProductMapper.INSTANCE.toDTOFromEntityList(products.stream().toList(), products.getTotalElements());
    }

    private ParamFilterDTO toParamFilter(String filterStr) {
        List<String> operations = List.of(":", "=", ">", "<");
        String curOperation = operations.stream()
                .filter(filterStr::contains)
                .collect(Collectors.joining());
        String key = filterStr.substring(0, filterStr.indexOf(curOperation));
        String value = filterStr.substring(filterStr.indexOf(curOperation) + 1);
        return new ParamFilterDTO(curOperation, key, value);
    }
}
