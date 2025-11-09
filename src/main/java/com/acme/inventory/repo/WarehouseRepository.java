package com.acme.inventory.repo;
import com.acme.inventory.domain.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> { }
