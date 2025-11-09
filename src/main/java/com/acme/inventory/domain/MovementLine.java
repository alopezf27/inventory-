package com.acme.inventory.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity @Table(name="movement_line")
@Getter @Setter @NoArgsConstructor
public class MovementLine {
  public enum Type { IN, OUT, TRANSFER }

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="document_id", nullable=false)
  private MovementDocument document;

  @Enumerated(EnumType.STRING)
  private Type type;

  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="product_id", nullable=false)
  private Product product;

  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="from_warehouse_id")
  private Warehouse fromWarehouse;

  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="to_warehouse_id")
  private Warehouse toWarehouse;

  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="unit_id", nullable=false)
  private Unit unit;

  @Column(nullable=false, precision=18, scale=4)
  private BigDecimal quantity;

  @Column(precision=18, scale=4)
  private BigDecimal unitCost;
}
