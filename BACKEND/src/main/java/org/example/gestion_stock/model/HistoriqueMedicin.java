package org.example.gestion_stock.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class HistoriqueMedicin {

    @Id
    @GeneratedValue
    private Long id;

    private String action; // Ajout, Modification, Suppression
    private String medicinName; // Nom du médicament
    private String userId; // Qui a fait l'action
    private LocalDateTime dateAction; // Quand

    public HistoriqueMedicin() {}

    public HistoriqueMedicin(String action, String medicinName, String userId, LocalDateTime dateAction) {
        this.action = action;
        this.medicinName = medicinName;
        this.userId = userId;
        this.dateAction = dateAction;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getMedicinName() { return medicinName; }
    public void setMedicinName(String medicinName) { this.medicinName = medicinName; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public LocalDateTime getDateAction() { return dateAction; }
    public void setDateAction(LocalDateTime dateAction) { this.dateAction = dateAction; }
}
