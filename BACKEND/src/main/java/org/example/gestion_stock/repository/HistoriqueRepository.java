package org.example.gestion_stock.repository;

import org.example.gestion_stock.model.HistoriqueMedicin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoriqueRepository extends JpaRepository<HistoriqueMedicin, Long> {
}
