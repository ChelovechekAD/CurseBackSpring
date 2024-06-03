package it.academy.cursebackspring.services;

import it.academy.cursebackspring.components.JwtUtils;
import it.academy.cursebackspring.dto.response.LoginUserJwtDTO;
import it.academy.cursebackspring.exceptions.UserNotFoundException;
import it.academy.cursebackspring.models.RefreshToken;
import it.academy.cursebackspring.models.User;
import it.academy.cursebackspring.models.embedded.Address;
import it.academy.cursebackspring.repositories.RefreshTokenRepos;
import it.academy.cursebackspring.repositories.UserRepos;
import it.academy.cursebackspring.services.impl.JwtServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@SpringBootTest
public class JwtServiceTest {

    @InjectMocks
    JwtServiceImpl jwtService;
    @Mock
    private UserRepos userRepos;
    @Mock
    private RefreshTokenRepos refreshTokenRepos;
    @Mock
    private JwtUtils jwtUtils;

    private static Stream<Arguments> provideNewPairOfTokensData() {
        return Stream.of(
                Arguments.of(new User(1L, "123", "123", "email@email.com",
                        "123", new Address(), "+3623", new HashSet<>()), true, null),
                Arguments.of(new User(1L, "123", "123", "existedemail@test.ru",
                        "123", new Address(), "+3623", new HashSet<>()), false, "User not found!")
        );
    }

    @ParameterizedTest
    @MethodSource("provideNewPairOfTokensData")
    public void generateNewPairOfTokensTest(User user, boolean isValid, String message) {
        when(jwtUtils.generateAccessToken(any(User.class)))
                .thenReturn("Access token");
        when(jwtUtils.generateRefreshToken(any(User.class)))
                .thenReturn("Refresh token");
        when(userRepos.existsUserByEmail(any())).then(invocation ->
                !invocation.getArgument(0).equals("existedemail@test.ru"));
        if (isValid) {
            ArgumentCaptor<RefreshToken> refreshTokenArgumentCaptor = ArgumentCaptor.forClass(RefreshToken.class);
            LoginUserJwtDTO loginUserJwtDTO = jwtService.generateNewPairOfTokens(user);
            verify(refreshTokenRepos).save(refreshTokenArgumentCaptor.capture());
            RefreshToken actualRefreshTokenObj = refreshTokenArgumentCaptor.getValue();
            Assertions.assertEquals(
                    List.of(
                            user.getEmail(),
                            "Access token",
                            "Refresh token",
                            "Refresh token",
                            user.getEmail()
                    ),
                    List.of(
                            loginUserJwtDTO.getUserDTO().getEmail(),
                            loginUserJwtDTO.getAccessToken(),
                            loginUserJwtDTO.getRefreshToken(),
                            actualRefreshTokenObj.getRefreshToken(),
                            actualRefreshTokenObj.getUserEmail()
                    )
            );
        } else {
            Exception exception = Assertions.assertThrows(UserNotFoundException.class,
                    () -> jwtService.generateNewPairOfTokens(user));
            Assertions.assertEquals(message, exception.getMessage());
        }
    }

}
