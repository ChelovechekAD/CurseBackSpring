package it.academy.cursebackspring.dto.request;

import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserDTO {

    @NotNull(message = Constants.USER_ID_CANNOT_BE_NULL_VALIDATION_EXCEPTION)
    @Min(value = Constants.MIN_USER_ID, message = Constants.USER_ID_VALIDATION_EXCEPTION)
    private Long id;

    @NotBlank(message = Constants.NAME_CANNOT_BE_BLANK_VALIDATION_EXCEPTION)
    private String name;

    @NotBlank(message = Constants.SURNAME_CANNOT_BE_BLANK_VALIDATION_EXCEPTION)
    private String surname;

    @NotBlank(message = Constants.CITY_CANNOT_BE_BLANK_VALIDATION_EXCEPTION)
    private String city;

    @NotBlank(message = Constants.STREET_CANNOT_BE_BLANK_VALIDATION_EXCEPTION)
    private String street;

    @NotNull(message = Constants.BUILDING_CANNOT_BE_NULL_OR_BLANK_VALIDATION_EXCEPTION)
    @Min(value = Constants.MIN_BUILDING, message = Constants.BUILDING_MUST_BE_BETWEEN_VALIDATION_EXCEPTION)
    @Max(value = Constants.MAX_BUILDING, message = Constants.BUILDING_MUST_BE_BETWEEN_VALIDATION_EXCEPTION)
    private Integer building;

}
