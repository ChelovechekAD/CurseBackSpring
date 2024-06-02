package it.academy.cursebackspring.services;

import it.academy.cursebackspring.dto.request.*;
import it.academy.cursebackspring.dto.response.ReviewsDTO;
import it.academy.cursebackspring.dto.response.UserReviewsDTO;
import it.academy.cursebackspring.exceptions.ProductNotFoundException;
import it.academy.cursebackspring.exceptions.ReviewExistException;
import it.academy.cursebackspring.exceptions.UserNotFoundException;
import it.academy.cursebackspring.models.Product;
import it.academy.cursebackspring.models.Review;
import it.academy.cursebackspring.models.User;
import it.academy.cursebackspring.models.embedded.ReviewPK;
import it.academy.cursebackspring.repositories.ProductRepos;
import it.academy.cursebackspring.repositories.ReviewRepos;
import it.academy.cursebackspring.repositories.UserRepos;
import it.academy.cursebackspring.services.impl.ReviewServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@SpringBootTest
public class ReviewServiceTests {

    @Mock
    ReviewRepos reviewRepos;

    @Mock
    UserRepos userRepos;

    @Mock
    ProductRepos productRepos;

    @InjectMocks
    ReviewServiceImpl reviewService;

    @ParameterizedTest
    @MethodSource("provideCreateReviewData")
    public <T extends Exception> void createReview(CreateReviewDTO dto, boolean isValid, Class<T> exClass, String message) {
        Product product = new Product();
        product.setId(dto.getProductId());
        User user = new User();
        user.setId(dto.getUserId());
        when(productRepos.findById(dto.getProductId())).then(invocation ->
                (invocation.getArgument(0, Long.class) != 2L) ? Optional.of(product) : Optional.empty());
        when(userRepos.findById(dto.getUserId())).then(invocation ->
                (invocation.getArgument(0, Long.class) != 2L) ? Optional.of(user) : Optional.empty());
        when(reviewRepos.existsById(any(ReviewPK.class))).then(invocation -> {
            ReviewPK reviewPK = invocation.getArgument(0, ReviewPK.class);
            return reviewPK.getProduct().getId() == 3L && reviewPK.getUser().getId() == 3L;
        });
        if (isValid) {
            ArgumentCaptor<Review> argumentCaptor = ArgumentCaptor.forClass(Review.class);
            reviewService.createReview(dto);
            verify(reviewRepos).save(argumentCaptor.capture());
            Review review = argumentCaptor.getValue();
            Assertions.assertEquals(List.of(dto.getDescription(), dto.getRating(), dto.getProductId(), dto.getUserId()),
                    List.of(review.getDescription(), review.getRating(),
                            review.getReviewPK().getProduct().getId(),
                            review.getReviewPK().getUser().getId()));
        } else {
            Exception exception = Assertions.assertThrows(exClass, () -> reviewService.createReview(dto));
            Assertions.assertEquals(message, exception.getMessage());
        }
    }

    @ParameterizedTest
    @MethodSource("provideGetSingleReviewOnProductByUserIdData")
    public <T extends Exception> void getSingleReviewOnProductByUserIdTest(GetReviewDTO dto, boolean isValid,
                                                                           Class<T> exClass, String message) {
        Product product = new Product();
        product.setId(dto.getProductId());
        User user = new User();
        user.setId(dto.getUserId());
        when(productRepos.findById(dto.getProductId())).then(invocation ->
                (invocation.getArgument(0, Long.class) != 2L) ? Optional.of(product) : Optional.empty());
        when(userRepos.findById(dto.getUserId())).then(invocation ->
                (invocation.getArgument(0, Long.class) != 2L) ? Optional.of(user) : Optional.empty());
        when(reviewRepos.findById(any(ReviewPK.class))).then(invocation -> {
            ReviewPK reviewPK = invocation.getArgument(0, ReviewPK.class);
            return (reviewPK.getProduct().getId() != 3L || reviewPK.getUser().getId() != 3L) ?
                    Optional.of(new Review(reviewPK, 2.2, "1")) : Optional.empty();
        });
        if (isValid) {
            ArgumentCaptor<ReviewPK> argumentCaptor = ArgumentCaptor.forClass(ReviewPK.class);
            reviewService.getSingleReviewOnProductByUserId(dto);
            verify(reviewRepos).findById(argumentCaptor.capture());
            ReviewPK reviewPk = argumentCaptor.getValue();
            Assertions.assertEquals(List.of(dto.getProductId(), dto.getUserId()),
                    List.of(reviewPk.getProduct().getId(), reviewPk.getUser().getId()));
        } else {
            Exception exception = Assertions.assertThrows(exClass, () -> reviewService.getSingleReviewOnProductByUserId(dto));
            Assertions.assertEquals(message, exception.getMessage());
        }
    }

