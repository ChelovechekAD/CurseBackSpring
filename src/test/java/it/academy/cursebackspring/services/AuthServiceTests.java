package it.academy.cursebackspring.services;

import it.academy.cursebackspring.dto.request.LoginUserDTO;
import it.academy.cursebackspring.dto.request.RegUserDTO;
import it.academy.cursebackspring.dto.response.LoginUserJwtDTO;
import it.academy.cursebackspring.enums.RoleEnum;
import it.academy.cursebackspring.exceptions.PasswordMatchException;
import it.academy.cursebackspring.models.Role;
import it.academy.cursebackspring.models.User;
import it.academy.cursebackspring.repositories.RoleRepos;
import it.academy.cursebackspring.repositories.UserRepos;
import it.academy.cursebackspring.services.impl.AuthServiceImpl;
import it.academy.cursebackspring.services.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthServiceTests {

    @Mock
    UserService userService;

    @Mock
    RoleRepos roleRepos;

    @Mock
    JwtService jwtService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserRepos userRepos;

    @Mock
    UserDetailsServiceImpl userDetailsService;

    @Mock
    AuthenticationManager authenticationManager;

    @InjectMocks
    AuthServiceImpl authService;

    @ParameterizedTest
    @MethodSource("provideRegUserData")
    public void regUserTest(RegUserDTO regUserDTO, boolean isValid, String message) {
        when(roleRepos.getByRole(RoleEnum.ROLE_DEFAULT_USER)).thenReturn(new Role((short)1, RoleEnum.ROLE_DEFAULT_USER));
        if (isValid) {
            ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
            authService.regUser(regUserDTO);
            verify(userService).saveUser(userArgumentCaptor.capture());
            User actualUser = userArgumentCaptor.getValue();
            Assertions.assertNotEquals(regUserDTO.getPassword(), actualUser.getPassword());
            Assertions.assertEquals(List.of(RoleEnum.ROLE_DEFAULT_USER, 1),
                    List.of(actualUser.getRoleSet().stream().toList().get(0).getRole(), actualUser.getRoleSet().size()));
        } else {
            Exception exception = Assertions.assertThrows(PasswordMatchException.class, ()->authService.regUser(regUserDTO));
            Assertions.assertEquals(message, exception.getMessage());
        }
    }

    @ParameterizedTest
    @MethodSource("provideLoginUserData")
    public void loginUserTest(LoginUserDTO dto, boolean isValid, String message) {

        when(userService.userDetailsService()).thenReturn(userDetailsService);
        when(userDetailsService.loadUserByUsername(any())).thenReturn(new User());
        when(jwtService.generateNewPairOfTokens(any(User.class))).thenReturn(new LoginUserJwtDTO());
        when(passwordEncoder.matches(any(), any())).then(invocation -> {
            String rawPassword = invocation.getArgument(0);
            return rawPassword.equals("Actual-password");
        });
        if (isValid) {
            authService.loginUser(dto);
            verify(jwtService, times(1)).generateNewPairOfTokens(any(User.class));
        } else {
            Exception exception = Assertions.assertThrows(PasswordMatchException.class, ()->authService.loginUser(dto));
            Assertions.assertEquals(message, exception.getMessage());
        }
    }

    private static Stream<Arguments> provideRegUserData() {
        return Stream.of(
                Arguments.of(new RegUserDTO("email@email.ru", "Chelovek", "Default",
                        "1234", "1234", null, null, null, "+1234"),
                        true, null),
                Arguments.of(new RegUserDTO("email@email.ru", "Chelovek", "Default",
                        "1234", "1", null, null, null, "+1234"),
                        false, "Password and password confirmation do not match!")
        );
    }
    private static Stream<Arguments> provideLoginUserData() {
        return Stream.of(
                Arguments.of(new LoginUserDTO("email@email.ru", "Actual-password"),
                        true, null),
                Arguments.of(new LoginUserDTO("123@mail.ru", "Wrong-password"),
                        false, "Password and password confirmation do not match!")
        );
    }

}
