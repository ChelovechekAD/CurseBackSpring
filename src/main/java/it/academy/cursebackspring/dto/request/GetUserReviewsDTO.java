package it.academy.cursebackspring.dto.request;

import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetUserReviewsDTO {

    @NotNull(message = Constants.PAGE_NUM_CANNOT_BE_NULL_VALIDATION_EXCEPTION)
    @Min(value = Constants.MIN_PAGE_NUM, message = Constants.PAGE_NUM_VALIDATION_EXCEPTION)
    private Integer pageNum;

    @NotNull(message = Constants.COUNT_PER_PAGE_CANNOT_BE_NULL_VALIDATION_EXCEPTION)
    @Min(value = Constants.MIN_COUNT_PER_PAGE, message = Constants.COUNT_PER_PAGE_VALIDATION_EXCEPTION)
    private Integer countPerPage;

    @NotNull(message = Constants.USER_ID_CANNOT_BE_NULL_VALIDATION_EXCEPTION)
    @Min(value = Constants.MIN_USER_ID, message = Constants.USER_ID_VALIDATION_EXCEPTION)
    private Long userId;

}
