package it.academy.cursebackspring.controllers;

import it.academy.cursebackspring.dto.request.CreateReviewDTO;
import it.academy.cursebackspring.dto.request.DeleteReviewDTO;
import it.academy.cursebackspring.dto.request.GetReviewDTO;
import it.academy.cursebackspring.dto.request.GetReviewsDTO;
import it.academy.cursebackspring.dto.response.ProductDTO;
import it.academy.cursebackspring.dto.response.ReviewDTO;
import it.academy.cursebackspring.dto.response.ReviewsDTO;
import it.academy.cursebackspring.models.User;
import it.academy.cursebackspring.services.ProductService;
import it.academy.cursebackspring.services.ReviewService;
import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/api/v1/catalog/product/{productId}")
public class ProductController {

    private final ProductService productService;
    private final ReviewService reviewService;

    @GetMapping()
    public ResponseEntity<ProductDTO> getProductById(@PathVariable(value = "productId")
                                                     @Valid
                                                     @NotNull
                                                     @Min(value = 1, message = Constants.PRODUCT_ID_VALIDATION_EXCEPTION) Long id) {
        ProductDTO productDTO = productService.getProductById(id);
        return ResponseEntity.ok(productDTO);
    }

    @GetMapping("/reviews")
    public ResponseEntity<ReviewsDTO> getProductReviewsById(@PathVariable(value = "productId")
                                                            @Valid
                                                            @NotNull
                                                            @Min(value = 1, message = Constants.PRODUCT_ID_VALIDATION_EXCEPTION) Long productId,
                                                            @Valid GetReviewsDTO dto) {
        dto.setProductId(productId);
        ReviewsDTO reviewsDTO = reviewService.getAllReviewsPage(dto);
        return ResponseEntity.ok(reviewsDTO);
    }

    @PostMapping("/reviews/create")
    public ResponseEntity<?> createNewReview(@PathVariable(value = "productId")
                                             @Valid
                                             @NotNull
                                             @Min(value = 1, message = Constants.PRODUCT_ID_VALIDATION_EXCEPTION) Long id,
                                             @Valid @RequestBody CreateReviewDTO dto) {
        Long userId  = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        dto.setUserId(userId);
        dto.setProductId(id);
        reviewService.createReview(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reviews/user_review")
    public ResponseEntity<ReviewDTO> getUserReviewOnProduct(@PathVariable(value = "productId")
                                                            @Valid
                                                            @NotNull
                                                            @Min(value = 1, message = Constants.PRODUCT_ID_VALIDATION_EXCEPTION) Long productId) {
        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        ReviewDTO reviewDTO = reviewService.getSingleReviewOnProductByUserId(new GetReviewDTO(userId, productId));
        return ResponseEntity.ok(reviewDTO);
    }

    @DeleteMapping("/reviews/delete")
    public ResponseEntity<?> deleteReview(@PathVariable(value = "productId")
                                          @Valid
                                          @NotNull
                                          @Min(value = 1, message = Constants.PRODUCT_ID_VALIDATION_EXCEPTION) Long productId) {
        Long userId = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        reviewService.deleteReviewOnProductByUserId(new DeleteReviewDTO(userId, productId));
        return ResponseEntity.ok().build();
    }
}
