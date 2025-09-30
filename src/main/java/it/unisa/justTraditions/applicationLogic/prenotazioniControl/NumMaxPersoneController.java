package it.unisa.justTraditions.applicationLogic.prenotazioniControl;

import it.unisa.justTraditions.storage.gestioneAnnunciStorage.dao.VisitaDao;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Visita;
import it.unisa.justTraditions.storage.prenotazioniStorage.dao.PrenotazioneDao;
import it.unisa.justTraditions.storage.prenotazioniStorage.entity.Prenotazione;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller per il trasferimento del dato Numero Max di Persone
 * che gia partecipano a una determinata visita in una specifica data.
 */
@Controller
@RequestMapping("/numMaxPersone")
public class NumMaxPersoneController {

  @Autowired
  private PrenotazioneDao prenotazioneDao;

  @Autowired
  private VisitaDao visitaDao;

  /**
   * Implementa la logica per il calcolo di persone che gia partecipano
   * a una visita in una specifica data.
   *
   * @param idVisita   Utilizzato per la ricerca di tutte le prenotazione della visita.
   * @param dataVisita Utilizzato per la ricerca di tutte le prenotazioni di una specifica data
   * @return Restituisce la view da reindirizzare.
   * @throws IllegalArgumentException se i dati non sono previsti dal sistema.
   */
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  private ResponseEntity<?> post(@RequestParam Long idVisita,
                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                 LocalDate dataVisita) {
    Visita visita = visitaDao.findById(idVisita).orElseThrow(IllegalArgumentException::new);

    int numeroPersonePrenotate = prenotazioneDao.findByVisitaAndDataVisita(visita, dataVisita)
        .stream()
        .mapToInt(Prenotazione::getNumPersonePrenotate)
        .sum();

    return ResponseEntity.ok(
        visita.getAnnuncio().getNumMaxPersonePerVisita() - numeroPersonePrenotate);
  }
}
