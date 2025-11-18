package eu.kraenz.moshstore.mappers;

import eu.kraenz.moshstore.dtos.ProductDto;
import eu.kraenz.moshstore.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
  @Mapping(source = "category.id", target = "categoryId")
  ProductDto toDto(Product product);
}
