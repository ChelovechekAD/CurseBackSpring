package it.academy.cursebackspring.repositories;

import it.academy.cursebackspring.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepos extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    boolean existsProductByCategoryId(Long categoryId);

    void deleteAllByCategory_Id(Long categoryId);
}
