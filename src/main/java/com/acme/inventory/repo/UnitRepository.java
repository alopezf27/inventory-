package com.acme.inventory.repo;
import com.acme.inventory.domain.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UnitRepository extends JpaRepository<Unit, Long> { }
