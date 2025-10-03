package it.unisa.justTraditions.applicationLogic.prenotazioniControl;

import it.unisa.justTraditions.applicationLogic.autenticazioneControl.util.SessionCliente;
import it.unisa.justTraditions.applicationLogic.prenotazioniControl.form.PrenotazioneForm;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.dao.VisitaDao;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Visita;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.dao.AnimaleDao;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Animale;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Cliente;
import it.unisa.justTraditions.storage.prenotazioniStorage.dao.PrenotazioneDao;
import it.unisa.justTraditions.storage.prenotazioniStorage.entity.Prenotazione;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/effettuaPrenotazione")
public class EffettuaPrenotazioneController {

  private static final String EFFETTUA_PRENOTAZIONE_VIEW = "prenotazioniView/effettuaPrenotazione";
  private static final String ESITO_PRENOTAZIONE_VIEW = "prenotazioniView/esitoPrenotazione";

  @Autowired
  private VisitaDao visitaDao;

  @Autowired
  private AnimaleDao animaleDao;

  @Autowired
  private PrenotazioneDao prenotazioneDao;

  @Autowired
  private SessionCliente sessionCliente;

  /**
   * Mostra la pagina di riepilogo prenotazione.
   */
  @GetMapping
  public String getRiepilogoPrenotazione(@ModelAttribute @Valid PrenotazioneForm prenotazioneForm,
                                         BindingResult bindingResult,
                                         Model model) {
    if (bindingResult.hasErrors()) {
      throw new IllegalArgumentException("Errore nella sottomissione della prenotazione (GET)");
    }

    // Recupera la visita
    Visita visita = visitaDao.findById(prenotazioneForm.getIdVisita())
            .orElseThrow(() -> new IllegalArgumentException("Visita non trovata"));

    // Recupera l'animale
    Animale animale = animaleDao.findById(prenotazioneForm.getIdAnimale())
            .orElseThrow(() -> new IllegalArgumentException("Animale non trovato"));

    // Aggiunge dati al model
    model.addAttribute("prenotazioneForm", prenotazioneForm);
    model.addAttribute("orarioinizio", visita.getOrarioInizio());
    model.addAttribute("orariofine", visita.getOrarioFine());
    model.addAttribute("idAnnuncio", visita.getAnnuncio().getId());
    model.addAttribute("animale", animale);

    return EFFETTUA_PRENOTAZIONE_VIEW;
  }

  //Elimino la prenotazione dato l'id
  @PostMapping("/elimina/{prenotazioneId}")
  public String eliminaPrenotazione(@PathVariable Long prenotazioneId, Model model) {
    Cliente cliente = sessionCliente.getCliente()
            .orElseThrow(() -> new RuntimeException("Cliente non loggato"));

    Prenotazione prenotazione = prenotazioneDao.findById(prenotazioneId)
            .orElseThrow(() -> new RuntimeException("Prenotazione non trovata"));

    // Verifica che la prenotazione appartenga al cliente loggato
    if (!prenotazione.getCliente().getId().equals(cliente.getId())) {
      model.addAttribute("message", "Non puoi eliminare una prenotazione che non ti appartiene!");
      return "error";
    }

    prenotazioneDao.delete(prenotazione);

    return "redirect:/visualizzazioneProfiloPersonale";
  }

  /**
   * Effettua la prenotazione (submit POST).
   */
  @PostMapping
  public String confermaPrenotazione(@ModelAttribute @Valid PrenotazioneForm prenotazioneForm,
                                     BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      throw new IllegalArgumentException("Errore nella sottomissione della prenotazione (POST)");
    }

    // Recupera la visita
    Visita visita = visitaDao.findById(prenotazioneForm.getIdVisita())
            .orElseThrow(() -> new IllegalArgumentException("Visita non trovata"));

    // Recupera l'animale
    Animale animale = animaleDao.findById(prenotazioneForm.getIdAnimale())
            .orElseThrow(() -> new IllegalArgumentException("Animale non trovato"));

    // Recupera il cliente loggato
    Cliente cliente = sessionCliente.getCliente()
            .orElseThrow(() -> new IllegalArgumentException("Cliente non loggato"));

    // Crea la prenotazione
    Prenotazione prenotazione = new Prenotazione(visita, animale, prenotazioneForm.getDataVisita());
    prenotazione.setCliente(cliente);

    // Aggiunge relazioni bidirezionali
    cliente.addPrenotazione(prenotazione);
    visita.addPrenotazione(prenotazione);

    // Salva nel database
    prenotazioneDao.save(prenotazione);

    return ESITO_PRENOTAZIONE_VIEW;
  }
}
