package it.academy.cursebackspring.services.impl;

import it.academy.cursebackspring.exceptions.UserNotFoundException;
import it.academy.cursebackspring.models.User;
import it.academy.cursebackspring.repositories.UserRepos;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepos userRepos;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepos.findUserByEmail(email)
                .orElseThrow(UserNotFoundException::new);
        user.setPassword("");
        return user;
    }
}
