package org.example.gestion_stock.controller;

import org.example.gestion_stock.model.User;
import org.example.gestion_stock.model.role;
import org.example.gestion_stock.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        authService.register(user, user.getRole() != null ? user.getRole() : role.MEDICIN);
        return "Utilisateur créé !";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password) {
        User user = authService.login(email, password);
        return user != null ? "Connecté en tant que " + user.getRole() : "Échec de connexion";
    }
}