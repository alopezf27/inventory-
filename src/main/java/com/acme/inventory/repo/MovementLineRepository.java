package com.acme.inventory.repo;
import com.acme.inventory.domain.MovementLine;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MovementLineRepository extends JpaRepository<MovementLine, Long> { }
