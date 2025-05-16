package org.example.gestion_stock.controller;

import org.example.gestion_stock.model.Medicin;
import org.example.gestion_stock.model.HistoriqueMedicin;
import org.example.gestion_stock.repository.MedicinRepository;
import org.example.gestion_stock.repository.HistoriqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/medicins")
public class MedicinController {

    @Autowired
    private MedicinRepository medicinrepo;

    @Autowired
    private HistoriqueRepository historiqueRepository; // 👈 ajouter l'injection du repository historique

    // Créer un médicament
    @PostMapping
    public Medicin createMedicin(@RequestBody Medicin newMedicin) {
        Medicin savedMedicin = medicinrepo.save(newMedicin);

        // Enregistrer l'historique d'ajout
        HistoriqueMedicin historique = new HistoriqueMedicin(
                "Ajout",
                savedMedicin.getName(),
                savedMedicin.getUserId(),
                LocalDateTime.now()
        );
        historiqueRepository.save(historique);

        return savedMedicin;
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
                    Medicin savedMedicin = medicinrepo.save(medicin);

                    // Enregistrer l'historique de modification
                    HistoriqueMedicin historique = new HistoriqueMedicin(
                            "Modification",
                            savedMedicin.getName(),
                            savedMedicin.getUserId(),
                            LocalDateTime.now()
                    );
                    historiqueRepository.save(historique);

                    return savedMedicin;
                })
                .orElseThrow(() -> new RuntimeException("Médicament non trouvé avec l'id " + id));
    }

    // Supprimer un médicament
    @DeleteMapping("/{id}")
    public void deleteMedicin(@PathVariable Long id) {
        medicinrepo.findById(id).ifPresent(medicin -> {
            // Enregistrer l'historique de suppression avant suppression
            HistoriqueMedicin historique = new HistoriqueMedicin(
                    "Suppression",
                    medicin.getName(),
                    medicin.getUserId(),
                    LocalDateTime.now()
            );
            historiqueRepository.save(historique);

            medicinrepo.deleteById(id);
        });
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
