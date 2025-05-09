package org.example.gestion_stock.repository;

import org.example.gestion_stock.model.Medicin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicinRepository extends JpaRepository<Medicin, Long> {
    List<Medicin> findByUserId(String userId); // 👈 Ajout important
}
