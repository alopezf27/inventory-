package com.acme.inventory.repo;
import com.acme.inventory.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CompanyRepository extends JpaRepository<Company, Long> { }
