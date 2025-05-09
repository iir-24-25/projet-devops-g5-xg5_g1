package org.example.gestion_stock.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Log {
    @Id
    @GeneratedValue
    private long id;
    private String action;
    private LocalDateTime dateAction;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User utilisateur;

    public Log() {}
    public Log(long id, String action, LocalDateTime dateAction, User utilisateur) {
        this.id = id;
        this.action = action;
        this.dateAction = dateAction;
        this.utilisateur = utilisateur;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDateTime getDateAction() {
        return dateAction;
    }

    public void setDateAction(LocalDateTime dateAction) {
        this.dateAction = dateAction;
    }

    public User getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(User utilisateur) {
        this.utilisateur = utilisateur;
    }
}

