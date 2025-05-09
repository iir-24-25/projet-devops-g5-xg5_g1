package org.example.gestion_stock.repository;

import org.example.gestion_stock.model.User;
import org.example.gestion_stock.model.role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    List<User> findByRole(role role);
}