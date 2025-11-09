package com.acme.inventory.web;

import com.acme.inventory.domain.Product;
import com.acme.inventory.repo.CompanyRepository;
import com.acme.inventory.repo.ProductRepository;
import com.acme.inventory.repo.UnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor

public class ProductController {
  private final ProductRepository repo;
  private final CompanyRepository companyRepo;
  private final UnitRepository unitRepo;

  public record ProductDto(Long id, String sku, String name) {}

  @GetMapping
  public List<ProductDto> all() {
    return repo.findAll().stream()
        .map(p -> new ProductDto(p.getId(), p.getSku(), p.getName()))
        .collect(Collectors.toList());
  }

  @PostMapping
  public ResponseEntity<ProductDto> create(@RequestBody ProductDto in){
    Product p = new Product();
    p.setCompany(companyRepo.findById(1L).orElseThrow());
    p.setUnit(unitRepo.findById(1L).orElseThrow());
    p.setSku(in.sku());
    p.setName(in.name());
    p = repo.save(p);
    return ResponseEntity.ok(new ProductDto(p.getId(), p.getSku(), p.getName()));
  }
}