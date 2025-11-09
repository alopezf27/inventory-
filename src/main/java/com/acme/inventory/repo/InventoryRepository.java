package com.acme.inventory.repo;
import com.acme.inventory.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
public interface InventoryRepository extends JpaRepository<Inventory, Long> { }
