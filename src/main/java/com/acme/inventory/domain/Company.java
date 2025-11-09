package com.acme.inventory.domain;

import jakarta.persistence.*;
import lombok.Getter; import lombok.Setter; import lombok.NoArgsConstructor;

@Entity @Table(name="company")
@Getter @Setter @NoArgsConstructor
public class Company {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable=false, unique=true) private String code;
  @Column(nullable=false) private String name;
  @Column(nullable=false) private String currency = "USD";
  @Column(nullable=false) private boolean isActive = true;
}
