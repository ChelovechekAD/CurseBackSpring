package it.academy.cursebackspring.repositories;

import it.academy.cursebackspring.models.Review;
import it.academy.cursebackspring.models.embedded.ReviewPK;
import it.academy.cursebackspring.utilities.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepos extends JpaRepository<Review, ReviewPK> {

    Page<Review> findAllByReviewPK_ProductId(Long productId, Pageable pageable);

    Page<Review> findAllByReviewPK_UserId(Long userId, Pageable pageable);

    @Query(Constants.SELECT_AVG_RATING_FROM_REVIEW)
    Double getAvgRating();

}

