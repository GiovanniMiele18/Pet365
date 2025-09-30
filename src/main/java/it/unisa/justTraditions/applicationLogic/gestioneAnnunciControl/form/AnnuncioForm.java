package it.unisa.justTraditions.applicationLogic.gestioneAnnunciControl.form;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 * Classe che rappresenta il form per la Sottomissione di un Annuncio.
 */
public class AnnuncioForm {

  private Long idAnnuncio;

  @NotBlank(message = "Nome attività vuoto")
  @Size(min = 2, max = 40, message = "Nome attività deve avere una lunghezza compresa fra 2 e 40")
  private String nomeAttivita;

  @NotBlank(message = "Provincia attività vuota")
  @ProvinciaIsValidConstraint
  private String provinciaAttivita;

  @NotBlank(message = "Indirizzo vuoto")
  @Size(min = 2, max = 128, message = "Indirizzo deve avere una lunghezza compresa fra 2 e 128")
  private String indirizzoAttivita;

  @NotBlank(message = "Descrizione vuota")
  @Size(min = 2, max = 1024, message = "Descrizione deve avere una lunghezza compresa fra 2 e 1024")
  private String descrizione;

  @NotBlank(message = "Servizi offerti vuoto")
  @Size(min = 2, max = 255,
      message = "Servizi offerti deve avere una lunghezza compresa fra 2 e 255")
  private String serviziOfferti;

  @NotNull(message = "Numero massimo di persone per visita vuoto")
  @Min(value = 1, message = "Almeno una persona")
  private Integer numMaxPersonePerVisita;

  @NotNull(message = "Prezzo visita vuoto")
  @DecimalMin(value = "0.0", inclusive = false, message = "Prezzo minore o uguale a 0")
  @Digits(integer = 3, fraction = 2,
      message = "Prezzo può avere massimo 3 cifre intere e due decimali")
  private BigDecimal prezzoVisita;

  @NotNull(message = "Nessuna foto inserita")
  @Size(min = 1, max = 3, message = "Almeno una, massimo 3 foto")
  @FotoListIsValidConstraint
  private List<MultipartFile> foto;

  @NotNull(message = "Nessuna visita inserita")
  @Size(min = 1, message = "Almeno una visita")
  private List<@Valid VisitaForm> visite;

  public AnnuncioForm() {
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
   * @param visite                 Lista delle visite del Annuncio.
   */
  public AnnuncioForm(Long idAnnuncio, String nomeAttivita, String provinciaAttivita,
                      String indirizzoAttivita, String descrizione, String serviziOfferti,
                      Integer numMaxPersonePerVisita, BigDecimal prezzoVisita,
                      List<@Valid VisitaForm> visite) {
    this.idAnnuncio = idAnnuncio;
    this.nomeAttivita = nomeAttivita;
    this.provinciaAttivita = provinciaAttivita;
    this.indirizzoAttivita = indirizzoAttivita;
    this.descrizione = descrizione;
    this.serviziOfferti = serviziOfferti;
    this.numMaxPersonePerVisita = numMaxPersonePerVisita;
    this.prezzoVisita = prezzoVisita;
    this.visite = visite;
  }

  public Long getIdAnnuncio() {
    return idAnnuncio;
  }

  public void setIdAnnuncio(Long idAnnuncio) {
    this.idAnnuncio = idAnnuncio;
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

  public void setNumMaxPersonePerVisita(Integer numMaxPersonePerVisita) {
    this.numMaxPersonePerVisita = numMaxPersonePerVisita;
  }

  public BigDecimal getPrezzoVisita() {
    return prezzoVisita;
  }

  public void setPrezzoVisita(BigDecimal prezzoVisita) {
    this.prezzoVisita = prezzoVisita;
  }

  public List<MultipartFile> getFoto() {
    return foto;
  }

  public void setFoto(List<MultipartFile> foto) {
    this.foto = foto;
  }

  public List<VisitaForm> getVisite() {
    return visite;
  }

  public void setVisite(List<VisitaForm> visite) {
    this.visite = visite;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("AnnuncioForm{");
    sb.append("idAnnuncio=").append(idAnnuncio);
    sb.append(", nomeAttivita='").append(nomeAttivita).append('\'');
    sb.append(", provinciaAttivita='").append(provinciaAttivita).append('\'');
    sb.append(", indirizzoAttivita='").append(indirizzoAttivita).append('\'');
    sb.append(", descrizione='").append(descrizione).append('\'');
    sb.append(", serviziOfferti='").append(serviziOfferti).append('\'');
    sb.append(", numMaxPersonePerVisita=").append(numMaxPersonePerVisita);
    sb.append(", prezzoVisita=").append(prezzoVisita);
    sb.append(", foto=").append(foto);
    sb.append(", visite=").append(visite);
    sb.append('}');
    return sb.toString();
  }
}
