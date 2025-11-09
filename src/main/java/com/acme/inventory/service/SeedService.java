package com.acme.inventory.service;

import com.acme.inventory.domain.*;
import com.acme.inventory.repo.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeedService {
  private final CompanyRepository companyRepository;
  private final BranchRepository branchRepository;
  private final UnitRepository unitRepository;
  private final WarehouseRepository warehouseRepository;
  private final ProductRepository productRepository;

  @PostConstruct
  public void seed(){
    if (companyRepository.count() > 0) return;

    Company c = new Company();
    c.setCode("ACME");
    c.setName("ACME SA");
    companyRepository.save(c);

    Branch b = new Branch();
    b.setCompany(c);
    b.setCode("GUA");
    b.setName("Guatemala");
    branchRepository.save(b);

    Unit u = new Unit();
    u.setCode("EA");
    u.setName("Unidad");
    unitRepository.save(u);

    Warehouse w1 = new Warehouse();
    w1.setCompany(c); w1.setBranch(b);
    w1.setCode("CEN"); w1.setName("Central");
    warehouseRepository.save(w1);

    Warehouse w2 = new Warehouse();
    w2.setCompany(c); w2.setBranch(b);
    w2.setCode("SEC"); w2.setName("Secundaria");
    warehouseRepository.save(w2);

    Product p = new Product();
    p.setCompany(c); p.setSku("P-001"); p.setName("Producto demo");
    p.setUnit(u);
    productRepository.save(p);
  }
}
