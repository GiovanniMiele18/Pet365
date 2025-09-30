package it.unisa.justTraditions.storage.gestioneProfiliStorage.entity;

import jakarta.persistence.*;

@Entity
public class Documento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 255)
    private String descrizione;

    @Column(length = 50)
    private String tipo; // es. "application/pdf", "image/png"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animale_id", nullable = false)
    private Animale animale;

    @OneToOne(mappedBy = "documento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private DatiDocumento datiDocumento;

    // Getters & setters
    public Long getId() { return id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Animale getAnimale() { return animale; }
    public void setAnimale(Animale animale) { this.animale = animale; }

    public DatiDocumento getDatiDocumento() { return datiDocumento; }
    public void setDatiDocumento(DatiDocumento datiDocumento) {
        this.datiDocumento = datiDocumento;
        datiDocumento.setDocumento(this); // sincronizza il legame
    }
}
