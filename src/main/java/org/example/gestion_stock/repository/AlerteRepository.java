package org.example.gestion_stock.repository;

import org.example.gestion_stock.model.Alerte;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlerteRepository extends JpaRepository<Alerte, Long> {
}