    @ParameterizedTest
    @MethodSource("provideDeleteReviewOnProductByUserIdData")
    public <T extends Exception> void deleteReviewOnProductByUserIdTest(DeleteReviewDTO dto, boolean isValid,
                                                  Class<T> exClass, String message) {
        when(productRepos.findById(dto.getProductId())).then(invocation ->
                (invocation.getArgument(0, Long.class) != 2L) ? Optional.of(new Product()) : Optional.empty());
        when(userRepos.findById(dto.getUserId())).then(invocation ->
                (invocation.getArgument(0, Long.class) != 2L) ? Optional.of(new User()) : Optional.empty());
        if (isValid) {
            reviewService.deleteReviewOnProductByUserId(dto);
            verify(reviewRepos, times(1)).deleteById(any());
        } else {
            Exception exception = Assertions.assertThrows(exClass, () -> reviewService.deleteReviewOnProductByUserId(dto));
            Assertions.assertEquals(message, exception.getMessage());
        }
    }

    @Test
    public void getAllReviewsPageTest() {
        GetReviewsDTO getReviewsDTO = new GetReviewsDTO(0, 10, 1L);
        when(reviewRepos.findAllByReviewPK_ProductId(any(Long.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(
                        List.of(new Review(), new Review())
                ));
        ReviewsDTO reviewsDTO = reviewService.getAllReviewsPage(getReviewsDTO);
        Assertions.assertEquals(List.of(2L, 2),
                List.of(reviewsDTO.getCountOfReviews(), reviewsDTO.getReviewDTOList().size()));

    }

    @Test
    public void getAllUserReviewsTest() {
        GetUserReviewsDTO getReviewsDTO = new GetUserReviewsDTO(0, 10, 1L);
        when(reviewRepos.findAllByReviewPK_UserId(any(Long.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(
                        List.of(new Review(), new Review())
                ));
        UserReviewsDTO reviewsDTO = reviewService.getAllUserReviews(getReviewsDTO);
        Assertions.assertEquals(List.of(2L, 2),
                List.of(reviewsDTO.getCountOf(), reviewsDTO.getReviews().size()));
    }

    private static Stream<Arguments> provideCreateReviewData() {
        return Stream.of(
                Arguments.of(new CreateReviewDTO("1", 2.2, 1L, 1L), true,
                        null, null),
                Arguments.of(new CreateReviewDTO("1", 2.2, 2L, 1L), false,
                        ProductNotFoundException.class, "Product not found."),
                Arguments.of(new CreateReviewDTO("1", 2.2, 1L, 2L), false,
                        UserNotFoundException.class, "User not found!"),
                Arguments.of(new CreateReviewDTO("1", 2.2, 3L, 1L), true,
                        ReviewExistException.class, "Review already exist!"),
                Arguments.of(new CreateReviewDTO("1", 2.2, 1L, 3L), true,
                        ReviewExistException.class, "Review already exist!")
        );
    }
    private static Stream<Arguments> provideGetSingleReviewOnProductByUserIdData() {
        return Stream.of(
                Arguments.of(new GetReviewDTO(1L, 1L), true,
                        null, null),
                Arguments.of(new GetReviewDTO(1L, 2L), false,
                        ProductNotFoundException.class, "Product not found."),
                Arguments.of(new GetReviewDTO(2L, 1L), false,
                        UserNotFoundException.class, "User not found!"),
                Arguments.of(new GetReviewDTO(3L, 1L), true,
                        ReviewExistException.class, "Review not found!"),
                Arguments.of(new GetReviewDTO(1L, 3L), true,
                        ReviewExistException.class, "Review not found!")
        );
    }
    private static Stream<Arguments> provideDeleteReviewOnProductByUserIdData() {
        return Stream.of(
                Arguments.of(new DeleteReviewDTO(1L, 1L), true,
                        null, null),
                Arguments.of(new DeleteReviewDTO(1L, 2L), false,
                        ProductNotFoundException.class, "Product not found."),
                Arguments.of(new DeleteReviewDTO(2L, 1L), false,
                        UserNotFoundException.class, "User not found!")
        );
    }
}
