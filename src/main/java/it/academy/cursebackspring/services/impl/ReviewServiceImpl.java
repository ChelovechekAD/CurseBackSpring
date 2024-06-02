package it.academy.cursebackspring.services.impl;


import it.academy.cursebackspring.dto.request.*;
import it.academy.cursebackspring.dto.response.ReviewDTO;
import it.academy.cursebackspring.dto.response.ReviewsDTO;
import it.academy.cursebackspring.dto.response.UserReviewsDTO;
import it.academy.cursebackspring.exceptions.ProductNotFoundException;
import it.academy.cursebackspring.exceptions.ReviewExistException;
import it.academy.cursebackspring.exceptions.ReviewNotFoundException;
import it.academy.cursebackspring.exceptions.UserNotFoundException;
import it.academy.cursebackspring.mappers.ReviewMapper;
import it.academy.cursebackspring.models.Product;
import it.academy.cursebackspring.models.Review;
import it.academy.cursebackspring.models.User;
import it.academy.cursebackspring.models.embedded.ReviewPK;
import it.academy.cursebackspring.repositories.ProductRepos;
import it.academy.cursebackspring.repositories.ReviewRepos;
import it.academy.cursebackspring.repositories.UserRepos;
import it.academy.cursebackspring.services.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED)
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepos reviewRepos;
    private final UserRepos userRepos;
    private final ProductRepos productRepos;

    /**
     * Create a new review.
     * If product is not found, a <code>ProductNotFoundException</code> is thrown.
     * If user is not found, a <code>UserNotFoundException</code> is thrown.
     *
     * @param dto - important data for method execution
     */
    @Override
    public void createReview(CreateReviewDTO dto) {
        List<Object> list = getUserAndProduct(dto.getUserId(), dto.getProductId());
        Product product = (Product) list.get(0);
        User user = (User) list.get(1);
        ReviewPK reviewPK = new ReviewPK(user, product);
        Review review = ReviewMapper.INSTANCE.toEntityFromCreateDTO(dto);
        review.setReviewPK(reviewPK);
        if (reviewRepos.existsById(reviewPK)) {
            throw new ReviewExistException();
        }
        reviewRepos.save(review);
        updateTotalRatingForProduct(product);
    }

    /**
     * Get the review by product id and user id.
     * If product is not found, a <code>ProductNotFoundException</code> is thrown.
     * If user is not found, a <code>UserNotFoundException</code> is thrown.
     *
     * @param dto - important data for method execution
     * @return dto with review info
     */
    @Override
    @Transactional(readOnly = true)
    public ReviewDTO getSingleReviewOnProductByUserId(GetReviewDTO dto) {
        List<Object> list = getUserAndProduct(dto.getUserId(), dto.getProductId());
        Product product = (Product) list.get(0);
        User user = (User) list.get(1);
        ReviewPK reviewPK = new ReviewPK(user, product);
        Review review = reviewRepos.findById(reviewPK)
                .orElseThrow(ReviewNotFoundException::new);
        return ReviewMapper.INSTANCE.toDTOFromEntity(review);
    }

    /**
     * Delete the review by user id and product id.
     * If product is not found, a <code>ProductNotFoundException</code> is thrown.
     * If user is not found, a <code>UserNotFoundException</code> is thrown.
     *
     * @param dto important data for method execution
     */
    @Override
    public void deleteReviewOnProductByUserId(DeleteReviewDTO dto) {
        List<Object> list = getUserAndProduct(dto.getUserId(), dto.getProductId());
        Product product = (Product) list.get(0);
        User user = (User) list.get(1);
        ReviewPK reviewPK = new ReviewPK(user, product);
        reviewRepos.deleteById(reviewPK);
        updateTotalRatingForProduct(product);
    }

    /**
     * Get the page of reviews on the product by id.
     *
     * @param getReviewsDTO - important data for method execution
     * @return dto with reviews on the product and total count.
     */
    @Override
    @Transactional(readOnly = true)
    public ReviewsDTO getAllReviewsPage(GetReviewsDTO getReviewsDTO) {
        Page<Review> reviewPage = reviewRepos.findAllByReviewPK_ProductId(getReviewsDTO.getProductId(),
                PageRequest.of(getReviewsDTO.getPageNum(), getReviewsDTO.getCountPerPage()));
        return ReviewMapper.INSTANCE.toDTOFromEntityList(reviewPage, reviewPage.getTotalElements());
    }

    /**
     * Get the page of user reviews by user id.
     *
     * @param getUserReviewsDTO - important data for method execution
     * @return dto with user reviews and total count.
     */
    @Override
    @Transactional(readOnly = true)
    public UserReviewsDTO getAllUserReviews(GetUserReviewsDTO getUserReviewsDTO) {
        Page<Review> reviewPage = reviewRepos.findAllByReviewPK_UserId(getUserReviewsDTO.getUserId(),
                PageRequest.of(getUserReviewsDTO.getPageNum(), getUserReviewsDTO.getCountPerPage()));
        return ReviewMapper.INSTANCE.toUserReviewsDTOFromEntityList(reviewPage, reviewPage.getTotalElements());
    }

    private void updateTotalRatingForProduct(Product product) {
        Double newRating = reviewRepos.getAvgRating();
        newRating = Optional.ofNullable(newRating).orElse(0d);
        product.setRating(newRating);
        productRepos.save(product);
    }

    private List<Object> getUserAndProduct(Long userId, Long productId) {
        Product product = productRepos.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
        User user = userRepos.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        return List.of(product, user);
    }

}
