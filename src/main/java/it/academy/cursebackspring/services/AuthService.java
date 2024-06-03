package it.academy.cursebackspring.services;

import it.academy.cursebackspring.dto.request.LoginUserDTO;
import it.academy.cursebackspring.dto.request.RegUserDTO;
import it.academy.cursebackspring.dto.response.LoginUserJwtDTO;

public interface AuthService {
    void regUser(RegUserDTO regUserDTO);

    LoginUserJwtDTO loginUser(LoginUserDTO loginUserDTO);

    LoginUserJwtDTO reLoginUser(String refreshToken);

}
