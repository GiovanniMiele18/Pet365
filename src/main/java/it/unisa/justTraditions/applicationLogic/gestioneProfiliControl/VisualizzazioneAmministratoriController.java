package it.unisa.justTraditions.applicationLogic.gestioneProfiliControl;

import it.unisa.justTraditions.storage.gestioneProfiliStorage.dao.AmministratoreDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Implementa il controller per la visualizzazione di una lista di Amministratori.
 */
@Controller
@RequestMapping("/visualizzazioneAmministratori")
public class VisualizzazioneAmministratoriController {

  private static final String visualizzazioneAmministratoriView =
      "gestioneProfiliView/visualizzazioneAmministratori";

  @Autowired
  private AmministratoreDao amministratoreDao;

  /**
   * Implementa la funzionalità di smistare l Amministratore
   * su la view di gestioneProfiliView/visualizzazioneAmministratori.
   *
   * @return ModelAndView(gestioneProfiliView / visualizzazioneAmministratori)
   *     con la lista degli Amministratori.
   */
  @GetMapping
  public ModelAndView get() {
    return new ModelAndView(visualizzazioneAmministratoriView)
        .addObject("Amministratori", amministratoreDao.findAll());
  }
}
