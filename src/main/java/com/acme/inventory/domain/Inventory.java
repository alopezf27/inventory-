package com.acme.inventory.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity @Table(name="inventory", uniqueConstraints=@UniqueConstraint(columnNames={"product_id","warehouse_id"}))
@Getter @Setter @NoArgsConstructor
public class Inventory {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="product_id", nullable=false)
  private Product product;

  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="warehouse_id", nullable=false)
  private Warehouse warehouse;

  @Column(nullable=false, precision=18, scale=4)
  private BigDecimal quantity = BigDecimal.ZERO;

  @Column(nullable=false, precision=18, scale=4)
  private BigDecimal reserved = BigDecimal.ZERO;

  @Column(nullable=false)
  private Instant updatedAt = Instant.now();
}
