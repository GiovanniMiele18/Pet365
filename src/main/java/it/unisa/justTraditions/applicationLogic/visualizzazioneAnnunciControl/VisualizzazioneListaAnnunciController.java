package it.unisa.justTraditions.applicationLogic.visualizzazioneAnnunciControl;

import it.unisa.justTraditions.storage.gestioneAnnunciStorage.dao.AnnuncioDao;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Annuncio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Implementa il controller per la visualizzazione della lista degli annunci.
 */
@Controller
@RequestMapping("/visualizzazioneListaAnnunci")
public class VisualizzazioneListaAnnunciController {

  private static final String visualizzazioneListaAnnunciView =
      "visualizzazioneAnnunciView/visualizzazioneListaAnnunci";

  @Autowired
  AnnuncioDao annuncioDao;

  /**
   * Implementa la funzionalità ricerca di liste di annunci è di smistare
   * l'amministratore nella view di visualizzazioneAnnunciView/visualizzazioneListaAnnunci.
   *
   * @param stato  Utilizzato per filtrare gli annunci per Stato.
   * @param pagina Utilizzata per l'impaginazione della lista di annunci.
   * @param model  Utilizzato per passare degli attributi alla view.
   * @return Restituisce la view da reindirizzare.
   * @throws IllegalArgumentException se i dati non sono previsti dal sistema.
   */
  @GetMapping
  public String get(@RequestParam(required = false) Annuncio.Stato stato,
                    @RequestParam(defaultValue = "0", required = false) Integer pagina,
                    Model model) {

    Annuncio annuncio = new Annuncio();
    annuncio.setStato(stato);

    Page<Annuncio> annuncioPage = annuncioDao.findAll(
        Example.of(annuncio),
        PageRequest.of(pagina, 20, Sort.by(Sort.Direction.DESC, "id"))
    );

    List<Annuncio> annunci;

    int totalPages = annuncioPage.getTotalPages();
    if (totalPages == 0) {
      annunci = List.of();
    } else if (totalPages <= pagina) {
      throw new IllegalArgumentException();
    } else {
      annunci = annuncioPage.getContent();
    }

    model.addAttribute("annunci", annunci);
    model.addAttribute("pagina", pagina);
    model.addAttribute("pagineTotali", totalPages);

    return visualizzazioneListaAnnunciView;
  }
}