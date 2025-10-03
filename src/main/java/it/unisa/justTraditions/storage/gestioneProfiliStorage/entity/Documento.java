package it.unisa.justTraditions.storage.gestioneProfiliStorage.entity;

import jakarta.persistence.*;

@Entity
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome; // Nome logico scelto dall’utente

    @Column(length = 255)
    private String descrizione;

    @Column(length = 150)
    private String tipo; // MIME type es. "application/pdf"

    @Column(length = 10)
    private String estensione; // es. ".pdf", ".jpg"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animale_id", nullable = false)
    private Animale animale;

    @OneToOne(mappedBy = "documento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private DatiDocumento datiDocumento;

    // --- Getters & Setters ---
    public Long getId() { return id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getEstensione() { return estensione; }
    public void setEstensione(String estensione) { this.estensione = estensione; }

    public Animale getAnimale() { return animale; }
    public void setAnimale(Animale animale) { this.animale = animale; }

    public DatiDocumento getDatiDocumento() { return datiDocumento; }
    public void setDatiDocumento(DatiDocumento datiDocumento) {
        this.datiDocumento = datiDocumento;
        if (datiDocumento != null) {
            datiDocumento.setDocumento(this); // sincronizza la relazione bidirezionale
        }
    }
}
