package org.example.gestion_stock.controller;

import org.example.gestion_stock.model.HistoriqueMedicin;
import org.example.gestion_stock.repository.HistoriqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historique")
@CrossOrigin(origins = "*")
public class HistoriqueController {

    @Autowired
    private HistoriqueRepository historiqueRepository;

    @GetMapping
    public List<HistoriqueMedicin> getAllHistoriques() {
        return historiqueRepository.findAll();
    }
}
