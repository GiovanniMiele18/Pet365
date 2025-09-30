package it.unisa.justTraditions.applicationLogic.prenotazioniControl;

import it.unisa.justTraditions.applicationLogic.autenticazioneControl.util.SessionCliente;
import it.unisa.justTraditions.applicationLogic.prenotazioniControl.form.PrenotazioneForm;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.dao.VisitaDao;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Annuncio;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Visita;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Cliente;
import it.unisa.justTraditions.storage.prenotazioniStorage.dao.PrenotazioneDao;
import it.unisa.justTraditions.storage.prenotazioniStorage.entity.Prenotazione;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Implementa il controller per Effettuare una prenotazione a un annuncio.
 */
@Controller
@RequestMapping("/effettuaPrenotazione")
public class EffettuaPrenotazioneController {

  private static final String effettuaPrenotazioneView = "prenotazioniView/effettuaPrenotazione";
  private static final String esitoPrenotazioneView = "prenotazioniView/esitoPrenotazione";

  @Autowired
  private VisitaDao visitaDao;

  @Autowired
  private SessionCliente sessionCliente;

  @Autowired
  private PrenotazioneDao prenotazioneDao;

  /**
   * Implementa la funzionalità di smistare il Cliente sulla view
   * di prenotazioniView/effettuaPrenotazione.
   *
   * @param prenotazioneForm Utilizzato per mappare il Form della view.
   * @param bindingResult    Utilizzato per mappare gli errori dei dati di annuncioForm.
   * @param model            Utilizzato per passare degli attributi alla view.
   * @return Restituisce la view da reindirizzare.
   * @throws IllegalArgumentException se i dati non sono previsti dal sistema.
   */
  @GetMapping
  public String get(@ModelAttribute @Valid PrenotazioneForm prenotazioneForm,
                    BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      throw new IllegalArgumentException("Errore nella sottomissione della prenotazione");
    }

    Visita visita = visitaDao.findById(prenotazioneForm.getIdVisita()).get();

    Annuncio annuncio = visita.getAnnuncio();
    model.addAttribute("prezzo", annuncio.getPrezzoVisita());
    model.addAttribute("orarioinizio", visita.getOrarioInizio());
    model.addAttribute("orariofine", visita.getOrarioFine());
    model.addAttribute("idAnnuncio", annuncio.getId());
    return effettuaPrenotazioneView;
  }

  /**
   * Implementa la funzionalità di prenotazione a un annuncio.
   *
   * @param prenotazioneForm Utilizzato per mappare il Form della view.
   * @param bindingResult    Utilizzato per mappare gli errori dei dati di prenotazioneForm.
   * @return Restituisce la view da reindirizzare.
   * @throws IllegalArgumentException se i dati non sono previsti dal sistema.
   */
  @PostMapping
  public String post(@ModelAttribute @Valid PrenotazioneForm prenotazioneForm,
                     BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new IllegalArgumentException();
    }

    Visita visita = visitaDao.findById(prenotazioneForm.getIdVisita()).get();

    Prenotazione prenotazione =
        new Prenotazione(visita.getAnnuncio().getPrezzoVisita(), prenotazioneForm.getDataVisita(),
            prenotazioneForm.getNumeroPersone());

    Cliente cliente = sessionCliente.getCliente().get();
    cliente.addPrenotazione(prenotazione);

    visita.addPrenotazione(prenotazione);

    prenotazioneDao.save(prenotazione);

    return esitoPrenotazioneView;
  }
}
