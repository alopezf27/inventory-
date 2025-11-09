package com.acme.inventory.repo;
import com.acme.inventory.domain.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
public interface BranchRepository extends JpaRepository<Branch, Long> { }
