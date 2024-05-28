package it.academy.cursebackspring.repositories;

import it.academy.cursebackspring.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepos extends JpaRepository<Order, Long>, OrderCustomRepos {

    Page<Order> getOrdersByUserId(Long userId, Pageable pageable);

    void deleteAllByUser_Id(Long userId);

    Long countOrdersByUserId(Long userId);

}
