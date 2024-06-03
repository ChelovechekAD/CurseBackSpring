package it.academy.cursebackspring.dto.request;

import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegUserDTO implements Serializable {

    @NotBlank(message = Constants.EMAIL_CANNOT_BE_BLANK_OR_NULL_VALIDATION_EXCEPTION)
    @Pattern(regexp = Constants.REGEXP_EMAIL)
    private String email;

    @NotBlank(message = Constants.NAME_CANNOT_BE_BLANK_VALIDATION_EXCEPTION)
    private String name;

    @NotBlank(message = Constants.SURNAME_CANNOT_BE_BLANK_VALIDATION_EXCEPTION)
    private String surname;

    @NotBlank(message = Constants.PASSWORD_CANNOT_BE_BLANK_OR_NULL_VALIDATION_EXCEPTION)
    private String password;

    @NotBlank(message = Constants.PASSWORD_CANNOT_BE_BLANK_OR_NULL_VALIDATION_EXCEPTION)
    private String passwordConfirm;

    private String city;

    private String street;

    @Min(value = Constants.MIN_BUILDING, message = Constants.BUILDING_MUST_BE_BETWEEN_VALIDATION_EXCEPTION)
    @Max(value = Constants.MAX_BUILDING, message = Constants.BUILDING_MUST_BE_BETWEEN_VALIDATION_EXCEPTION)
    private Integer building;

    @NotBlank(message = Constants.PHONE_NUMBER_CANNOT_BE_BLANK_OR_NULL_VALIDATION_EXCEPTION)
    @Pattern(regexp = Constants.REG_EXP_PHONE_NUMBER, message = Constants.PHONE_NUMBER_VALIDATION_EXCEPTION)
    private String phoneNumber;

}
