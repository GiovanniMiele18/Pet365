package it.unisa.justTraditions.applicationLogic.prenotazioniControl;

import it.unisa.justTraditions.applicationLogic.autenticazioneControl.util.SessionCliente;
import it.unisa.justTraditions.storage.prenotazioniStorage.dao.PrenotazioneDao;
import it.unisa.justTraditions.storage.prenotazioniStorage.entity.Prenotazione;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/prenotazioni")
public class PrenotazioneController {

    @Autowired
    private PrenotazioneDao prenotazioneDao;

    @Autowired
    private SessionCliente sessionCliente;

    /**
     * POST → Elimina una prenotazione esistente
     */
    @PostMapping("/elimina/{prenotazioneId}")
    public String eliminaPrenotazione(@PathVariable Long prenotazioneId, Model model) {
        // Recupera il cliente loggato
        Cliente cliente = sessionCliente.getCliente()
                .orElseThrow(() -> new RuntimeException("Cliente non loggato"));

        // Recupera la prenotazione
        Prenotazione prenotazione = prenotazioneDao.findById(prenotazioneId)
                .orElseThrow(() -> new RuntimeException("Prenotazione non trovata"));

        // Controlla che la prenotazione appartenga al cliente
        if (!prenotazione.getCliente().getId().equals(cliente.getId())) {
            model.addAttribute("message", "Non puoi eliminare una prenotazione che non ti appartiene!");
            return "error";
        }

        // Elimina la prenotazione
        prenotazioneDao.delete(prenotazione);

        // Torna al profilo personale
        return "redirect:/visualizzazioneProfiloPersonale";
    }
}
