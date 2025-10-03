package it.unisa.justTraditions.applicationLogic.prenotazioniControl.form;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Classe che rappresenta il form per la prenotazione di un Cliente a un annuncio
 * associata a un animale.
 */
public class PrenotazioneForm {

  @NotNull
  private Long idVisita;

  @NotNull
  private Long idAnimale;

  @NotNull
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate dataVisita;

  public PrenotazioneForm() {}

  public PrenotazioneForm(Long idVisita, Long idAnimale, LocalDate dataVisita) {
    this.idVisita = idVisita;
    this.idAnimale = idAnimale;
    this.dataVisita = dataVisita;
  }

  // --- GETTER & SETTER ---
  public Long getIdVisita() { return idVisita; }
  public void setIdVisita(Long idVisita) { this.idVisita = idVisita; }

  public Long getIdAnimale() { return idAnimale; }
  public void setIdAnimale(Long idAnimale) { this.idAnimale = idAnimale; }

  public LocalDate getDataVisita() { return dataVisita; }
  public void setDataVisita(LocalDate dataVisita) { this.dataVisita = dataVisita; }

  @Override
  public String toString() {
    return "PrenotazioneForm{" +
            "idVisita=" + idVisita +
            ", idAnimale=" + idAnimale +
            ", dataVisita=" + dataVisita +
            '}';
  }
}
