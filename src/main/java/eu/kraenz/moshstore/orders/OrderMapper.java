package eu.kraenz.moshstore.orders;

import eu.kraenz.moshstore.entities.Order;
import eu.kraenz.moshstore.entities.OrderItem;
import java.math.BigDecimal;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    imports = {BigDecimal.class})
interface OrderMapper {
  OrderDto toDto(Order order);

  OrderItemDto toOrderItemDto(OrderItem item);
}
