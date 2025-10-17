package it.unisa.justTraditions.applicationLogic.prenotazioniControl;

import it.unisa.justTraditions.storage.gestioneAnnunciStorage.dao.VisitaDao;
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

     @Autowired
    private VisitaDao visitaDao;

    @PostMapping("/elimina/{prenotazioneId}")
    public String eliminaPrenotazione(@PathVariable Long prenotazioneId, Model model) {
        Cliente cliente = sessionCliente.getCliente()
                .orElseThrow(() -> new RuntimeException("Cliente non loggato!"));

        Prenotazione prenotazione = prenotazioneDao.findById(prenotazioneId)
                .orElseThrow(() -> new RuntimeException("Prenotazione non trovata"));

        if (!prenotazione.getCliente().getId().equals(cliente.getId())) {
            model.addAttribute("message", "Non puoi eliminare una prenotazione che non ti appartiene!");
            return "error";
        }

         // ✅ Ripristina la validità della visita associata
        if (prenotazione.getVisita() != null) {
            prenotazione.getVisita().setValidita(true);
            visitaDao.save(prenotazione.getVisita());
        }

        // ✅ Elimina la prenotazione
        prenotazioneDao.delete(prenotazione);
        
        return ResponseEntity.ok("Prenotazione eliminata con successo ✅");
    }
}
