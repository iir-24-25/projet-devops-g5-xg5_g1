package org.example.gestion_stock.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Alerte {
    @Id
    @GeneratedValue
    private long id;
    @Enumerated
    private TypeAlert type;
    private String message;
    private boolean estResolue;
    private LocalDateTime dateAlerte;
    @ManyToOne
    @JoinColumn(name = "lot_id")
    private Lot lot;

    public void setId(Long id) {
    }
}
