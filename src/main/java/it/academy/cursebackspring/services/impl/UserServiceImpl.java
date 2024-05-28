package it.academy.cursebackspring.services.impl;

import it.academy.cursebackspring.dto.request.RequestDataDetailsDTO;
import it.academy.cursebackspring.dto.request.UpdateUserDTO;
import it.academy.cursebackspring.dto.response.UserDTO;
import it.academy.cursebackspring.dto.response.UsersDTO;
import it.academy.cursebackspring.exceptions.UserExistException;
import it.academy.cursebackspring.exceptions.UserNotFoundException;
import it.academy.cursebackspring.mappers.UserMapper;
import it.academy.cursebackspring.models.User;
import it.academy.cursebackspring.repositories.UserRepos;
import it.academy.cursebackspring.services.UserService;
import it.academy.cursebackspring.utilities.Constants;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
@Validated
public class UserServiceImpl implements UserService {

    private final UserRepos userRepos;

    /**
     * Save a new user.
     *
     * @param user user object
     */
    public void saveUser(User user) {
        Optional.of(userRepos.existsUserByEmail(user.getEmail()))
                .filter(p -> !p)
                .orElseThrow(UserExistException::new);
        userRepos.save(user);
    }

    /**
     * Delete the user and all of their reviews, orders and cart items.
     *
     * @param id user id
     */
    public void deleteUser(@Valid @Min(value = 1, message = Constants.USER_ID_VALIDATION_EXCEPTION) Long id) {
        userRepos.deleteUserById(id);
    }

    /**
     * Update user information.
     *
     * @param dto new user information
     */
    public void updateUser(@Valid UpdateUserDTO dto) {
        User user = userRepos.findById(dto.getId())
                .orElseThrow(UserNotFoundException::new);
        UserMapper.mergeEntityAndDTO(user, dto);
        userRepos.save(user);
    }

    /**
     * Get the user by id.
     *
     * @param id user id
     * @return dto which contains specific user information
     */
    public UserDTO getUserById(@Valid @Min(value = 1, message = Constants.USER_ID_VALIDATION_EXCEPTION) Long id) {
        User user = userRepos.findById(id)
                .orElseThrow(UserNotFoundException::new);
        return UserMapper.INSTANCE.toDTOFromEntity(user);
    }

    /**
     * Get the page of users by id.
     *
     * @param requestDataDetailsDTO dto with page num and count per page.
     * @return list of dto which contains specific user information
     */
    public UsersDTO getUsersPage(@Valid RequestDataDetailsDTO requestDataDetailsDTO) {
        Page<User> userPage = userRepos.findAll(PageRequest.of(requestDataDetailsDTO.getPageNum(),
                requestDataDetailsDTO.getCountPerPage()));
        return UserMapper.INSTANCE.toUsersDTOFromEntityList(userPage, userPage.getTotalElements());
    }

    private User getUserByEmail(@Valid @Email String email) {
        return userRepos.findUserByEmail(email)
                .orElseThrow(UserNotFoundException::new);

    }

    public UserDetailsService userDetailsService() {
        return this::getUserByEmail;
    }

    public UserDTO getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return UserMapper.INSTANCE.toDTOFromEntity(getUserByEmail(email));
    }
}
