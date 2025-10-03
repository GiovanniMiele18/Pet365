package it.unisa.justTraditions.storage.gestioneProfiliStorage.entity;

import it.unisa.justTraditions.storage.prenotazioniStorage.entity.Prenotazione;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Animale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String specie;
    private String razza;
    private int eta;
    private String sesso;
    private double peso;
    private String colore;

    @Column(length = 500)
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "animale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FotoAnimale> fotoAnimali = new ArrayList<>();

    @OneToMany(mappedBy = "animale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Documento> documenti = new ArrayList<>();

    @OneToMany(mappedBy = "animale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Prenotazione> prenotazioni = new ArrayList<>();

    public List<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    public void addPrenotazione(Prenotazione prenotazione) {
        prenotazioni.add(prenotazione);
        prenotazione.setAnimale(this);
    }



    // ✅ Costruttore vuoto richiesto da Hibernate
    public Animale() {}

    public Animale(String nome, String specie, String razza, int eta,
                   String sesso, Double peso, String colore, String note) {
        this.nome = nome;
        this.specie = specie;
        this.razza = razza;
        this.eta = eta;
        this.sesso = sesso;
        this.peso = peso;
        this.colore = colore;
        this.note = note;
    }

    // Getter & Setter
    public Long getId() { return id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSpecie() { return specie; }
    public void setSpecie(String specie) { this.specie = specie; }

    public String getRazza() { return razza; }
    public void setRazza(String razza) { this.razza = razza; }

    public int getEta() { return eta; }
    public void setEta(int eta) { this.eta = eta; }

    public String getSesso() { return sesso; }
    public void setSesso(String sesso) { this.sesso = sesso; }

    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }

    public String getColore() { return colore; }
    public void setColore(String colore) { this.colore = colore; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public List<FotoAnimale> getFotoAnimali() {
        return fotoAnimali;
    }

    public void addFoto(FotoAnimale f) {
        f.setAnimale(this);
        this.fotoAnimali.add(f);
    }

    public List<Documento> getDocumenti() { return documenti; }
    public void setDocumenti(List<Documento> documenti) { this.documenti = documenti; }
}
