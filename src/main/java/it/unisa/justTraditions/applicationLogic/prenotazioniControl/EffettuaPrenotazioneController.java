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
    private static final String ERROR_VIEW = "error";

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
            model.addAttribute("message", "Errore nei dati della prenotazione.");
            return ERROR_VIEW;
        }

        // 🔒 Verifica login
        Cliente cliente = sessionCliente.getCliente().orElse(null);
        if (cliente == null) {
            model.addAttribute("message", "Devi effettuare il login per completare la prenotazione.");
            return ERROR_VIEW;
        }

        // 🔍 Recupera la visita
        Visita visita = visitaDao.findById(prenotazioneForm.getIdVisita())
                .orElseThrow(() -> new IllegalArgumentException("Visita non trovata"));

        // 🔍 Recupera l'animale
        Animale animale = animaleDao.findById(prenotazioneForm.getIdAnimale())
                .orElseThrow(() -> new IllegalArgumentException("Animale non trovato"));

        // ✅ Verifica che l'animale appartenga al cliente loggato
        if (!animale.getCliente().getId().equals(cliente.getId())) {
            model.addAttribute("message", "Non puoi prenotare per un animale che non ti appartiene.");
            return ERROR_VIEW;
        }

        // Prepara la view di riepilogo
        model.addAttribute("prenotazioneForm", prenotazioneForm);
        model.addAttribute("orarioinizio", visita.getOrarioInizio());
        model.addAttribute("orariofine", visita.getOrarioFine());
        model.addAttribute("idAnnuncio", visita.getAnnuncio().getId());
        model.addAttribute("animale", animale);

        return EFFETTUA_PRENOTAZIONE_VIEW;
    }

    /**
     * Conferma la prenotazione (submit POST).
     */
    @PostMapping
    public String confermaPrenotazione(@ModelAttribute @Valid PrenotazioneForm prenotazioneForm,
                                       BindingResult bindingResult,
                                       Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("message", "Errore nella sottomissione della prenotazione.");
            return ERROR_VIEW;
        }

        // 🔒 Verifica login
        Cliente cliente = sessionCliente.getCliente().orElse(null);
        if (cliente == null) {
            model.addAttribute("message", "Devi essere loggato per completare la prenotazione.");
            return ERROR_VIEW;
        }

        // Recupera la visita
        Visita visita = visitaDao.findById(prenotazioneForm.getIdVisita())
                .orElseThrow(() -> new IllegalArgumentException("Visita non trovata"));

        // 🔍 Controlla se la visita è valida
        if (!visita.getValidita()) {
            model.addAttribute("message", "La visita selezionata non è più disponibile per la prenotazione.");
            return ERROR_VIEW;
        }

        // Recupera l'animale
        Animale animale = animaleDao.findById(prenotazioneForm.getIdAnimale())
                .orElseThrow(() -> new IllegalArgumentException("Animale non trovato"));

        // ✅ Controlla che l'animale appartenga al cliente loggato
        if (!animale.getCliente().getId().equals(cliente.getId())) {
            model.addAttribute("message", "L'animale selezionato non appartiene al tuo profilo.");
            return ERROR_VIEW;
        }

        // Crea la prenotazione
        Prenotazione prenotazione = new Prenotazione(visita, animale, prenotazioneForm.getDataVisita());
        prenotazione.setCliente(cliente);

        // Relazioni bidirezionali
        cliente.addPrenotazione(prenotazione);
        visita.addPrenotazione(prenotazione);

        // ❌ Disattiva la visita (non più disponibile)
        visita.setValidita(false);

        // Salva nel database
        prenotazioneDao.save(prenotazione);
        visitaDao.save(visita);

        return ESITO_PRENOTAZIONE_VIEW;
    }
}
