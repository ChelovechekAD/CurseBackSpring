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

    void deleteUser(Long id);

    void updateUser(UpdateUserDTO dto);

    UsersDTO getUsersPage(RequestDataDetailsDTO requestDataDetailsDTO);

    UserDTO getUserById(Long id);

    void saveUser(User user);

    UserDetailsService userDetailsService();

    UserDTO getCurrentUser();

}
