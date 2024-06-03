package it.academy.cursebackspring.services.impl;


import it.academy.cursebackspring.dto.request.LoginUserDTO;
import it.academy.cursebackspring.dto.request.RegUserDTO;
import it.academy.cursebackspring.dto.response.LoginUserJwtDTO;
import it.academy.cursebackspring.enums.RoleEnum;
import it.academy.cursebackspring.exceptions.PasswordMatchException;
import it.academy.cursebackspring.mappers.UserMapper;
import it.academy.cursebackspring.models.User;
import it.academy.cursebackspring.repositories.RoleRepos;
import it.academy.cursebackspring.services.AuthService;
import it.academy.cursebackspring.services.JwtService;
import it.academy.cursebackspring.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final RoleRepos roleRepos;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    /**
     * User registration
     *
     * @param regUserDTO user data
     */
    @Override
    public void regUser(RegUserDTO regUserDTO) {
        if (!regUserDTO.getPassword().equals(regUserDTO.getPasswordConfirm())) {
            throw new PasswordMatchException();
        }
        User user = UserMapper.INSTANCE.toEntityFromRegDTO(regUserDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoleSet(Set.of(roleRepos.getByRole(RoleEnum.ROLE_DEFAULT_USER)));
        userService.saveUser(user);
    }

    /**
     * User login
     *
     * @param loginUserDTO user data for login
     * @return dto with two keys and user data
     */
    @Override
    public LoginUserJwtDTO loginUser(LoginUserDTO loginUserDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginUserDTO.getEmail(),
                loginUserDTO.getPassword()
        ));
        User user = (User) userService.userDetailsService()
                .loadUserByUsername(loginUserDTO.getEmail());

        if (!passwordEncoder.matches(loginUserDTO.getPassword(), user.getPassword())) {
            throw new PasswordMatchException();
        }

        return jwtService.generateNewPairOfTokens(user);
    }

    /**
     * If user access token expired, this method allow to generate a new pair of tokens based on old refresh token.
     *
     * @param refreshToken - old refresh token
     * @return new pair of tokens and user data
     */
    @Override
    public LoginUserJwtDTO reLoginUser(String refreshToken) {
        return jwtService.regenerateTokens(refreshToken);
    }
}
