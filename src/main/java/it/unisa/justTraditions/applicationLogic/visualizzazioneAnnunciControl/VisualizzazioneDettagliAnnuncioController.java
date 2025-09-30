package it.unisa.justTraditions.applicationLogic.visualizzazioneAnnunciControl;

import it.unisa.justTraditions.storage.gestioneAnnunciStorage.dao.AnnuncioDao;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Annuncio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Implementa il controller per la visualizzazione dei dettagli di un annuncio.
 */
@Controller
@RequestMapping("/visualizzazioneDettagliAnnuncio")
public class VisualizzazioneDettagliAnnuncioController {

  private static final String visualizzazioneDettagliAnnuncioView =
      "visualizzazioneAnnunciView/visualizzazioneDettagliAnnuncio";

  @Autowired
  private AnnuncioDao annuncioDao;

  /**
   * Implementa la funzionalità di smistate l utente
   * nella view di visualizzazioneAnnunciView/visualizzazioneDettagliAnnuncio.
   *
   * @param id Utilizzato per la ricerca dell annuncio nel database.
   * @return ModelAndView(visualizzazioneAnnunciView / visualizzazioneDettagliAnnuncio)
   *     .addObject("totalFoto", annuncio.getFoto().size())
   *     .addObject("annuncio", annuncio);
   * @throws IllegalArgumentException se i dati non sono previsti dal sistema.
   */
  @GetMapping
  public ModelAndView get(@RequestParam Long id) {
    Annuncio annuncio = annuncioDao.findById(id).orElseThrow(IllegalArgumentException::new);
    if (!annuncio.getStato().equals(Annuncio.Stato.APPROVATO)) {
      throw new IllegalArgumentException();
    }

    return new ModelAndView(visualizzazioneDettagliAnnuncioView)
        .addObject("totalFoto", annuncio.getFoto().size())
        .addObject("annuncio", annuncio);
  }
}

