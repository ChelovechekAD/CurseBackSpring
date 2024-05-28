package it.academy.cursebackspring.mappers;

import it.academy.cursebackspring.dto.request.OrderItemDTO;
import it.academy.cursebackspring.dto.response.OrderItemProductDTO;
import it.academy.cursebackspring.dto.response.OrderItemsDTO;
import it.academy.cursebackspring.models.Order;
import it.academy.cursebackspring.models.OrderItem;
import it.academy.cursebackspring.models.Product;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface OrderItemMapper {

    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

    @Mappings({
            @Mapping(source = "order", target = "orderItemPK.order"),
            @Mapping(source = "product", target = "orderItemPK.product"),
            @Mapping(source = "orderItemDTO.price", target = "price"),
            @Mapping(source = "orderItemDTO.quantity", target = "count")
    })
    OrderItem toEntity(OrderItemDTO orderItemDTO, Order order, Product product);

    @Mappings({
            @Mapping(source = "orderItem.price", target = "price"),
            @Mapping(source = "orderItem.orderItemPK.product.id", target = "productId"),
            @Mapping(source = "orderItem.orderItemPK.product.name", target = "name"),
            @Mapping(source = "orderItem.orderItemPK.product.imageLink", target = "imageLink")
    })
    OrderItemProductDTO toOrderItemProductDTO(OrderItem orderItem);

    default OrderItemsDTO toOrderItemsDTO(List<OrderItem> list, Long totalCount) {
        List<OrderItemProductDTO> outList = list.stream()
                .map(OrderItemMapper.INSTANCE::toOrderItemProductDTO)
                .toList();
        return new OrderItemsDTO(outList, totalCount);
    }

}
