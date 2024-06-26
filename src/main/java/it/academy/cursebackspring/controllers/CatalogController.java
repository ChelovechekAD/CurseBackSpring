package it.academy.cursebackspring.controllers;

import it.academy.cursebackspring.dto.request.GetProductPageDTO;
import it.academy.cursebackspring.dto.response.CategoriesDTO;
import it.academy.cursebackspring.dto.response.ProductsDTO;
import it.academy.cursebackspring.services.CategoryService;
import it.academy.cursebackspring.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/catalog/")
@RequiredArgsConstructor
@Validated
public class CatalogController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping("/products")
    public ResponseEntity<ProductsDTO> getProductPage(@Valid GetProductPageDTO dto) {
        ProductsDTO productsDTO = productService.getAllExistProducts(dto);
        return ResponseEntity.ok(productsDTO);
    }

    @GetMapping("/categories")
    public ResponseEntity<CategoriesDTO> getAllCategories() {
        CategoriesDTO categoriesDTO = categoryService.getAllCategories();
        return ResponseEntity.ok(categoriesDTO);
    }


}
