package it.unisa.justTraditions.storage.shopStorage.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Entità che rappresenta un prodotto dello shop.
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
    private BigDecimal prezzo;

    @Column(nullable = false)
    private Integer quantitaDisponibile;

    @Column(nullable = false)
    private String categoria;

    @OneToMany(mappedBy = "prodotto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FotoProdotto> foto = new ArrayList<>();

    public Prodotto() {}

    public Prodotto(String nome, String descrizione, BigDecimal prezzo,
                    Integer quantitaDisponibile, String categoria) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
        this.quantitaDisponibile = quantitaDisponibile;
        this.categoria = categoria;
    }

    // --- Getters & Setters ---
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

   public List<FotoProdotto> getFoto() {
    return foto;
}

    // --- Metodi helper ---
    public void addFoto(FotoProdotto fotoProdotto) {
        fotoProdotto.setProdotto(this);
        foto.add(fotoProdotto);
    }

    public void removeFoto(FotoProdotto fotoProdotto) {
        foto.remove(fotoProdotto);
        fotoProdotto.setProdotto(null);
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
