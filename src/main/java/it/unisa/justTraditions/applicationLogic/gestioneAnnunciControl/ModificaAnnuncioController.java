package it.unisa.justTraditions.applicationLogic.gestioneAnnunciControl;

import it.unisa.justTraditions.applicationLogic.autenticazioneControl.util.SessionCliente;
import it.unisa.justTraditions.applicationLogic.gestioneAnnunciControl.form.AnnuncioForm;
import it.unisa.justTraditions.applicationLogic.gestioneAnnunciControl.form.VisitaForm;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.dao.AnnuncioDao;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Annuncio;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Foto;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Visita;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.dao.AmministratoreDao;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Amministratore;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * Implementa il controller per la modifica di un annuncio.
 */
@Controller
@RequestMapping("/modificaAnnuncio")
public class ModificaAnnuncioController {

  private static final String modificaAnnuncioView =
      "gestioneAnnunciView/modificaAnnuncio";
  private static final String modificaAnnuncioSuccessView =
      "gestioneAnnunciView/modificaAnnuncioSuccess";

  @Autowired
  private AnnuncioDao annuncioDao;

  @Autowired
  private AmministratoreDao amministratoreDao;

  @Autowired
  private SessionCliente sessionCliente;

  /**
   * Implementa la funzionalità di smistare l'Artigiano
   * sulla view di gestioneAnnunciView/modificaAnnuncio.
   * gestioneAnnunciView/modificaAnnuncio se i parametri sono giusti.
   * IllegalArgumentException se l'id non corrisponde a un annuncio
   * se l'annuncio non e dell'artigiano loggato o lo stato dell'annuncio e Proposto o In_Revisione.
   *
   * @param id    Utilizzato per la ricerca dell Annuncio nel database.
   * @param model Utilizzato per passare degli attributi alla view.
   * @return Restituisce la view da reindirizzare.
   * @throws IllegalArgumentException se i dati non sono previsti dal sistema.
   */
  @GetMapping
  public String get(@RequestParam Long id, Model model) {
    Optional<Annuncio> optionalAnnuncio = annuncioDao.findById(id);
    if (optionalAnnuncio.isEmpty()) {
      throw new IllegalArgumentException();
    }

    Annuncio annuncio = optionalAnnuncio.get();
    if (!annuncio.getArtigiano().equals(sessionCliente.getCliente().get())) {
      throw new IllegalArgumentException();
    }

    if (annuncio.getStato() == Annuncio.Stato.PROPOSTO
        || annuncio.getStato() == Annuncio.Stato.IN_REVISIONE) {
      throw new IllegalArgumentException();
    }

    AnnuncioForm annuncioForm = new AnnuncioForm();
    annuncioForm.setIdAnnuncio(annuncio.getId());
    annuncioForm.setNomeAttivita(annuncio.getNomeAttivita());
    annuncioForm.setProvinciaAttivita(annuncio.getProvinciaAttivita());
    annuncioForm.setIndirizzoAttivita(annuncio.getIndirizzoAttivita());
    annuncioForm.setDescrizione(annuncio.getDescrizione());
    annuncioForm.setServiziOfferti(annuncio.getServiziOfferti());
    annuncioForm.setNumMaxPersonePerVisita(annuncio.getNumMaxPersonePerVisita());
    annuncioForm.setPrezzoVisita(annuncio.getPrezzoVisita());


    List<Long> idFoto = annuncio.getFoto().stream()
        .map(Foto::getId)
        .toList();
    model.addAttribute("idFoto", idFoto);

    List<VisitaForm> visiteForm = annuncio.getVisite().stream()
        .filter(Visita::getValidita)
        .map(v -> new VisitaForm(v.getId(), v.getGiorno(), v.getOrarioInizio(), v.getOrarioFine()))
        .toList();
    annuncioForm.setVisite(visiteForm);

    model.addAttribute("annuncioForm", annuncioForm);

    return modificaAnnuncioView;
  }

