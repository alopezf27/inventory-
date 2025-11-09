package com.acme.inventory.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="supplier", uniqueConstraints=@UniqueConstraint(columnNames={"company_id","name"}))
@Getter @Setter @NoArgsConstructor
public class Supplier {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="company_id", nullable=false)
  private Company company;

  @Column(nullable=false) private String name;
  private String contactEmail;
  private String phone;
  @Column(nullable=false) private boolean isActive = true;
}
