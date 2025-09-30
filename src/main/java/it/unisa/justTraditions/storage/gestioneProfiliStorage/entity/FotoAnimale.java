package it.unisa.justTraditions.storage.gestioneProfiliStorage.entity;

import it.unisa.justTraditions.storage.util.OnlyStorageCall;
import jakarta.persistence.*;

@Entity
public class FotoAnimale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(nullable = false, length = 16777215)
    private byte[] dati;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animale_id", nullable = false)
    private Animale animale;

    public FotoAnimale() {}

    public FotoAnimale(byte[] dati) {
        this.dati = dati;
    }

    public Long getId() { return id; }

    public byte[] getDati() { return dati; }
    public void setDati(byte[] dati) { this.dati = dati; }

    public Animale getAnimale() { return animale; }
    public void setAnimale(Animale animale) {
        OnlyStorageCall.validateCall();
        this.animale = animale;
    }
}
