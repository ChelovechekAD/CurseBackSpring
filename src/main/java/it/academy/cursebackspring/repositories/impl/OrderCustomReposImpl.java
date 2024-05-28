package it.academy.cursebackspring.repositories.impl;

import it.academy.cursebackspring.models.Order;
import it.academy.cursebackspring.models.OrderItem;
import it.academy.cursebackspring.models.OrderItem_;
import it.academy.cursebackspring.models.Order_;
import it.academy.cursebackspring.models.embedded.OrderItemPK;
import it.academy.cursebackspring.models.embedded.OrderItemPK_;
import it.academy.cursebackspring.repositories.OrderCustomRepos;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

@Repository
public class OrderCustomReposImpl implements OrderCustomRepos {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void deleteByOrderId(Long orderId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaDelete<OrderItem> cq = cb.createCriteriaDelete(OrderItem.class);
        Root<OrderItem> root = cq.from(OrderItem.class);
        Join<OrderItem, OrderItemPK> pkJoin = root.join(OrderItem_.ORDER_ITEM_PK);
        Join<OrderItemPK, Order> orderJoin = pkJoin.join(OrderItemPK_.ORDER);

        cq.where(cb.equal(orderJoin.get(Order_.ID), orderId));
        entityManager.createQuery(cq).executeUpdate();
        entityManager.remove(orderId);
    }
}
