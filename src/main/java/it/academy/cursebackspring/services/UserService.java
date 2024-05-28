package it.academy.cursebackspring.services;

import it.academy.cursebackspring.dto.request.RequestDataDetailsDTO;
import it.academy.cursebackspring.dto.request.UpdateUserDTO;
import it.academy.cursebackspring.dto.response.UserDTO;
import it.academy.cursebackspring.dto.response.UsersDTO;
import it.academy.cursebackspring.models.User;
import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {

    void deleteUser(@Valid @Min(value = 1, message = Constants.USER_ID_VALIDATION_EXCEPTION) Long id);

    void updateUser(@Valid UpdateUserDTO dto);

    UsersDTO getUsersPage(@Valid RequestDataDetailsDTO requestDataDetailsDTO);

    UserDTO getUserById(@Valid @Min(value = 1, message = Constants.USER_ID_VALIDATION_EXCEPTION) Long id);

    void saveUser(@Valid User user);

    UserDetailsService userDetailsService();

    UserDTO getCurrentUser();

}
