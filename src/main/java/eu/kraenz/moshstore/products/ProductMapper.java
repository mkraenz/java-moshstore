package eu.kraenz.moshstore.products;

import eu.kraenz.moshstore.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
  @Mapping(source = "category.id", target = "categoryId")
  ProductDto toDto(Product product);

  @Mapping(target = "category.id", source = "categoryId")
  Product toEntity(CreateProductDto dto);

  void update(@MappingTarget Product product, UpdateProductDto updateDto);
}
