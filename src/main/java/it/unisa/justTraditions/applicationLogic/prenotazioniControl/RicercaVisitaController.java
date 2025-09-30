package it.unisa.justTraditions.applicationLogic.prenotazioniControl;

import it.unisa.justTraditions.applicationLogic.prenotazioniControl.json.VisitaResponse;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.dao.AnnuncioDao;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.dao.VisitaDao;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Annuncio;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Implementa il controller per la ricerca della visita.
 */
@Controller
@RequestMapping("/ricercaVisita")
public class RicercaVisitaController {

  @Autowired
  private VisitaDao visitaDao;

  @Autowired
  private AnnuncioDao annuncioDao;

  /**
   * Implementa la logica per la ricerca delle visite di un annuncio un una determinata data.
   *
   * @param idAnnuncio Utilizzata per la ricerca delle visite.
   * @param dataVisita Utilizzata per la ricerca della visita.
   * @return ResponseEntity con una lista vuota se l annuncio non esiste,
   *     se la data e uguale o minore della data odierna o non ci sono visite per quella data.
   *     ResponseEntity con una lista di visite se la ricerca ha avuto successo.
   */
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  private ResponseEntity<?> post(@RequestParam Long idAnnuncio,
                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                 LocalDate dataVisita) {
    Optional<Annuncio> optionalAnnuncio = annuncioDao.findById(idAnnuncio);
    if (optionalAnnuncio.isEmpty()) {
      return ResponseEntity.ok(List.of());
    }
    if (!dataVisita.isAfter(LocalDate.now())) {
      return ResponseEntity.ok(List.of());
    }

    Annuncio annuncio = optionalAnnuncio.get();
    List<VisitaResponse> visitaResponses =
        visitaDao.findByAnnuncioAndGiornoAndValiditaTrue(annuncio, dataVisita.getDayOfWeek())
            .stream()
            .map(v -> new VisitaResponse(v.getId(), v.getOrarioInizio(), v.getOrarioFine()))
            .toList();

    return ResponseEntity.ok(visitaResponses);
  }
}
