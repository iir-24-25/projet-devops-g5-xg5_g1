package org.example.gestion_stock.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class MouvementStock {
    @Id
    @GeneratedValue
    private long id;
    private String motif;
    private LocalDateTime dateMouvement;
    @Enumerated
    private TypeMouvement type;
    @ManyToOne
    @JoinColumn(name = "lot_id")
    private Lot lot;
    @ManyToOne
    @JoinColumn(name = "effectue_par")
    private User utilisateur;
    private int quantite;

    public MouvementStock() {}
    public MouvementStock(long id, String motif, LocalDateTime dateMouvement, int quantite , TypeMouvement type, Lot lot, User utilisateur) {
        this.id = id;
        this.motif = motif;
        this.dateMouvement = dateMouvement;
        this.type = type;
        this.lot = lot;
        this.utilisateur = utilisateur;
        this.quantite = quantite;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public LocalDateTime getDateMouvement() {
        return dateMouvement;
    }

    public void setDateMouvement(LocalDateTime dateMouvement) {
        this.dateMouvement = dateMouvement;
    }

    public TypeMouvement getType() {
        return type;
    }

    public void setType(TypeMouvement type) {
        this.type = type;
    }

    public Lot getLot() {
        return lot;
    }

    public void setLot(Lot lot) {
        this.lot = lot;
    }

    public User getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(User utilisateur) {
        this.utilisateur = utilisateur;
    }
}
