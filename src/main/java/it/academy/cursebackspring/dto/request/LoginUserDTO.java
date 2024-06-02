package it.academy.cursebackspring.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.constraints.Email;
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
    @Schema(name = "email", description = "User email", type = "string", defaultValue = "test@test.test")
    private String email;

    @Schema(name = "password", description = "User password", type = "string", defaultValue = "1234")
    @NotBlank(message = Constants.PASSWORD_CANNOT_BE_BLANK_OR_NULL_VALIDATION_EXCEPTION)
    private String password;

}
