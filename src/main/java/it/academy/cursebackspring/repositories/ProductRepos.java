package it.academy.cursebackspring.repositories;

import it.academy.cursebackspring.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepos extends JpaRepository<Product, Long> {

    Page<Product> findAllByCategoryId(Pageable pageable, Long categoryId);

    boolean existsProductByCategoryId(Long categoryId);

    Long countByCategoryId(Long id);

    void deleteAllByCategory_Id(Long categoryId);
}
