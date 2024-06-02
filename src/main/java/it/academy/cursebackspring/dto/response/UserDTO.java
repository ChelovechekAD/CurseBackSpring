package it.academy.cursebackspring.dto.response;

import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @NotNull
    @Min(value = 1, message = Constants.USER_ID_VALIDATION_EXCEPTION)
    private Long id;

    private String building;

    private String city;

    private String street;

    @Email
    private String email;

    @NotBlank(message = Constants.NAME_CANNOT_BE_BLANK_VALIDATION_EXCEPTION)
    private String name;

    @NotBlank
    @Pattern(regexp = Constants.REG_EXP_PHONE_NUMBER, message = Constants.PHONE_NUMBER_VALIDATION_EXCEPTION)
    private String phoneNumber;

    @NotBlank(message = Constants.SURNAME_CANNOT_BE_BLANK_VALIDATION_EXCEPTION)
    private String surname;

    @NotEmpty(message = Constants.SET_OF_ROLES_CANNOT_BE_EMPTY_VALIDATION_EXCEPTION)
    private Set<String> roles;

}
