package it.unisa.justTraditions.applicationLogic.gestioneAnnunciControl.form;

import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Classe che rappresenta il form di una Visita del Annuncio.
 */
@OrarioIsValidConstraint
public class VisitaForm {

  private Long idVisita;

  @NotNull(message = "giorno vuoto")
  private DayOfWeek giorno;

  @NotNull(message = "orario di inizio vuoto")
  @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
  private LocalTime orarioInizio;

  @NotNull(message = "orario di fine vuoto")
  @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
  private LocalTime orarioFine;

  public VisitaForm() {
  }

  /**
   * Costruttore per l ogggetto visitaForm.
   *
   * @param idVisita     Id della visita.
   * @param giorno       Giorno della settimana della visita.
   * @param orarioInizio Orario d'inizio della visita.
   * @param orarioFine   Orario di fine della visita.
   */
  public VisitaForm(Long idVisita, DayOfWeek giorno, LocalTime orarioInizio, LocalTime orarioFine) {
    this.idVisita = idVisita;
    this.giorno = giorno;
    this.orarioInizio = orarioInizio;
    this.orarioFine = orarioFine;
  }

  public Long getIdVisita() {
    return idVisita;
  }

  public void setIdVisita(Long idVisita) {
    this.idVisita = idVisita;
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

  public DayOfWeek getGiorno() {
    return giorno;
  }

  public void setGiorno(DayOfWeek giorno) {
    this.giorno = giorno;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("VisitaForm{");
    sb.append("idVisita=").append(idVisita);
    sb.append(", giorno=").append(giorno);
    sb.append(", orarioInizio=").append(orarioInizio);
    sb.append(", orarioFine=").append(orarioFine);
    sb.append('}');
    return sb.toString();
  }
}
