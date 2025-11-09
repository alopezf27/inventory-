package com.acme.inventory.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="unit")
@Getter @Setter @NoArgsConstructor
public class Unit {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable=false, unique=true) private String code;
  @Column(nullable=false) private String name;
}
