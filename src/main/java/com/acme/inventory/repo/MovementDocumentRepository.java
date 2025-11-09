package com.acme.inventory.repo;
import com.acme.inventory.domain.MovementDocument;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MovementDocumentRepository extends JpaRepository<MovementDocument, Long> { }
