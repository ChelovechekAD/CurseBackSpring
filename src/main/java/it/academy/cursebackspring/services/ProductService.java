package it.academy.cursebackspring.services;


import it.academy.cursebackspring.dto.request.CreateProductDTO;
import it.academy.cursebackspring.dto.request.GetProductPageDTO;
import it.academy.cursebackspring.dto.request.UpdateProductDTO;
import it.academy.cursebackspring.dto.response.ProductDTO;
import it.academy.cursebackspring.dto.response.ProductsDTO;

public interface ProductService {
    void addProduct(CreateProductDTO createProductDTO);

    void updateProduct(UpdateProductDTO updateProductDTO);

    void deleteProduct(Long id);

    ProductDTO getProductById(Long id);

    ProductsDTO getAllExistProducts(GetProductPageDTO dto);

}
