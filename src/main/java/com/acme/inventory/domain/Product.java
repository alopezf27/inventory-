package com.acme.inventory.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity @Table(name="product", uniqueConstraints=@UniqueConstraint(columnNames={"company_id","sku"}))
@Getter @Setter @NoArgsConstructor
public class Product {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="company_id", nullable=false)
  private Company company;

  @Column(nullable=false) private String sku;
  @Column(nullable=false) private String name;
  private String description;

  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="unit_id", nullable=false)
  private Unit unit;

  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="supplier_id")
  private Supplier supplier;

  @Column(nullable=false) private boolean isActive = true;

  @Column(nullable=false) private Instant createdAt = Instant.now();
}
