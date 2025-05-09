package org.example.gestion_stock.service;

import org.example.gestion_stock.model.User;
import org.example.gestion_stock.model.role;
import org.example.gestion_stock.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public User register(User user, role role) {
        user.setRole(role);
        user.setPasswordHash(user.getPasswordHash()); // Stockage direct sans hash
        return userRepository.save(user);
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if(user != null && password.equals(user.getPasswordHash())) {
            return user;
        }
        return null;
    }
}