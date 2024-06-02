package it.academy.cursebackspring.services;

import it.academy.cursebackspring.dto.request.RequestDataDetailsDTO;
import it.academy.cursebackspring.dto.request.UpdateUserDTO;
import it.academy.cursebackspring.dto.response.UserDTO;
import it.academy.cursebackspring.dto.response.UsersDTO;
import it.academy.cursebackspring.exceptions.UserExistException;
import it.academy.cursebackspring.exceptions.UserNotFoundException;
import it.academy.cursebackspring.models.User;
import it.academy.cursebackspring.models.embedded.Address;
import it.academy.cursebackspring.repositories.UserRepos;
import it.academy.cursebackspring.services.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTests {

    @Mock
    UserRepos userRepos;

    @InjectMocks
    UserServiceImpl userService;

    @ParameterizedTest
    @MethodSource("provideSaveUserData")
    public void saveUserTest(User user, boolean isValid, String message) {
        when(userRepos.existsUserByEmail(user.getEmail())).then(invocation ->
                invocation.getArgument(0).equals("Existed@email.com"));
        if (isValid) {
            userService.saveUser(user);
            verify(userRepos, times(1)).save(user);
        } else {
            Exception exception = Assertions.assertThrows(UserExistException.class, () -> userService.saveUser(user));
            Assertions.assertEquals(message, exception.getMessage());
        }
    }

    @ParameterizedTest
    @MethodSource("provideUpdateUserData")
    public void updateUserTest(UpdateUserDTO dto, boolean isValid, String message) {
        User user = new User();
        when(userRepos.findById(any())).then(invocation ->
                (invocation.getArgument(0, Long.class) == 1L) ? Optional.of(user) : Optional.empty());
        if (isValid) {
            userService.updateUser(dto);
            verify(userRepos, times(1)).save(any());
            Assertions.assertEquals(
                    List.of(user.getAddress().getCity(),
                            user.getAddress().getBuilding(),
                            user.getAddress().getStreet(),
                            user.getName(),
                            user.getSurname()),
                    List.of(dto.getCity(),
                            dto.getBuilding(),
                            dto.getStreet(),
                            dto.getName(),
                            dto.getSurname()));
        } else {
            Exception exception = Assertions.assertThrows(UserNotFoundException.class, () -> userService.updateUser(dto));
            Assertions.assertEquals(message, exception.getMessage());
        }
    }

    @Test
    public void deleteUserTest() {
        userService.deleteUser(1L);
        verify(userRepos, times(1)).deleteUserById(any());
    }

    @Test
    public void getUsersPageTest() {
        RequestDataDetailsDTO dto = new RequestDataDetailsDTO(10, 0);
        when(userRepos.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(
                List.of(new User(), new User())
        ));
        UsersDTO actualDto = userService.getUsersPage(dto);
        Assertions.assertEquals(List.of(2, 2L), List.of(actualDto.getUserDTOList().size(), actualDto.getCount()));
    }

    @ParameterizedTest
    @MethodSource("provideGetUserByIdData")
    public void getUserById(Long userId, boolean isValid, String message){
        User user = new User(1L, "123", "123", "Existed@email.com",
                "123", new Address(), "+3623", new HashSet<>());
        when(userRepos.findById(any(Long.class))).then(invocation ->
                (invocation.getArgument(0, Long.class) == 1L) ? Optional.of(user) : Optional.empty());
        if (isValid) {
            UserDTO userDTO = userService.getUserById(userId);
            Assertions.assertEquals(
                    List.of(
                            user.getId(), user.getEmail(), user.getName(),
                            user.getSurname(), user.getPhoneNumber()),
                    List.of(
                            userDTO.getId(), userDTO.getEmail(), userDTO.getName(),
                            userDTO.getSurname(), userDTO.getPhoneNumber()));
        } else {
            Exception exception = Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
            Assertions.assertEquals(message, exception.getMessage());
        }
    }

    private static Stream<Arguments> provideSaveUserData() {
        return Stream.of(
                Arguments.of(new User(1L, "123", "123", "Existed@email.com",
                        "123", new Address(), "+3623", new HashSet<>()), false, "User already exist!"),
                Arguments.of(new User(1L, "123", "123", "NewEmail@email.com",
                        "123", new Address(), "+3623", new HashSet<>()), true, null)

        );
    }
    private static Stream<Arguments> provideGetUserByIdData() {
        return Stream.of(
                Arguments.of(1L, true, null),
                Arguments.of(2L, false, "User not found!")

        );
    }
    private static Stream<Arguments> provideUpdateUserData() {
        return Stream.of(
                Arguments.of(new UpdateUserDTO(1L, "123", "123", "Email@email.com",
                        "123", 123), true, null),
                Arguments.of(new UpdateUserDTO(2L, "123", "123", "Email@email.com",
                        "123", 123), false, "User not found!")


        );
    }

}
