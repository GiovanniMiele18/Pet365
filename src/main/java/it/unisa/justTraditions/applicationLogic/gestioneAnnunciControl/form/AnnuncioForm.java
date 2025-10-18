package it.unisa.justTraditions.applicationLogic.gestioneAnnunciControl.form;

import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Visita;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class AnnuncioForm {

  private Long idAnnuncio;

  @NotBlank(message = "Il nome dell'attività è obbligatorio")
  private String nomeAttivita;

  @NotBlank(message = "La provincia è obbligatoria")
  private String provinciaAttivita;

  @NotBlank(message = "L'indirizzo è obbligatorio")
  private String indirizzoAttivita;

  @NotBlank(message = "La descrizione è obbligatoria")
  private String descrizione;

  @NotNull(message = "Il servizio offerto è obbligatorio")
  private ServiziOffertiEnum serviziOfferti;

  @Valid
  private List<VisitaForm> visite = new ArrayList<>();
  
  private List<MultipartFile> foto = new ArrayList<>();

  public enum ServiziOffertiEnum {
    PETSITTER,
    VETERINARI_H24,
    SHOP,
    DOCCE_E_TOELETTATURA,
    ADDESTRAMENTO,
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

  // --- Getters e Setters ---

  public Long getIdAnnuncio() { return idAnnuncio; }
  public void setIdAnnuncio(Long idAnnuncio) { this.idAnnuncio = idAnnuncio; }

  public String getNomeAttivita() { return nomeAttivita; }
  public void setNomeAttivita(String nomeAttivita) { this.nomeAttivita = nomeAttivita; }

  public String getProvinciaAttivita() { return provinciaAttivita; }
  public void setProvinciaAttivita(String provinciaAttivita) { this.provinciaAttivita = provinciaAttivita; }

  public String getIndirizzoAttivita() { return indirizzoAttivita; }
  public void setIndirizzoAttivita(String indirizzoAttivita) { this.indirizzoAttivita = indirizzoAttivita; }

  public String getDescrizione() { return descrizione; }
  public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

  public ServiziOffertiEnum getServiziOfferti() { return serviziOfferti; }
  public void setServiziOfferti(ServiziOffertiEnum serviziOfferti) { this.serviziOfferti = serviziOfferti; }

  public List<VisitaForm> getVisite() { return visite; }
  public void setVisite(List<VisitaForm> visite) { this.visite = visite; }

  public List<MultipartFile> getFoto() { return foto; }
  public void setFoto(List<MultipartFile> foto) { this.foto = foto; }

}
