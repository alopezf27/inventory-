package com.acme.inventory.repo;
import com.acme.inventory.domain.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SupplierRepository extends JpaRepository<Supplier, Long> { }
