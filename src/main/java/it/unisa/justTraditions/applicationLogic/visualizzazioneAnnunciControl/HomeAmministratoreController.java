package it.unisa.justTraditions.applicationLogic.visualizzazioneAnnunciControl;

import it.unisa.justTraditions.storage.gestioneAnnunciStorage.dao.AnnuncioDao;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Annuncio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Implementa il controller la visualizzazione della home degli amministratori.
 */
@Controller
@RequestMapping("/homeAmministratore")
public class HomeAmministratoreController {

  private static final String homeAmministratoreView =
      "visualizzazioneAnnunciView/homeAmministratore";

  @Autowired
  private AnnuncioDao annuncioDao;

  /**
   * Implementa la funzionalità di smistare
   * l Amministratore alle view di visualizzazioneAnnunciView/homeAmministratore.
   *
   * @param model Utilizzato per passare degli attributi alla view.
   * @return Restituisce la view da reindirizzare.
   */
  @GetMapping
  public String get(Model model) {
    model.addAttribute("annunci", annuncioDao.findAll(PageRequest.of(
        0,
        8,
        Sort.by(Sort.Direction.DESC, "id")
    )).getContent());

    model.addAttribute("annuncirifiutati", annuncioDao.countByStato(Annuncio.Stato.RIFIUTATO));
    model.addAttribute("annunciapprovati", annuncioDao.countByStato(Annuncio.Stato.APPROVATO));
    model.addAttribute("annunciproposti", annuncioDao.countByStato(Annuncio.Stato.PROPOSTO));
    model.addAttribute("annunciinrevisione", annuncioDao.countByStato(Annuncio.Stato.IN_REVISIONE));

    return homeAmministratoreView;
  }
}

