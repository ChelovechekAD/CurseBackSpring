package it.academy.cursebackspring.services;

import it.academy.cursebackspring.dto.request.LoginUserDTO;
import it.academy.cursebackspring.dto.request.RegUserDTO;
import it.academy.cursebackspring.dto.response.LoginUserJwtDTO;
import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public interface AuthService {
    void regUser(RegUserDTO regUserDTO);

    LoginUserJwtDTO loginUser(LoginUserDTO loginUserDTO);

    LoginUserJwtDTO reLoginUser(String refreshToken);

}
