package it.academy.cursebackspring.models.embedded;

import it.academy.cursebackspring.models.Product;
import it.academy.cursebackspring.models.User;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class CartItemPK implements Serializable {
    @JoinColumn(nullable = false, name = "user_id")
    @ManyToOne
    @NotNull
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false, name = "product_id")
    @NotNull
    private Product product;
}
