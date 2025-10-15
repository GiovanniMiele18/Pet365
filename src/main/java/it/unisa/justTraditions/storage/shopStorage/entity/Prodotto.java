package it.unisa.justTraditions.storage.shopStorage.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Entità che rappresenta un prodotto dello shop.
 * I prodotti sono articoli per animali acquistabili da tutti gli utenti.
 */
@Entity
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, length = 2000)
    private String descrizione;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prezzo; // prezzo unitario

    @Column(nullable = false)
    private Integer quantitaDisponibile; // quantità attualmente in magazzino

    @Column(nullable = false)
    private String categoria; // es: "Cibo", "Accessori", "Cura", ecc.

    @OneToMany(mappedBy = "prodotto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FotoProdotto> foto = new ArrayList<>();

    public Prodotto() {}

    public Prodotto(String nome, String descrizione, BigDecimal prezzo, Integer quantitaDisponibile, String categoria) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
        this.quantitaDisponibile = quantitaDisponibile;
        this.categoria = categoria;
    }

    // --- Getters e Setters ---
    public Long getId() { return id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public BigDecimal getPrezzo() { return prezzo; }
    public void setPrezzo(BigDecimal prezzo) { this.prezzo = prezzo; }

    public Integer getQuantitaDisponibile() { return quantitaDisponibile; }
    public void setQuantitaDisponibile(Integer quantitaDisponibile) { this.quantitaDisponibile = quantitaDisponibile; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public List<FotoProdotto> getFoto() { return Collections.unmodifiableList(foto); }

    public void addFoto(FotoProdotto fotoProdotto) {
        foto.add(fotoProdotto);
        fotoProdotto.setProdotto(this);
    }

    public void removeFoto(FotoProdotto fotoProdotto) {
        foto.remove(fotoProdotto);
        fotoProdotto.setProdotto(null);
    }

    // 🔹 Metodo helper per compatibilità con il controller AJAX
    @Transient
    public int getQuantita() {
        return this.quantitaDisponibile != null ? this.quantitaDisponibile : 0;
    }

    @Override
    public String toString() {
        return "Prodotto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", prezzo=" + prezzo +
                ", categoria='" + categoria + '\'' +
                ", quantitaDisponibile=" + quantitaDisponibile +
                '}';
    }
}
