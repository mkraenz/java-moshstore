package eu.kraenz.moshstore.controllers;

import eu.kraenz.moshstore.dtos.CreateProductDto;
import eu.kraenz.moshstore.dtos.ProductDto;
import eu.kraenz.moshstore.dtos.UpdateProductDto;
import eu.kraenz.moshstore.mappers.ProductMapper;
import eu.kraenz.moshstore.repositories.CategoryRepository;
import eu.kraenz.moshstore.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@AllArgsConstructor
@RestController
@RequestMapping("/products")
class ProductController {
  private final ProductRepository productRepository;
  private final ProductMapper productMapper;
  private final CategoryRepository categoryRepository;

  @GetMapping
  public Iterable<ProductDto> findMany(
      @RequestParam(name = "categoryId", required = false) Byte categoryId) {
    var products =
        categoryId == null
            ? productRepository.findAll()
            : productRepository.findByCategoryId(categoryId);
    return products.stream().map(productMapper::toDto).toList();
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductDto> findOne(@PathVariable long id) {
    var product = productRepository.findById(id).orElse(null);
    if (product == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(productMapper.toDto(product));
  }

  @PostMapping
  public ResponseEntity<ProductDto> create(
      @RequestBody CreateProductDto createDto, UriComponentsBuilder uriBuilder) {
    var category = categoryRepository.findById(createDto.getCategoryId()).orElse(null);
    if (category == null) {
      return ResponseEntity.unprocessableEntity().build();
    }
    var product = productMapper.toEntity(createDto);
    product.setCategory(category);
    productRepository.save(product);
    var uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
    return ResponseEntity.created(uri).body(productMapper.toDto(product));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProductDto> update(
      @PathVariable(name = "id") Long id, @RequestBody UpdateProductDto updateDto) {
    var product = productRepository.findById(id).orElse(null);
    if (product == null) {
      return ResponseEntity.notFound().build();
    }
    productMapper.update(product, updateDto);
    var category = categoryRepository.findById(updateDto.getCategoryId()).orElse(null);
    if (category == null) {
      return ResponseEntity.unprocessableEntity().build();
    }
    product.setCategory(category);
    productRepository.save(product);
    return ResponseEntity.ok(productMapper.toDto(product));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
    var product = productRepository.findById(id).orElse(null);
    if (product == null) {
      return ResponseEntity.notFound().build();
    }
    productRepository.delete(product);
    return ResponseEntity.noContent().build();
  }
}
