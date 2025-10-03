package it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity;

import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Amministratore;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Artigiano;
import it.unisa.justTraditions.storage.util.OnlyStorageCall;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Questa classe rappresenta un annuncio.
 * L'annuncio è pubblicato da un artigiano.
 * Contiene una o più visite e fino a tre foto.
 * Viene approvato o rifiutato da un amministratore.
 */
@Entity
public class Annuncio {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 40)
  private String nomeAttivita;

  @Column(nullable = false, length = 21)
  private String provinciaAttivita;

  @Column(nullable = false, length = 128)
  private String indirizzoAttivita;

  @Column(nullable = false, length = 1024)
  private String descrizione;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 50)
  private ServizioOfferto serviziOfferti;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 12)
  private Stato stato;

  private String motivoDelRifiuto;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "amministratore")
  private Amministratore amministratore;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "artigiano", nullable = false)
  private Artigiano artigiano;

  @OneToMany(mappedBy = "annuncio", cascade = CascadeType.ALL)
  private List<Visita> visite = new ArrayList<>();

  @OneToMany(mappedBy = "annuncio", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Foto> foto = new ArrayList<>();

  public Annuncio() {}

  public Annuncio(String nomeAttivita, String provinciaAttivita, String indirizzoAttivita,
                  String descrizione, ServizioOfferto serviziOfferti, Stato stato) {
    this.nomeAttivita = nomeAttivita;
    this.provinciaAttivita = provinciaAttivita;
    this.indirizzoAttivita = indirizzoAttivita;
    this.descrizione = descrizione;
    this.serviziOfferti = serviziOfferti;
    this.stato = stato;
  }

  // --- Getter e Setter ---
  public Long getId() { return id; }

  public String getNomeAttivita() { return nomeAttivita; }
  public void setNomeAttivita(String nomeAttivita) { this.nomeAttivita = nomeAttivita; }

  public String getProvinciaAttivita() { return provinciaAttivita; }
  public void setProvinciaAttivita(String provinciaAttivita) { this.provinciaAttivita = provinciaAttivita; }

  public String getIndirizzoAttivita() { return indirizzoAttivita; }
  public void setIndirizzoAttivita(String indirizzoAttivita) { this.indirizzoAttivita = indirizzoAttivita; }

  public String getDescrizione() { return descrizione; }
  public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

  public ServizioOfferto getServiziOfferti() { return serviziOfferti; }
  public void setServiziOfferti(ServizioOfferto serviziOfferti) { this.serviziOfferti = serviziOfferti; }

  public Stato getStato() { return stato; }
  public void setStato(Stato stato) { this.stato = stato; }

  public String getMotivoDelRifiuto() { return motivoDelRifiuto; }
  public void setMotivoDelRifiuto(String motivoDelRifiuto) { this.motivoDelRifiuto = motivoDelRifiuto; }

  public Amministratore getAmministratore() { return amministratore; }
  public void setAmministratore(Amministratore amministratore) {
    OnlyStorageCall.validateCall();
    this.amministratore = amministratore;
  }

  public Artigiano getArtigiano() { return artigiano; }
  public void setArtigiano(Artigiano artigiano) {
    OnlyStorageCall.validateCall();
    this.artigiano = artigiano;
  }

  public List<Visita> getVisite() { return Collections.unmodifiableList(visite); }
  public void addVisita(Visita visita) {
    visite.add(visita);
    visita.setAnnuncio(this);
  }

  public List<Foto> getFoto() { return Collections.unmodifiableList(foto); }
  public void addFoto(Foto foto) {
    this.foto.add(foto);
    foto.setAnnuncio(this);
  }
  public void removeFoto(Foto foto) {
    this.foto.remove(foto);
    foto.setAnnuncio(null);
  }

  // --- Equals, hashCode, toString ---
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Annuncio annuncio = (Annuncio) o;

    return id.equals(annuncio.id);
  }

  @Override
  public int hashCode() { return id.hashCode(); }

  @Override
  public String toString() {
    return "Annuncio{" +
            "id=" + id +
            ", nomeAttivita='" + nomeAttivita + '\'' +
            ", provinciaAttivita='" + provinciaAttivita + '\'' +
            ", indirizzoAttivita='" + indirizzoAttivita + '\'' +
            ", descrizione='" + descrizione + '\'' +
            ", serviziOfferti=" + serviziOfferti +
            ", stato=" + stato +
            ", motivoDelRifiuto='" + motivoDelRifiuto + '\'' +
            ", amministratore=" + amministratore +
            ", artigiano=" + artigiano +
            '}';
  }

  // --- Enum ---
  public enum Stato {
    PROPOSTO,
    IN_REVISIONE,
    RIFIUTATO,
    APPROVATO
  }

  public enum ServizioOfferto {
    PETSITTER,
    VETERINARI_H24,
    SHOP,
    DOCCE_TOELETTATURA,
    ADDDESTRAMENTO,
    PENSIONI,
    COMMEMORAZIONI_E_SERVIZI_FUNEBRI,
    SERVIZI_DI_ACCOPPIAMENTO,
    COMPRA_O_ADOTTA_UN_CUCCIOLO,
    SFILATE_E_EVENTI,
    TRASPORTO_SICURO_PER_ANIMALI,
    ASSICURAZIONE_ANIMALI,
    PET_PHOTOGRAPHY,
    TERAPIE_E_BENESSERE,
    NUTRIZIONE_PERSONALIZZATA
  }
}
