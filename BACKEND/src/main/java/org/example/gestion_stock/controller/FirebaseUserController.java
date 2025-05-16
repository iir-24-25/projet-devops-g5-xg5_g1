package org.example.gestion_stock.controller;


import com.google.firebase.auth.UserRecord;
import org.example.gestion_stock.service.FirebaseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class FirebaseUserController {

    @Autowired
    private FirebaseUserService firebaseUserService;

    @GetMapping
    public List<UserRecord> listAllUsers() throws Exception {
        return firebaseUserService.getAllUsers();
    }
}
