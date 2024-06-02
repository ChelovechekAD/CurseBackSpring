package it.academy.cursebackspring.services;

import it.academy.cursebackspring.dto.response.LoginUserJwtDTO;
import it.academy.cursebackspring.models.User;
import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public interface JwtService {

    LoginUserJwtDTO generateNewPairOfTokens(@Valid User user);

    LoginUserJwtDTO regenerateTokens(
            @Valid @NotBlank(message = Constants.TOKEN_MUST_NOT_BE_BLANK_VALIDATION_EXCEPTION) String refreshToken);

}
