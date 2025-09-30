package it.unisa.justTraditions.applicationLogic.gestioneAnnunciControl;

import it.unisa.justTraditions.applicationLogic.autenticazioneControl.util.SessionCliente;
import it.unisa.justTraditions.applicationLogic.gestioneAnnunciControl.form.AnnuncioForm;
import it.unisa.justTraditions.applicationLogic.gestioneAnnunciControl.form.VisitaForm;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Annuncio;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Foto;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Visita;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.dao.ArtigianoDao;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Artigiano;
import jakarta.validation.Valid;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

/**
 * Implementa il controller per la sottomissione Di un Annuncio.
 */
@Controller
@RequestMapping("/sottomissioneAnnuncio")
public class SottomissioneAnnuncioController {

  private static final String sottomissioneAnnuncioView =
      "gestioneAnnunciView/sottomissioneAnnuncio";
  private static final String modificaAnnuncioSuccessView =
      "gestioneAnnunciView/modificaAnnuncioSuccess";

  @Autowired
  private SessionCliente sessionCliente;

  @Autowired
  private ArtigianoDao artigianoDao;

  /**
   * Implementa la funzionalità di smistare l'Artigiano sulla view di
   * gestioneAnnunciView/sottomissioneAnnuncio.
   *
   * @param annuncioForm Utilizzato per mappare il Form della view.
   * @return Restituisce la view da reindirizzare.
   */
  @GetMapping
  public String get(@ModelAttribute AnnuncioForm annuncioForm) {
    return sottomissioneAnnuncioView;
  }

  /**
   * Implementa la funzionalità di Sottomissione di un annuncio.
   *
   * @param annuncioForm  Utilizzato per mappare il Form della view.
   * @param bindingResult Utilizzato per mappare gli errori dei dati di annuncioForm.
   * @param model         Utilizzato per passare degli attributi alla view.
   * @return Restituisce la view da reindirizzare.
   */
  @PostMapping
  public String post(@ModelAttribute @Valid AnnuncioForm annuncioForm,
                     BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      return sottomissioneAnnuncioView;
    }

    Annuncio annuncio = new Annuncio(
        annuncioForm.getNomeAttivita(),
        annuncioForm.getProvinciaAttivita(),
        annuncioForm.getIndirizzoAttivita(),
        annuncioForm.getDescrizione(),
        annuncioForm.getServiziOfferti(),
        annuncioForm.getNumMaxPersonePerVisita(),
        annuncioForm.getPrezzoVisita(),
        Annuncio.Stato.PROPOSTO
    );

    for (MultipartFile file : annuncioForm.getFoto()) {
      try {
        annuncio.addFoto(new Foto(file.getBytes()));
      } catch (IOException e) {
        model.addAttribute("erroreFile", true);
        return sottomissioneAnnuncioView;
      }
    }

    for (VisitaForm visitaForm : annuncioForm.getVisite()) {
      annuncio.addVisita(new Visita(
          visitaForm.getGiorno(),
          visitaForm.getOrarioInizio(),
          visitaForm.getOrarioFine(),
          true
      ));
    }

    Artigiano artigiano = (Artigiano) sessionCliente.getCliente().get();
    artigiano.addAnnuncio(annuncio);
    artigianoDao.save(artigiano);

    return modificaAnnuncioSuccessView;
  }
}

