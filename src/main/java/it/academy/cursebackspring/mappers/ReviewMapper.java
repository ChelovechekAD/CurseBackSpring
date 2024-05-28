package it.academy.cursebackspring.mappers;

import it.academy.cursebackspring.dto.request.CreateReviewDTO;
import it.academy.cursebackspring.dto.response.ReviewDTO;
import it.academy.cursebackspring.dto.response.ReviewsDTO;
import it.academy.cursebackspring.dto.response.UserReviewInfoDTO;
import it.academy.cursebackspring.dto.response.UserReviewsDTO;
import it.academy.cursebackspring.models.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper
public interface ReviewMapper {

    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

    @Mappings({
            @Mapping(source = "createReviewDTO.rating", target = "rating"),
            @Mapping(source = "createReviewDTO.description", target = "description"),
            @Mapping(target = "reviewPK", ignore = true)
    })
    Review toEntityFromCreateDTO(CreateReviewDTO createReviewDTO);

    @Mappings({
            @Mapping(source = "review.reviewPK.user.id", target = "userId"),
            @Mapping(source = "review.reviewPK.user.name", target = "name"),
            @Mapping(source = "review.reviewPK.user.surname", target = "surname"),
    })
    ReviewDTO toDTOFromEntity(Review review);

    @Mappings({
            @Mapping(source = "review.description", target = "description"),
            @Mapping(source = "review.rating", target = "rating"),
            @Mapping(source = "review.reviewPK.product.id", target = "productId"),
            @Mapping(source = "review.reviewPK.product.imageLink", target = "imageLink"),
            @Mapping(source = "review.reviewPK.product.name", target = "name"),
    })
    UserReviewInfoDTO toUserReviewDTOFromEntity(Review review);

    default ReviewsDTO toDTOFromEntityList(Page<Review> list, Long count) {
        List<ReviewDTO> reviewDTOList = list.stream()
                .map(ReviewMapper.INSTANCE::toDTOFromEntity)
                .toList();
        return new ReviewsDTO(reviewDTOList, count);
    }

    default UserReviewsDTO toUserReviewsDTOFromEntityList(Page<Review> list, Long count) {
        List<UserReviewInfoDTO> reviewDTOList = list.stream()
                .map(ReviewMapper.INSTANCE::toUserReviewDTOFromEntity)
                .toList();
        return new UserReviewsDTO(reviewDTOList, count);
    }


}
