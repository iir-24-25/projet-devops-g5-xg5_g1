package org.example.gestion_stock.repository;

import org.example.gestion_stock.model.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
}
