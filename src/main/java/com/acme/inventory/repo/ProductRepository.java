package com.acme.inventory.repo;

import com.acme.inventory.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Búsqueda por SKU o nombre (contiene, case-insensitive)
    Page<Product> findBySkuContainingIgnoreCaseOrNameContainingIgnoreCase(String sku, String name, Pageable pageable);
}