package it.academy.cursebackspring.services;


import it.academy.cursebackspring.dto.request.*;
import it.academy.cursebackspring.dto.response.ReviewDTO;
import it.academy.cursebackspring.dto.response.ReviewsDTO;
import it.academy.cursebackspring.dto.response.UserReviewsDTO;
import jakarta.validation.Valid;

public interface ReviewService {
    void createReview(@Valid CreateReviewDTO createReviewDTO);

    ReviewDTO getSingleReviewOnProductByUserId(@Valid GetReviewDTO getReviewDTO);

    void deleteReviewOnProductByUserId(@Valid DeleteReviewDTO deleteReviewDTO);

    ReviewsDTO getAllReviewsPage(@Valid GetReviewsDTO getReviewsDTO);

    UserReviewsDTO getAllUserReviews(@Valid GetUserReviewsDTO getUserReviewsDTO);
}
