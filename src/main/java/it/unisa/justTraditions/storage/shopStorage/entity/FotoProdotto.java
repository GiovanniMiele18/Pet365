package it.unisa.justTraditions.storage.shopStorage.entity;

import jakarta.persistence.*;

/**
 * Rappresenta la foto di un prodotto caricata dallo shop manager o dall'amministratore.
 */
@Entity
public class FotoProdotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(nullable = false, length = 16777215)
    private byte[] dati;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prodotto_id", nullable = false)
    private Prodotto prodotto;

    public FotoProdotto() {}

    public FotoProdotto(byte[] dati) {
        this.dati = dati;
    }

    public Long getId() { return id; }
    public byte[] getDati() { return dati; }
    public void setDati(byte[] dati) { this.dati = dati; }

    public Prodotto getProdotto() { return prodotto; }
    public void setProdotto(Prodotto prodotto) { this.prodotto = prodotto; }
}
