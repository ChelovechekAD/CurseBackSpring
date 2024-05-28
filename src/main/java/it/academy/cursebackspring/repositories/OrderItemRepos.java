package it.academy.cursebackspring.repositories;

import it.academy.cursebackspring.models.OrderItem;
import it.academy.cursebackspring.models.embedded.OrderItemPK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepos extends CrudRepository<OrderItem, OrderItemPK> {

    Long countOrderItemsByOrderItemPK_OrderId(Long orderId);

    Page<OrderItem> getOrderItemsPageByOrderItemPK_OrderId(Long orderId, Pageable pageable);

    boolean existsByOrderItemPK_ProductId(Long productId);

    boolean existsByOrderItemPK_Product_CategoryId(Long categoryId);

}
