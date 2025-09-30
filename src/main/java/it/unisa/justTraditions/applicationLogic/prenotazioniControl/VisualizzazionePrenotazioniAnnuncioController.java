package it.unisa.justTraditions.applicationLogic.prenotazioniControl;

import it.unisa.justTraditions.applicationLogic.autenticazioneControl.util.SessionCliente;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.dao.AnnuncioDao;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Annuncio;
import it.unisa.justTraditions.storage.prenotazioniStorage.dao.PrenotazioneDao;
import it.unisa.justTraditions.storage.prenotazioniStorage.entity.Prenotazione;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Implementa il controller per la visualizzazione delle prenotazioni di un annuncio.
 */
@Controller
@RequestMapping("/visualizzazionePrenotazioniAnnuncio")
public class VisualizzazionePrenotazioniAnnuncioController {

  private static final String visualizzazionePrenotazioniAnnuncioView =
      "prenotazioniView/visualizzazionePrenotazioniAnnuncio";

  @Autowired
  private SessionCliente sessionCliente;

  @Autowired
  private PrenotazioneDao prenotazioneDao;

  @Autowired
  private AnnuncioDao annuncioDao;

  /**
   * Implementa la funzionalità di smistare
   * l artigliano sulla view di prenotazioniView/visualizzazionePrenotazioniAnnuncio.
   *
   * @param idAnnuncio Utilizzato per passare alla view l id dell annuncio.
   * @return Restituisce la view da reindirizzare.
   */
  @GetMapping
  public ModelAndView get(@RequestParam Long idAnnuncio) {
    return new ModelAndView(visualizzazionePrenotazioniAnnuncioView)
        .addObject("idAnnuncio", idAnnuncio);
  }

  /**
   * Implementa la funzionalità di visualizzazione
   * delle prenotazioni dell annuncio in una precisa data.
   *
   * @param idAnnuncio Utilizzato per la ricerca delle prenotazioni.
   * @param dataVisita Utilizzato per la ricerca delle prenotazioni.
   * @param pagina     Utilizzato per la paginazione della lista delle prenotazioni.
   * @param model      Utilizzato per passare degli attributi alla view.
   * @return Restituisce la view da reindirizzare.
   * @throws IllegalArgumentException se i dati non sono previsti dal sistema.
   */
  @PostMapping
  public String post(@RequestParam Long idAnnuncio,
                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                     LocalDate dataVisita,
                     @RequestParam(defaultValue = "0", required = false) Integer pagina,
                     Model model) {
    Annuncio annuncio = annuncioDao.findById(idAnnuncio).orElseThrow(IllegalArgumentException::new);
    if (!annuncio.getArtigiano().equals(sessionCliente.getCliente().get())) {
      throw new IllegalArgumentException();
    }

    Page<Prenotazione> prenotazionePage = prenotazioneDao.findByVisitaAnnuncioAndDataVisita(
        annuncio,
        dataVisita,
        PageRequest.of(
            pagina,
            20,
            Sort.by(Sort.Direction.DESC, "dataVisita")
        )
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

    model.addAttribute("prenotazioni", prenotazioni);
    model.addAttribute("pagina", pagina);
    model.addAttribute("pagineTotali", totalPages);
    model.addAttribute("idAnnuncio", idAnnuncio);
    model.addAttribute("dataVisita", dataVisita);

    return visualizzazionePrenotazioniAnnuncioView;
  }
}

