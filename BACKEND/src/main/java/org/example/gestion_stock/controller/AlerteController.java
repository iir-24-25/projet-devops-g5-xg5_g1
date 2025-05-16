package org.example.gestion_stock.controller;


import org.example.gestion_stock.model.Alerte;
import org.example.gestion_stock.repository.AlerteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/alertes")
public class AlerteController {

    @Autowired
    private AlerteRepository alerteRepo;

    // 1. Créer une alerte
    @PostMapping
    public Alerte createAlerte(@RequestBody Alerte alerte) {
        return alerteRepo.save(alerte);
    }

    // 2. Récupérer toutes les alertes
    @GetMapping
    public List<Alerte> getAllAlertes() {
        return alerteRepo.findAll();
    }

    // 3. Récupérer une alerte par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Alerte> getAlerteById(@PathVariable Long id) {
        Optional<Alerte> alerte = alerteRepo.findById(id);
        return alerte.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 4. Mettre à jour une alerte
    @PutMapping("/{id}")
    public ResponseEntity<Alerte> updateAlerte(@PathVariable Long id, @RequestBody Alerte alerte) {
        if (!alerteRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        alerte.setId(id); // Assurez-vous que l'alerte a le bon ID
        Alerte updatedAlerte = alerteRepo.save(alerte);
        return ResponseEntity.ok(updatedAlerte);
    }

    // 5. Supprimer une alerte
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlerte(@PathVariable Long id) {
        if (!alerteRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        alerteRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
