package it.academy.cursebackspring.repositories;

import it.academy.cursebackspring.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepos extends JpaRepository<Category, Long> {

    Optional<Category> getCategoryByCategoryName(String name);

    boolean existsByCategoryName(String name);


}
