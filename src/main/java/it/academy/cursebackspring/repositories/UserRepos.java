package it.academy.cursebackspring.repositories;

import it.academy.cursebackspring.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepos extends JpaRepository<User, Long>, UserCustomRepos {

    Optional<User> findUserByEmail(String email);

    boolean existsUserByEmail(String email);

}
