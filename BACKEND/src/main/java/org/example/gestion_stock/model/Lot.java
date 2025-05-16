package org.example.gestion_stock.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "lot")
public class Lot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_lot")
    private String numeroLot;

    @Column(name = "date_expiration")
    private LocalDate dateExpiration;

    @Column(name = "date_entree")
    private LocalDateTime dateEntree;

    @Column(name = "quantite")
    private Integer quantite;

    @ManyToOne
    @JoinColumn(name = "medicin_id")
    private Medicin medicin;

    @Column(name = "user_id")
    private String userId;

    // Constructeurs
    public Lot() {}

    // Getters/Setters (Générez-les avec votre IDE)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumeroLot() { return numeroLot; }
    public void setNumeroLot(String numeroLot) { this.numeroLot = numeroLot; }
    public LocalDate getDateExpiration() { return dateExpiration; }
    public void setDateExpiration(LocalDate dateExpiration) { this.dateExpiration = dateExpiration; }
    public LocalDateTime getDateEntree() { return dateEntree; }
    public void setDateEntree(LocalDateTime dateEntree) { this.dateEntree = dateEntree; }
    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }
    public Medicin getMedicin() { return medicin; }
    public void setMedicin(Medicin medicin) { this.medicin = medicin; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}