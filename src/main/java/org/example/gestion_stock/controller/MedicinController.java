package org.example.gestion_stock.controller;

import org.example.gestion_stock.model.Medicin;
import org.example.gestion_stock.repository.MedicinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/medicins")
public class MedicinController {

    @Autowired
    private MedicinRepository medicinrepo;

    // Créer un médicament
    @PostMapping
    public Medicin createMedicin(@RequestBody Medicin newMedicin) {
        return medicinrepo.save(newMedicin);
    }

    // Mettre à jour un médicament
    @PutMapping("/{id}")
    public Medicin updateMedicin(@PathVariable Long id, @RequestBody Medicin updatedMedicin) {
        return medicinrepo.findById(id)
                .map(medicin -> {
                    medicin.setName(updatedMedicin.getName());
                    medicin.setFabriquant(updatedMedicin.getFabriquant());
                    medicin.setDescription(updatedMedicin.getDescription());
                    medicin.setSeuilAlerte(updatedMedicin.getSeuilAlerte());
                    medicin.setQuantity(updatedMedicin.getQuantity());
                    return medicinrepo.save(medicin);
                })
                .orElseThrow(() -> new RuntimeException("Médicament non trouvé avec l'id " + id));
    }

    // Supprimer un médicament
    @DeleteMapping("/{id}")
    public void deleteMedicin(@PathVariable Long id) {
        medicinrepo.deleteById(id);
    }

    // Récupérer tous les médicaments ou par utilisateur
    @GetMapping
    public List<Medicin> getMedicinsByUserId(@RequestParam(required = false) String userId) {
        if (userId != null) {
            return medicinrepo.findByUserId(userId);
        } else {
            return medicinrepo.findAll();
        }
    }

    // Récupérer un médicament par ID
    @GetMapping("/{id}")
    public Medicin getMedicin(@PathVariable Long id) {
        return medicinrepo.findById(id).orElse(null);
    }

    // Récupérer les médicaments en stock faible
    @GetMapping("/low-stock")
    public List<Medicin> getLowStockMedicins(@RequestParam(required = false) String userId) {
        List<Medicin> medicins;

        if (userId != null) {
            medicins = medicinrepo.findByUserId(userId);
        } else {
            medicins = medicinrepo.findAll();
        }

        return medicins.stream()
                .filter(m -> m.getQuantity() != null &&
                        m.getSeuilAlerte() != null &&
                        m.getQuantity() <= m.getSeuilAlerte())
                .toList();
    }
}