  /**
   * Implementa la funzionalità di Modifica di un annuncio.
   *
   * @param annuncioForm  Utilizzato per mappare il Form della view.
   * @param bindingResult Utilizzato per mappare gli errori dei dati di loginForm.
   * @param idFoto        Utilizzato per segnare le foto dell annuncio che non sono state eliminate
   *                      nella modifica.
   * @param model         Utilizzato per passare degli attributi alla view.
   * @return Restituisce la view da reindirizzare.
   * @throws IllegalArgumentException se i dati non sono previsti dal sistema.
   */
  @PostMapping
  public String post(@ModelAttribute @Valid AnnuncioForm annuncioForm, BindingResult bindingResult,
                     @RequestParam(required = false) List<Long> idFoto, Model model) {
    model.addAttribute("idFoto", idFoto);
    
    if (bindingResult.hasFieldErrors("foto")) {
      if (bindingResult.getErrorCount() > 1) {
        return modificaAnnuncioView;
      }
    } else {
      if (bindingResult.getErrorCount() > 0) {
        return modificaAnnuncioView;
      }
    }

    if (idFoto == null
        && (annuncioForm.getFoto() == null || annuncioForm.getFoto().get(0).isEmpty())) {
      model.addAttribute("nessunaFoto", true);
      return modificaAnnuncioView;
    }

    if (annuncioForm.getIdAnnuncio() == null) {
      throw new IllegalArgumentException();
    }

    Optional<Annuncio> optionalAnnuncio = annuncioDao.findById(annuncioForm.getIdAnnuncio());
    if (optionalAnnuncio.isEmpty()) {
      throw new IllegalArgumentException();
    }

    Annuncio annuncio = optionalAnnuncio.get();
    if (!annuncio.getArtigiano().equals(sessionCliente.getCliente().get())) {
      throw new IllegalArgumentException();
    }

    if (annuncio.getStato() == Annuncio.Stato.PROPOSTO
        || annuncio.getStato() == Annuncio.Stato.IN_REVISIONE) {
      throw new IllegalArgumentException();
    }

    boolean modificaInformazioniAttivita = false;

    if (!annuncio.getNomeAttivita().equals(annuncioForm.getNomeAttivita())) {
      annuncio.setNomeAttivita(annuncioForm.getNomeAttivita());
      modificaInformazioniAttivita = true;
    }

    if (!annuncio.getProvinciaAttivita().equals(annuncioForm.getProvinciaAttivita())) {
      annuncio.setProvinciaAttivita(annuncioForm.getProvinciaAttivita());
      modificaInformazioniAttivita = true;
    }

    if (!annuncio.getIndirizzoAttivita().equals(annuncioForm.getIndirizzoAttivita())) {
      annuncio.setIndirizzoAttivita(annuncioForm.getIndirizzoAttivita());
      modificaInformazioniAttivita = true;
    }

    if (!annuncio.getDescrizione().equals(annuncioForm.getDescrizione())) {
      annuncio.setDescrizione(annuncioForm.getDescrizione());
      modificaInformazioniAttivita = true;
    }

    if (!annuncio.getServiziOfferti().equals(annuncioForm.getServiziOfferti())) {
      annuncio.setServiziOfferti(annuncioForm.getServiziOfferti());
      modificaInformazioniAttivita = true;
    }

    List<Foto> foto = annuncio.getFoto();
    List<Foto> fotoRimosse = new ArrayList<>();
    for (Foto f : foto) {
      if (idFoto == null || !idFoto.contains(f.getId())) {
        fotoRimosse.add(f);
      }
    }
    for (Foto f : fotoRimosse) {
      annuncio.removeFoto(f);
    }

    List<MultipartFile> annuncioFormFoto = annuncioForm.getFoto();
    if (annuncioFormFoto != null && !annuncioFormFoto.get(0).isEmpty()) {
      for (MultipartFile file : annuncioFormFoto) {
        if (foto.size() >= 3) {
          break;
        }

        try {
          annuncio.addFoto(new Foto(file.getBytes()));
          modificaInformazioniAttivita = true;
        } catch (IOException e) {
          model.addAttribute("erroreFile", true);
          return modificaAnnuncioView;
        }
      }
    }

    Amministratore amministratore = annuncio.getAmministratore();
    if (modificaInformazioniAttivita) {
      annuncio.setStato(Annuncio.Stato.PROPOSTO);
      if (amministratore != null) {
        amministratore.removeAnnuncioApprovato(annuncio);
      }
      annuncio.setMotivoDelRifiuto(null);
    }

    annuncio.setPrezzoVisita(annuncioForm.getPrezzoVisita());
    annuncio.setNumMaxPersonePerVisita(annuncioForm.getNumMaxPersonePerVisita());

    List<Visita> visite = annuncio.getVisite();
    List<VisitaForm> visiteForm = annuncioForm.getVisite();

    visite.forEach(visita -> visita.setValidita(false));

    for (VisitaForm visitaForm : visiteForm) {
      Optional<Visita> optionalVisita = visite.stream()
          .filter(visita -> visita.getGiorno().equals(visitaForm.getGiorno())
              && visita.getOrarioInizio().equals(visitaForm.getOrarioInizio())
              && visita.getOrarioFine().equals(visitaForm.getOrarioFine()))
          .findFirst();
      if (optionalVisita.isPresent()) {
        optionalVisita.get().setValidita(true);
      } else {
        Visita visita = new Visita(
            visitaForm.getGiorno(),
            visitaForm.getOrarioInizio(),
            visitaForm.getOrarioFine(),
            true
        );
        annuncio.addVisita(visita);
      }
    }

    if (modificaInformazioniAttivita && amministratore != null) {
      amministratoreDao.save(amministratore);
    }
    annuncioDao.save(annuncio);

    return modificaAnnuncioSuccessView;
  }
}

