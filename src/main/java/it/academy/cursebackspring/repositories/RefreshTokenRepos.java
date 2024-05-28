package it.academy.cursebackspring.repositories;

import it.academy.cursebackspring.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepos extends JpaRepository<RefreshToken, String> {

}
