package it.academy.cursebackspring.dto.request;

import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginUserDTO {

    @NotBlank(message = Constants.EMAIL_CANNOT_BE_BLANK_OR_NULL_VALIDATION_EXCEPTION)
    @Pattern(regexp = Constants.REGEXP_EMAIL)
    private String email;

    @NotBlank(message = Constants.PASSWORD_CANNOT_BE_BLANK_OR_NULL_VALIDATION_EXCEPTION)
    private String password;

}
