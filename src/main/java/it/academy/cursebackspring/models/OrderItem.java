package it.academy.cursebackspring.models;

import it.academy.cursebackspring.models.embedded.OrderItemPK;
import it.academy.cursebackspring.utilities.Constants;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
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
@Table(name = "order_item")
public class OrderItem implements Serializable {

    @EmbeddedId
    private OrderItemPK orderItemPK;

    @Column(nullable = false)
    @Min(value = 1, message = Constants.COUNT_CANNOT_BE_LESS_THAN_VALIDATION_EXCEPTION)
    private Long count;

    @Column(nullable = false)
    @DecimalMin(value = "0.01", message = Constants.PRICE_CANNOT_BE_LESS_THAN_VALIDATION_EXCEPTION)
    private Double price;
}
