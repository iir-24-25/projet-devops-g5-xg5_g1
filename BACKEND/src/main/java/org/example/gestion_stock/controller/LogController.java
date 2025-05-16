package org.example.gestion_stock.controller;
import org.example.gestion_stock.model.Log;
import org.example.gestion_stock.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    public LogRepository logrepo;

    @PostMapping
    public Log createLog(@RequestBody Log log) {
        return logrepo.save(log);
    }

    // 2. Récupérer tous les logs
    @GetMapping
    public List<Log> getAllLogs() {
        return logrepo.findAll();
    }

    // 3. Récupérer un log par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Log> getLogById(@PathVariable Long id) {
        Optional<Log> log = logrepo.findById(id);
        return log.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 4. Mettre à jour un log
    @PutMapping("/{id}")
    public ResponseEntity<Log> updateLog(@PathVariable Long id, @RequestBody Log log) {
        if (!logrepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        log.setId(id); // Assurez-vous que le log a le bon ID
        Log updatedLog = logrepo.save(log);
        return ResponseEntity.ok(updatedLog);
    }

    // 5. Supprimer un log
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLog(@PathVariable Long id) {
        if (!logrepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        logrepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
