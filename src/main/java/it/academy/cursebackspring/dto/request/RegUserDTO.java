package it.academy.cursebackspring.dto.request;

import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.constraints.Email;
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

    @Email
    private String email;

    @NotBlank(message = Constants.NAME_CANNOT_BE_BLANK_VALIDATION_EXCEPTION)
    private String name;

    @NotBlank(message = Constants.SURNAME_CANNOT_BE_BLANK_VALIDATION_EXCEPTION)
    private String surname;

    @NotBlank
    private String password;

    @NotBlank
    private String passwordConfirm;

    private String city;

    private String street;

    private String building;

    @NotBlank
    @Pattern(regexp = Constants.REG_EXP_PHONE_NUMBER, message = Constants.PHONE_NUMBER_VALIDATION_EXCEPTION)
    private String phoneNumber;

}
