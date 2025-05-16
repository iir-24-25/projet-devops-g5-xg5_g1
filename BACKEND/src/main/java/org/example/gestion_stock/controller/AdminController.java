package org.example.gestion_stock.controller;

import org.example.gestion_stock.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.gestion_stock.model.User;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/all-users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}