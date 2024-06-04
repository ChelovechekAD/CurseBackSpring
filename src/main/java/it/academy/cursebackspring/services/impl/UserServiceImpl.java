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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(propagation = Propagation.REQUIRED)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepos userRepos;

    /**
     * Save a new user.
     *
     * @param user user object
     */
    @Override
    public void saveUser(User user) {
        if (userRepos.existsUserByEmail(user.getEmail())) {
            throw new UserExistException();
        }
        userRepos.save(user);
    }

    /**
     * Delete the user and all of their reviews, orders and cart items.
     *
     * @param id user id
     */
    @Override
    public void deleteUser(Long id) {
        userRepos.deleteUserById(id);
    }

    /**
     * Update user information.
     *
     * @param dto new user information
     */
    @Override
    public void updateUser(UpdateUserDTO dto) {
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
    @Override
    public UserDTO getUserById(Long id) {
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
    @Override
    public UsersDTO getUsersPage(RequestDataDetailsDTO requestDataDetailsDTO) {
        Page<User> userPage = userRepos.findAll(PageRequest.of(requestDataDetailsDTO.getPageNum() - 1,
                requestDataDetailsDTO.getCountPerPage()));
        return UserMapper.INSTANCE.toUsersDTOFromEntityList(userPage, userPage.getTotalElements());
    }

    private User getUserByEmail(String email) {
        return userRepos.findUserByEmail(email)
                .orElseThrow(UserNotFoundException::new);

    }

    @Override
    public UserDetailsService userDetailsService() {
        return this::getUserByEmail;
    }

    @Override
    public UserDTO getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return UserMapper.INSTANCE.toDTOFromEntity(getUserByEmail(email));
    }
}
