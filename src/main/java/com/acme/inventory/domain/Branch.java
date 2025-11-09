package com.acme.inventory.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="branch", uniqueConstraints=@UniqueConstraint(columnNames={"company_id","code"}))
@Getter @Setter @NoArgsConstructor
public class Branch {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="company_id", nullable=false)
  private Company company;

  @Column(nullable=false) private String code;
  @Column(nullable=false) private String name;
  private String location;
  @Column(nullable=false) private boolean isActive = true;
}
