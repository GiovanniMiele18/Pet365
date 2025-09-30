package it.unisa.justTraditions.applicationLogic.gestioneProfiliControl;

import it.unisa.justTraditions.storage.gestioneAnnunciStorage.dao.AnnuncioDao;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Annuncio;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.dao.ArtigianoDao;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Artigiano;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Implementa il controller per la visualizzazione di un profilo di un artigiano.
 */
@Controller
@RequestMapping("/visualizzazioneProfiloArtigiano")
public class VisualizzazioneProfiloArtigianoController {

  private static final String visualizzazioneProfiloArtigianoView =
      "gestioneProfiliView/visualizzazioneProfiloArtigiano";

  @Autowired
  private ArtigianoDao artigianoDao;

  @Autowired
  private AnnuncioDao annuncioDao;

  /**
   * Implementa la funzionalità di smistare l'Utente
   * su la view di gestioneProfiliView/visualizzazioneProfiloArtigiano.
   * Per la visualizzazione del profilo dell Artigiano.
   *
   * @param id     Utilizzato per la ricerca di un Artigiano nell database.
   * @param pagina Utilizzato per la paginazione della lista degli annunci dell Artigiano.
   * @param model  Utilizzato per passare degli attributi alla view.
   * @return Restituisce la view da reindirizzare.
   * @throws IllegalArgumentException se i dati non sono previsti dal sistema.
   */
  @GetMapping
  public String get(@RequestParam Long id,
                    @RequestParam(defaultValue = "0", required = false) Integer pagina,
                    Model model) {
    Artigiano artigiano = artigianoDao.findById(id).orElseThrow(IllegalArgumentException::new);

    Page<Annuncio> annuncioPage = annuncioDao.findByArtigiano(
        artigiano,
        PageRequest.of(pagina, 4, Sort.by(Sort.Direction.DESC, "id"))
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
    model.addAttribute("artigiano", artigiano);

    return visualizzazioneProfiloArtigianoView;
  }
}
