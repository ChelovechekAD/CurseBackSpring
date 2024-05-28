package it.academy.cursebackspring.repositories;

import it.academy.cursebackspring.models.CartItem;
import it.academy.cursebackspring.models.embedded.CartItemPK;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepos extends CrudRepository<CartItem, CartItemPK> {

    List<CartItem> findCartItemsByCartItemPK_UserId(Long userId);

    void deleteAllByCartItemPK_UserId(Long userId);

}
