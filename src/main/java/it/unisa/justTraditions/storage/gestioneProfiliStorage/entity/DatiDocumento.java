package it.unisa.justTraditions.storage.gestioneProfiliStorage.entity;

import jakarta.persistence.*;

@Entity
public class DatiDocumento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(nullable = false, length = 16777215)
    private byte[] dati;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documento_id", nullable = false)
    private Documento documento;

    public DatiDocumento() {}

    public DatiDocumento(byte[] dati, Documento documento) {
        this.dati = dati;
        this.documento = documento;
    }


    // Getters & Setters
    public Long getId() { return id; }

    public byte[] getDati() { return dati; }
    public void setDati(byte[] dati) { this.dati = dati; }

    public Documento getDocumento() { return documento; }
    public void setDocumento(Documento documento) { this.documento = documento; }
}
