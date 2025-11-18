package eu.kraenz.moshstore.controllers;

import eu.kraenz.moshstore.dtos.ProductDto;
import eu.kraenz.moshstore.entities.Product;
import eu.kraenz.moshstore.mappers.ProductMapper;
import eu.kraenz.moshstore.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/products")
class ProductController {
  private final ProductRepository productRepository;
  private final ProductMapper productMapper;

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
}
