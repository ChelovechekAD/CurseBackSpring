package it.academy.cursebackspring.services.impl;

import it.academy.cursebackspring.components.JwtUtils;
import it.academy.cursebackspring.dto.response.LoginUserJwtDTO;
import it.academy.cursebackspring.exceptions.RefreshTokenInvalidException;
import it.academy.cursebackspring.exceptions.UserNotFoundException;
import it.academy.cursebackspring.mappers.JwtMapper;
import it.academy.cursebackspring.mappers.UserMapper;
import it.academy.cursebackspring.models.User;
import it.academy.cursebackspring.repositories.RefreshTokenRepos;
import it.academy.cursebackspring.repositories.UserRepos;
import it.academy.cursebackspring.services.JwtService;
import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
@Validated
public class JwtServiceImpl implements JwtService {

    private final UserRepos userRepos;
    private final RefreshTokenRepos refreshTokenRepos;
    private final JwtUtils jwtUtils;

    /**
     * Generate a new pair of tokens.
     *
     * @param user user object
     * @return dto with access, refresh tokens and user data
     */
    @Override
    public LoginUserJwtDTO generateNewPairOfTokens(@Valid User user) {
        String accessToken = jwtUtils.generateAccessToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);
        saveRefreshToken(user.getEmail(), refreshToken);
        return JwtMapper.INSTANCE.toDTO(UserMapper.INSTANCE.toDTOFromEntity(user), accessToken, refreshToken);
    }

    /**
     * Save refresh token in database. If user with this email is not found,
     * a <code>UserNotFoundException</code> is thrown.
     *
     * @param email user email
     * @param token token to be saved
     */

    private void saveRefreshToken(@Valid @Email String email,
                                  @Valid @NotBlank(message = Constants.TOKEN_MUST_NOT_BE_BLANK_VALIDATION_EXCEPTION) String token) {
        if (!userRepos.existsUserByEmail(email)) {
            throw new UserNotFoundException();
        }
        refreshTokenRepos.save(JwtMapper.INSTANCE.createEntity(email, token));
    }

    /**
     * Generate a new pair of tokens based on old refresh token. If user with extracted from token email is not found,
     * a <code>UserNotFoundException</code> is thrown.
     *
     * @param refreshToken old refresh token
     * @return dto with access, refresh tokens and user data
     */
    @Override
    public LoginUserJwtDTO regenerateTokens(
            @Valid @NotBlank(message = Constants.TOKEN_MUST_NOT_BE_BLANK_VALIDATION_EXCEPTION) String refreshToken) {
        if (!jwtUtils.validateRefreshToken(refreshToken)) {
            throw new RefreshTokenInvalidException();
        }
        String email = jwtUtils.getRefreshClaims(refreshToken).getSubject();
        User user = userRepos.findUserByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        return generateNewPairOfTokens(user);
    }

}
