package com.acme.inventory.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.*;

@Entity @Table(name="movement_document")
@Getter @Setter @NoArgsConstructor
public class MovementDocument {
  public enum Status { DRAFT, POSTED, CANCELLED }

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="company_id", nullable=false)
  private Company company;

  @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="branch_id", nullable=false)
  private Branch branch;

  private String reference;

  @Enumerated(EnumType.STRING)
  private Status status = Status.DRAFT;

  private String notes;
  private Instant createdAt = Instant.now();
  private Instant postedAt;
  private String createdBy;
  private String postedBy;

  @OneToMany(mappedBy="document", cascade=CascadeType.ALL, orphanRemoval=true)
  private List<MovementLine> lines = new ArrayList<>();
}
