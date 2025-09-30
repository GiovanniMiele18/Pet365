package it.unisa.justTraditions.applicationLogic.visualizzazioneAnnunciControl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Implementa il controller per la visualizzazione della pagina chi siamo.
 */
@Controller
@RequestMapping("/chisiamo")
public class ChiSiamoController {

  private static final String chisiamoView = "visualizzazioneAnnunciView/chisiamo";

  /**
   * Implementa la funzionalità di smistare l utente nella view di
   * visualizzazioneAnnunciView/chisiamo.
   *
   * @return ModelAndView(visualizzazioneAnnunciView / chisiamo).
   */
  @GetMapping
  public ModelAndView get() {
    return new ModelAndView(chisiamoView);
  }
}

