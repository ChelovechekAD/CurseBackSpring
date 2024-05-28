package it.academy.cursebackspring.models.embedded;

import it.academy.cursebackspring.models.Order;
import it.academy.cursebackspring.models.Product;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemPK implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "order_id")
    @NotNull
    private Order order;

    @ManyToOne
    @JoinColumn(nullable = false, name = "product_id")
    @NotNull
    private Product product;

}
