package it.academy.cursebackspring.repositories.impl;

import it.academy.cursebackspring.models.*;
import it.academy.cursebackspring.models.embedded.CartItemPK_;
import it.academy.cursebackspring.models.embedded.OrderItemPK_;
import it.academy.cursebackspring.models.embedded.ReviewPK_;
import it.academy.cursebackspring.repositories.UserCustomRepos;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;

@Repository
public class UserCustomReposImpl implements UserCustomRepos {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void deleteUserById(Long userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaDelete<OrderItem> orderItemCriteriaDelete = cb.createCriteriaDelete(OrderItem.class);

        Subquery<Order> orderSubquery = orderItemCriteriaDelete.subquery(Order.class);
        Root<Order> subRoot = orderSubquery.from(Order.class);
        Join<Order, User> userJoin = subRoot.join(Order_.USER);
        orderSubquery.select(subRoot);
        orderSubquery.where(cb.equal(userJoin.get(User_.ID), userId));

        Root<OrderItem> orderItemRoot = orderItemCriteriaDelete.from(OrderItem.class);
        orderItemCriteriaDelete
                .where(orderItemRoot.get(OrderItem_.ORDER_ITEM_PK).get(OrderItemPK_.ORDER).in(orderSubquery));
        entityManager.createQuery(orderItemCriteriaDelete).executeUpdate();


        CriteriaDelete<Order> orderCriteriaDelete = cb.createCriteriaDelete(Order.class);
        Root<Order> root = orderCriteriaDelete.from(Order.class);
        orderCriteriaDelete.where(cb.equal(root.get(Order_.USER).get(User_.ID), userId));
        entityManager.createQuery(orderCriteriaDelete).executeUpdate();

        CriteriaDelete<CartItem> cartDeleteQuery = cb.createCriteriaDelete(CartItem.class);
        Root<CartItem> root1 = cartDeleteQuery.from(CartItem.class);
        cartDeleteQuery.where(cb.equal(root1.get(CartItem_.CART_ITEM_PK).get(CartItemPK_.USER).get(User_.ID), userId));
        entityManager.createQuery(cartDeleteQuery).executeUpdate();

        CriteriaDelete<Review> reviewDeleteQuery = cb.createCriteriaDelete(Review.class);
        Root<Review> root2 = reviewDeleteQuery.from(Review.class);
        reviewDeleteQuery.where(cb.equal(root2.get(Review_.REVIEW_PK).get(ReviewPK_.USER).get(User_.ID), userId));
        entityManager.createQuery(reviewDeleteQuery).executeUpdate();

        User user = entityManager.find(User.class, userId);
        entityManager.remove(user);
    }
}
