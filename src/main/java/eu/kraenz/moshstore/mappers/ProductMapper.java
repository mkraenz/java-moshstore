package eu.kraenz.moshstore.mappers;

import eu.kraenz.moshstore.dtos.CreateProductDto;
import eu.kraenz.moshstore.dtos.ProductDto;
import eu.kraenz.moshstore.dtos.UpdateProductDto;
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
