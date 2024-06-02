package it.academy.cursebackspring.services;


import it.academy.cursebackspring.dto.request.*;
import it.academy.cursebackspring.dto.response.ReviewDTO;
import it.academy.cursebackspring.dto.response.ReviewsDTO;
import it.academy.cursebackspring.dto.response.UserReviewsDTO;
import jakarta.validation.Valid;

public interface ReviewService {
    void createReview(CreateReviewDTO createReviewDTO);

    ReviewDTO getSingleReviewOnProductByUserId(GetReviewDTO getReviewDTO);

    void deleteReviewOnProductByUserId(DeleteReviewDTO deleteReviewDTO);

    ReviewsDTO getAllReviewsPage(GetReviewsDTO getReviewsDTO);

    UserReviewsDTO getAllUserReviews(GetUserReviewsDTO getUserReviewsDTO);
}
