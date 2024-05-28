package it.academy.cursebackspring.dto.request;

import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetReviewsDTO {

    @Min(value = 0, message = Constants.PAGE_NUM_VALIDATION_EXCEPTION)
    private Integer pageNum;

    @Min(value = 10, message = Constants.COUNT_PER_PAGE_VALIDATION_EXCEPTION)
    private Integer countPerPage;

    @Min(value = 1, message = Constants.PRODUCT_ID_VALIDATION_EXCEPTION)
    private Long productId;


}
