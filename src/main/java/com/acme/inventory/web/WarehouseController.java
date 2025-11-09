package com.acme.inventory.web;

import com.acme.inventory.domain.Warehouse;
import com.acme.inventory.repo.BranchRepository;
import com.acme.inventory.repo.CompanyRepository;
import com.acme.inventory.repo.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor

public class WarehouseController {
  private final WarehouseRepository repo;
  private final CompanyRepository companyRepo;
  private final BranchRepository branchRepo;

  public record WarehouseDto(Long id, String code, String name){}

  @GetMapping
  public List<WarehouseDto> all(){
    return repo.findAll().stream()
        .map(w -> new WarehouseDto(w.getId(), w.getCode(), w.getName()))
        .collect(Collectors.toList());
  }

  @PostMapping
  public ResponseEntity<WarehouseDto> create(@RequestBody WarehouseDto in){
    Warehouse w = new Warehouse();
    w.setCompany(companyRepo.findById(1L).orElseThrow());
    w.setBranch(branchRepo.findById(1L).orElseThrow());
    w.setCode(in.code());
    w.setName(in.name());
    w = repo.save(w);
    return ResponseEntity.ok(new WarehouseDto(w.getId(), w.getCode(), w.getName()));
  }
}