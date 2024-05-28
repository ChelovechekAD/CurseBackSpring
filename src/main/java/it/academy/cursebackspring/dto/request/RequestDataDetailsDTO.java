package it.academy.cursebackspring.dto.request;

import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestDataDetailsDTO implements Serializable {

    @Min(value = 10, message = Constants.COUNT_PER_PAGE_VALIDATION_EXCEPTION)
    private Integer countPerPage;

    @Min(value = 0, message = Constants.PAGE_NUM_VALIDATION_EXCEPTION)
    private Integer pageNum;

}
