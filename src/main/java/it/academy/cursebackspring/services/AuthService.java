package it.academy.cursebackspring.services;

import it.academy.cursebackspring.dto.request.LoginUserDTO;
import it.academy.cursebackspring.dto.request.RegUserDTO;
import it.academy.cursebackspring.dto.response.LoginUserJwtDTO;
import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public interface AuthService {
    void regUser(@Valid RegUserDTO regUserDTO);

    LoginUserJwtDTO loginUser(@Valid LoginUserDTO loginUserDTO);

    LoginUserJwtDTO reLoginUser(
            @Valid @NotBlank(message = Constants.TOKEN_MUST_NOT_BE_BLANK_VALIDATION_EXCEPTION) String refreshToken);

}
