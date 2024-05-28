package it.academy.cursebackspring.models;

import it.academy.cursebackspring.models.embedded.CartItemPK;
import it.academy.cursebackspring.utilities.Constants;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
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
@Entity
@Table(name = "cart")
public class CartItem implements Serializable {

    @EmbeddedId
    @NotNull
    private CartItemPK cartItemPK;

    @Column(nullable = false, name = "quantity")
    @Min(value = 1, message = Constants.QUANTITY_CANNOT_BE_LESS_THAN_VALIDATION_EXCEPTION)
    private int quantity;


}
