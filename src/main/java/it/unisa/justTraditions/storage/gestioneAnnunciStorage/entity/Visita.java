package it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity;

import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REFRESH;

import it.unisa.justTraditions.storage.prenotazioniStorage.entity.Prenotazione;
import it.unisa.justTraditions.storage.util.OnlyStorageCall;
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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Questa classe rappresenta una visita di un annuncio.
 * Una visita e associata a piu prenotazioni.
 */
@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"annuncio", "giorno", "orarioInizio", "orarioFine"})
})
public class Visita {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 9)
  private DayOfWeek giorno;
  @Column(nullable = false)
  private LocalTime orarioInizio;
  @Column(nullable = false)
  private LocalTime orarioFine;
  @Column(nullable = false)
  private Boolean validita;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "annuncio", nullable = false)
  private Annuncio annuncio;
  @OneToMany(mappedBy = "visita", cascade = {MERGE, PERSIST, REFRESH})
  private List<Prenotazione> prenotazioni = new ArrayList<>();

  public Visita() {
  }

  /**
   * Costruttore di una visita.
   *
   * @param giorno       Giorno della settimana di una visita.
   * @param orarioInizio Orario d' inizio di una visita.
   * @param orarioFine   Orario di fine di una visita.
   * @param validita     Validita della visita.
   */
  public Visita(DayOfWeek giorno, LocalTime orarioInizio, LocalTime orarioFine, Boolean validita) {
    this.giorno = giorno;
    this.orarioInizio = orarioInizio;
    this.orarioFine = orarioFine;
    this.validita = validita;
  }

  public Long getId() {
    return id;
  }

  public DayOfWeek getGiorno() {
    return giorno;
  }

  public void setGiorno(DayOfWeek giorno) {
    this.giorno = giorno;
  }

  public LocalTime getOrarioInizio() {
    return orarioInizio;
  }

  public void setOrarioInizio(LocalTime orarioInizio) {
    this.orarioInizio = orarioInizio;
  }

  public LocalTime getOrarioFine() {
    return orarioFine;
  }

  public void setOrarioFine(LocalTime orarioFine) {
    this.orarioFine = orarioFine;
  }

  public Boolean getValidita() {
    return validita;
  }

  public void setValidita(Boolean validita) {
    this.validita = validita;
  }

  public Annuncio getAnnuncio() {
    return annuncio;
  }

  public void setAnnuncio(Annuncio annuncio) {
    OnlyStorageCall.validateCall();
    this.annuncio = annuncio;
  }

  public List<Prenotazione> getPrenotazioni() {
    return Collections.unmodifiableList(prenotazioni);
  }

  public void addPrenotazione(Prenotazione prenotazione) {
    prenotazioni.add(prenotazione);
    prenotazione.setVisita(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Visita visita = (Visita) o;

    return id.equals(visita.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Visita{");
    sb.append("id=").append(id);
    sb.append(", giorno=").append(giorno);
    sb.append(", orarioInizio=").append(orarioInizio);
    sb.append(", orarioFine=").append(orarioFine);
    sb.append(", validita=").append(validita);
    sb.append(", annuncio=").append(annuncio);
    sb.append('}');
    return sb.toString();
  }
}
