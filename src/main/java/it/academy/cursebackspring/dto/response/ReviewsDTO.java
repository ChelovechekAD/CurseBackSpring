package it.academy.cursebackspring.dto.response;

import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewsDTO {

    @NotNull
    private List<ReviewDTO> reviewDTOList;

    @NotNull
    @Min(value = 0, message = Constants.COUNT_CANNOT_BE_LESS_THAN_VALIDATION_EXCEPTION)
    private Long countOfReviews;

}
