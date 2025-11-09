package com.acme.inventory.web;

import com.acme.inventory.domain.MovementDocument;
import com.acme.inventory.domain.MovementLine;
import com.acme.inventory.repo.*;
import com.acme.inventory.service.MovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/movements")
@RequiredArgsConstructor

public class MovementController {
  private final MovementDocumentRepository docRepo;
  private final CompanyRepository companyRepo;
  private final BranchRepository branchRepo;
  private final ProductRepository productRepo;
  private final WarehouseRepository warehouseRepo;
  private final UnitRepository unitRepo;
  private final MovementService service;

  @PostMapping
  public ResponseEntity<Map<String,Object>> createDoc(@RequestBody Map<String,Object> body){
    MovementDocument doc = new MovementDocument();
    Long companyId = ((Number)body.getOrDefault("companyId", 1)).longValue();
    Long branchId = ((Number)body.getOrDefault("branchId", 1)).longValue();
    String ref = (String) body.getOrDefault("reference","WEB");

    doc.setCompany(companyRepo.findById(companyId).orElseThrow());
    doc.setBranch(branchRepo.findById(branchId).orElseThrow());
    doc.setReference(ref);
    doc = docRepo.save(doc);
    return ResponseEntity.ok(Map.of("id", doc.getId()));
  }

  @PostMapping("/{id}/lines")
  public ResponseEntity<Map<String,Object>> addLine(@PathVariable Long id, @RequestBody Map<String,Object> body){
    MovementDocument doc = docRepo.findById(id).orElseThrow();

    MovementLine line = new MovementLine();
    line.setDocument(doc);
    line.setType(MovementLine.Type.valueOf((String) body.get("type")));
    line.setProduct(productRepo.findById(((Number)body.get("productId")).longValue()).orElseThrow());
    if (body.get("fromWarehouseId")!=null) {
      line.setFromWarehouse(warehouseRepo.findById(((Number)body.get("fromWarehouseId")).longValue()).orElseThrow());
    }
    if (body.get("toWarehouseId")!=null) {
      line.setToWarehouse(warehouseRepo.findById(((Number)body.get("toWarehouseId")).longValue()).orElseThrow());
    }
    line.setUnit(unitRepo.findById(((Number)body.getOrDefault("unitId",1)).longValue()).orElseThrow());
    line.setQuantity(new BigDecimal(body.get("quantity").toString()));
    if (body.get("unitCost")!=null) line.setUnitCost(new BigDecimal(body.get("unitCost").toString()));

    doc.getLines().add(line);
    line = docRepo.save(doc).getLines().get(doc.getLines().size()-1);
    return ResponseEntity.ok(Map.of(
        "id", line.getId(),
        "type", line.getType().name(),
        "productId", line.getProduct().getId(),
        "fromWarehouseId", line.getFromWarehouse()==null? null : line.getFromWarehouse().getId(),
        "toWarehouseId", line.getToWarehouse()==null? null : line.getToWarehouse().getId(),
        "quantity", line.getQuantity()
    ));
  }

  @PostMapping("/{id}/post")
  public ResponseEntity<Map<String,Object>> post(@PathVariable Long id, @RequestParam(defaultValue="web") String user){
    MovementDocument d = service.postDocument(id, user);
    return ResponseEntity.ok(Map.of("id", d.getId(), "status", d.getStatus().name()));
  }
}