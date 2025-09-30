package it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity;

import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Amministratore;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Artigiano;
import it.unisa.justTraditions.storage.util.OnlyStorageCall;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Questa classe rappresenta un annuncio.
 * L annuncio è pubblicato da un artigiano.
 * l annuncio contiene una o piu visite.
 * l annuncio contiene uno o fino a tre foto.
 * l annuncio viene approvato o rifiutato da un amministratore.
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
  @Column(nullable = false)
  private String serviziOfferti;
  @Column(nullable = false)
  private Integer numMaxPersonePerVisita;
  @Column(nullable = false, precision = 5, scale = 2)
  private BigDecimal prezzoVisita;
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

  public Annuncio() {
  }

  /**
   * Costruttore di un annuncio.
   *
   * @param nomeAttivita           Nome dell'attività
   * @param provinciaAttivita      Provincia dell' Attività.
   * @param indirizzoAttivita      Indirizzo dell' Attività.
   * @param descrizione            Descrizione della attività.
   * @param serviziOfferti         Breve descrizione dei servizi offerti dalla attività.
   * @param numMaxPersonePerVisita Numero massimo di persone che possono partecipare all' attività.
   * @param prezzoVisita           Prezzo della visita.
   * @param stato                  Strato dell' Annuncio.
   */
  public Annuncio(String nomeAttivita, String provinciaAttivita, String indirizzoAttivita,
                  String descrizione, String serviziOfferti, Integer numMaxPersonePerVisita,
                  BigDecimal prezzoVisita, Stato stato) {
    this.nomeAttivita = nomeAttivita;
    this.provinciaAttivita = provinciaAttivita;
    this.indirizzoAttivita = indirizzoAttivita;
    this.descrizione = descrizione;
    this.serviziOfferti = serviziOfferti;
    this.numMaxPersonePerVisita = numMaxPersonePerVisita;
    this.prezzoVisita = prezzoVisita;
    this.stato = stato;
  }

  public Long getId() {
    return id;
  }

  public String getNomeAttivita() {
    return nomeAttivita;
  }

  public void setNomeAttivita(String nomeAttivita) {
    this.nomeAttivita = nomeAttivita;
  }

  public String getProvinciaAttivita() {
    return provinciaAttivita;
  }

  public void setProvinciaAttivita(String provinciaAttivita) {
    this.provinciaAttivita = provinciaAttivita;
  }

  public String getIndirizzoAttivita() {
    return indirizzoAttivita;
  }

  public void setIndirizzoAttivita(String indirizzoAttivita) {
    this.indirizzoAttivita = indirizzoAttivita;
  }

  public String getDescrizione() {
    return descrizione;
  }

  public void setDescrizione(String descrizione) {
    this.descrizione = descrizione;
  }

  public String getServiziOfferti() {
    return serviziOfferti;
  }

  public void setServiziOfferti(String serviziOfferti) {
    this.serviziOfferti = serviziOfferti;
  }

  public Integer getNumMaxPersonePerVisita() {
    return numMaxPersonePerVisita;
  }

  public void setNumMaxPersonePerVisita(Integer numMaxPersonaPerVisita) {
    this.numMaxPersonePerVisita = numMaxPersonaPerVisita;
  }

  public BigDecimal getPrezzoVisita() {
    return prezzoVisita;
  }

  public void setPrezzoVisita(BigDecimal prezzoVisita) {
    this.prezzoVisita = prezzoVisita;
  }

  public Stato getStato() {
    return stato;
  }

  public void setStato(Stato stato) {
    this.stato = stato;
  }

  public String getMotivoDelRifiuto() {
    return motivoDelRifiuto;
  }

  public void setMotivoDelRifiuto(String motivoDelRifiuto) {
    this.motivoDelRifiuto = motivoDelRifiuto;
  }

  public Amministratore getAmministratore() {
    return amministratore;
  }

  public void setAmministratore(Amministratore amministratore) {
    OnlyStorageCall.validateCall();
    this.amministratore = amministratore;
  }

  public Artigiano getArtigiano() {
    return artigiano;
  }

  public void setArtigiano(Artigiano artigiano) {
    OnlyStorageCall.validateCall();
    this.artigiano = artigiano;
  }

  public List<Visita> getVisite() {
    return Collections.unmodifiableList(visite);
  }

  public void addVisita(Visita visita) {
    visite.add(visita);
    visita.setAnnuncio(this);
  }

  public List<Foto> getFoto() {
    return Collections.unmodifiableList(foto);
  }

  public void addFoto(Foto foto) {
    this.foto.add(foto);
    foto.setAnnuncio(this);
  }

  public void removeFoto(Foto foto) {
    this.foto.remove(foto);
    foto.setAnnuncio(null);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Annuncio annuncio = (Annuncio) o;

    return id.equals(annuncio.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Annuncio{");
    sb.append("id=").append(id);
    sb.append(", nomeAttivita='").append(nomeAttivita).append('\'');
    sb.append(", provinciaAttivita='").append(provinciaAttivita).append('\'');
    sb.append(", indirizzoAttivita='").append(indirizzoAttivita).append('\'');
    sb.append(", descrizione='").append(descrizione).append('\'');
    sb.append(", serviziOfferti='").append(serviziOfferti).append('\'');
    sb.append(", numMaxPersonePerVisita=").append(numMaxPersonePerVisita);
    sb.append(", prezzoVisita=").append(prezzoVisita);
    sb.append(", stato=").append(stato);
    sb.append(", motivoDelRifiuto='").append(motivoDelRifiuto).append('\'');
    sb.append(", amministratore=").append(amministratore);
    sb.append(", artigiano=").append(artigiano);
    sb.append('}');
    return sb.toString();
  }

  /**
   * Enumerator che mappa gli stati di un oggetto Annuncio.
   */
  public enum Stato {
    PROPOSTO,
    IN_REVISIONE,
    RIFIUTATO,
    APPROVATO
  }
}
