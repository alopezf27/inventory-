package com.acme.inventory.service;

import com.acme.inventory.domain.*;
import com.acme.inventory.repo.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class MovementService {
  private final MovementDocumentRepository docRepo;
  private final InventoryRepository invRepo;
  private final WarehouseRepository whRepo;

  @Transactional
  public MovementDocument postDocument(Long documentId, String postedBy) {
    MovementDocument doc = docRepo.findById(documentId)
      .orElseThrow(() -> new EntityNotFoundException("Documento no encontrado"));

    if (doc.getStatus() == MovementDocument.Status.POSTED) return doc;
    if (doc.getLines() == null || doc.getLines().isEmpty())
      throw new IllegalStateException("No se puede postear un documento sin líneas");

    doc.getLines().forEach(line -> {
      if (line.getType() == MovementLine.Type.OUT || line.getType() == MovementLine.Type.TRANSFER) {
        Long p = line.getProduct().getId();
        Long w = line.getFromWarehouse().getId();
        Inventory inv = invRepo.findAll().stream()
            .filter(i -> i.getProduct().getId().equals(p) && i.getWarehouse().getId().equals(w))
            .findFirst()
            .orElseGet(() -> {
              Inventory i = new Inventory();
              i.setProduct(line.getProduct());
              i.setWarehouse(line.getFromWarehouse());
              return invRepo.save(i);
            });
        BigDecimal newQty = inv.getQuantity().subtract(line.getQuantity());
        Warehouse wh = whRepo.findById(w).orElseThrow();
        if (!wh.isAllowNegativeStock() && newQty.signum() < 0)
          throw new IllegalStateException("Stock negativo no permitido en bodega " + w);
        inv.setQuantity(newQty);
        inv.setUpdatedAt(Instant.now());
        invRepo.save(inv);
      }
      if (line.getType() == MovementLine.Type.IN || line.getType() == MovementLine.Type.TRANSFER) {
        Long p = line.getProduct().getId();
        Long w = line.getToWarehouse().getId();
        Inventory inv = invRepo.findAll().stream()
            .filter(i -> i.getProduct().getId().equals(p) && i.getWarehouse().getId().equals(w))
            .findFirst()
            .orElseGet(() -> {
              Inventory i = new Inventory();
              i.setProduct(line.getProduct());
              i.setWarehouse(line.getToWarehouse());
              return invRepo.save(i);
            });
        inv.setQuantity(inv.getQuantity().add(line.getQuantity()));
        inv.setUpdatedAt(Instant.now());
        invRepo.save(inv);
      }
    });

    doc.setStatus(MovementDocument.Status.POSTED);
    doc.setPostedAt(Instant.now());
    doc.setPostedBy(postedBy);
    return docRepo.save(doc);
  }
}
