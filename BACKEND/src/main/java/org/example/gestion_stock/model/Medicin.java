package org.example.gestion_stock.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Medicin {
    @Id
    @GeneratedValue
    private int id;

    private String name;
    private String description;
    private String codeBarres;
    private String categorie;
    private String fabriquant;
    private Integer seuilAlerte;

    private Integer quantity; // 👈 Nouveau champ pour la quantité en stock

    private String userId; // ✅ Champ pour lier l'utilisateur

    @OneToMany(mappedBy = "medicin")
    private List<Lot> lots;

    public Medicin() {}

    public Medicin(int id, String name, String description, String codeBarres, String categorie,
                   String fabriquant, Integer seuilAlerte, Integer quantity,
                    String userId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.codeBarres = codeBarres;
        this.categorie = categorie;
        this.fabriquant = fabriquant;
        this.seuilAlerte = seuilAlerte;
        this.quantity = quantity;
        this.lots = lots;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCodeBarres() {
        return codeBarres;
    }

    public void setCodeBarres(String codeBarres) {
        this.codeBarres = codeBarres;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getFabriquant() {
        return fabriquant;
    }

    public void setFabriquant(String fabriquant) {
        this.fabriquant = fabriquant;
    }

    public Integer getSeuilAlerte() {
        return seuilAlerte;
    }

    public void setSeuilAlerte(Integer seuilAlerte) {
        this.seuilAlerte = seuilAlerte;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
