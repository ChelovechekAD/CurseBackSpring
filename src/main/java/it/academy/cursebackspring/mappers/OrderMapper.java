package it.academy.cursebackspring.mappers;

import it.academy.cursebackspring.dto.request.CreateOrderDTO;
import it.academy.cursebackspring.dto.response.OrderDTO;
import it.academy.cursebackspring.models.Order;
import it.academy.cursebackspring.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mappings({
            @Mapping(target = "createdAt", ignore = true),
            @Mapping(target = "orderStatus", ignore = true),
            @Mapping(target = "user", source = "user")
    })
    Order toNewEntity(CreateOrderDTO dto, User user);

    @Mappings({
            @Mapping(source = "order.createdAt", target = "date")
    })
    OrderDTO toOrderDTO(Order order, Long countOfItems);

}
