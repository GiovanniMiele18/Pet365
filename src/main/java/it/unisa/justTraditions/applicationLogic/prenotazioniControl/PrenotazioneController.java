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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/prenotazioni")
public class PrenotazioneController {

    @Autowired
    private PrenotazioneDao prenotazioneDao;

    @Autowired
    private SessionCliente sessionCliente;

     @Autowired
    private VisitaDao visitaDao;

   @DeleteMapping("/elimina/{prenotazioneId}")
@ResponseBody
public ResponseEntity<Map<String, Object>> eliminaPrenotazione(@PathVariable Long prenotazioneId) {
    Map<String, Object> response = new HashMap<>();
    try {
        Cliente cliente = sessionCliente.getCliente()
                .orElseThrow(() -> new RuntimeException("Cliente non loggato!"));

        Prenotazione prenotazione = prenotazioneDao.findById(prenotazioneId)
                .orElseThrow(() -> new RuntimeException("Prenotazione non trovata"));

        if (!prenotazione.getCliente().getId().equals(cliente.getId())) {
            response.put("success", false);
            response.put("message", "Non puoi eliminare una prenotazione che non ti appartiene!");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }

        if (prenotazione.getVisita() != null) {
            prenotazione.getVisita().setValidita(true);
            visitaDao.save(prenotazione.getVisita());
        }

        prenotazioneDao.delete(prenotazione);

        response.put("success", true);
        response.put("message", "Prenotazione eliminata con successo!");
        return ResponseEntity.ok(response);

    } catch (RuntimeException e) {
        response.put("success", false);
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}

}
