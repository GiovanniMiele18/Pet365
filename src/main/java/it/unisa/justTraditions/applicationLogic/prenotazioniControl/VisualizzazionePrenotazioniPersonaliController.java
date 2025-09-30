package it.unisa.justTraditions.applicationLogic.prenotazioniControl;


import it.unisa.justTraditions.applicationLogic.autenticazioneControl.util.SessionCliente;
import it.unisa.justTraditions.storage.prenotazioniStorage.dao.PrenotazioneDao;
import it.unisa.justTraditions.storage.prenotazioniStorage.entity.Prenotazione;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Implementa il controller per la visualizzazione delle prenotazioni personali.
 */
@Controller
@RequestMapping("/visualizzazionePrenotazioniPersonali")
public class VisualizzazionePrenotazioniPersonaliController {

  private static final String visualizzazionePrenotazioniPersonaliView =
      "prenotazioniView/visualizzazionePrenotazioniPersonali";

  @Autowired
  private SessionCliente sessionCliente;

  @Autowired
  private PrenotazioneDao prenotazioneDao;

  /**
   * Implementa la funzionalità di smistare il Cliente
   * sulla view di prenotazioniView/visualizzazionePrenotazioniPersonali.
   *
   * @param pagina Utilizzata per la paginazione della lista delle prenotazioni.
   * @return Restituisce la view da reindirizzare.
   * @throws IllegalArgumentException se i dati non sono previsti dal sistema.
   */
  @GetMapping
  public ModelAndView get(@RequestParam(defaultValue = "0", required = false) Integer pagina) {
    Page<Prenotazione> prenotazionePage = prenotazioneDao.findByCliente(
        sessionCliente.getCliente().get(),
        PageRequest.of(pagina, 20, Sort.by(Sort.Direction.DESC, "dataVisita"))
    );

    List<Prenotazione> prenotazioni;

    int totalPages = prenotazionePage.getTotalPages();
    if (totalPages == 0) {
      prenotazioni = List.of();
    } else if (totalPages <= pagina) {
      throw new IllegalArgumentException();
    } else {
      prenotazioni = prenotazionePage.getContent();
    }

    return new ModelAndView(visualizzazionePrenotazioniPersonaliView)
        .addObject("prenotazioni", prenotazioni)
        .addObject("pagina", pagina)
        .addObject("pagineTotali", totalPages);
  }
}
