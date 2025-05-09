package org.example.gestion_stock.controller;

import org.example.gestion_stock.model.MouvementStock;
import org.example.gestion_stock.repository.MouvementStockRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/mouvements")
public class MouvementStockController {

    public MouvementStockRepository mouvementstockrepo;

    // 1. Créer un mouvement
    @PostMapping
    public MouvementStock createMouvement(@RequestBody MouvementStock mouvementStock) {
        return mouvementstockrepo.save(mouvementStock);
    }

    // 2. Récupérer tous les mouvements
    @GetMapping
    public List<MouvementStock> getAllMouvements() {
        return mouvementstockrepo.findAll();
    }

    // 3. Récupérer un mouvement par son ID
    @GetMapping("/{id}")
    public ResponseEntity<MouvementStock> getMouvementById(@PathVariable Long id) {
        Optional<MouvementStock> mouvementStock = mouvementstockrepo.findById(id);
        return mouvementStock.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 4. Mettre à jour un mouvement
    @PutMapping("/{id}")
    public ResponseEntity<MouvementStock> updateMouvement(@PathVariable Long id, @RequestBody MouvementStock mouvementStock) {
        if (!mouvementstockrepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        mouvementStock.setId(id); // Assurez-vous que le mouvementStock a le bon ID
        MouvementStock updatedMouvement = mouvementstockrepo.save(mouvementStock);
        return ResponseEntity.ok(updatedMouvement);
    }

    // 5. Supprimer un mouvement
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMouvement(@PathVariable Long id) {
        if (!mouvementstockrepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        mouvementstockrepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
