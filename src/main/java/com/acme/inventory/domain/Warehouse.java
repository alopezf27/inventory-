package com.acme.inventory.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="warehouse", uniqueConstraints=@UniqueConstraint(columnNames={"company_id","code"}))
@Getter @Setter @NoArgsConstructor
public class Warehouse {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="company_id", nullable=false)
  private Company company;

  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="branch_id", nullable=false)
  private Branch branch;

  @Column(nullable=false) private String code;
  @Column(nullable=false) private String name;
  private String location;
  @Column(nullable=false) private boolean allowNegativeStock = false;
  @Column(nullable=false) private boolean isActive = true;
}
