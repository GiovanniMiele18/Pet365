package it.unisa.justTraditions.applicationLogic.visualizzazioneAnnunciControl;

import it.unisa.justTraditions.applicationLogic.util.Province;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.dao.AnnuncioDao;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Annuncio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Implementa il controller per la ricerca di un annuncio.
 */
@Controller
@RequestMapping("/ricercaAnnunci")
public class RicercaAnnunciController {

  private static final String ricercaAnnunciView = "visualizzazioneAnnunciView/ricercaAnnunci";

  @Autowired
  private AnnuncioDao annuncioDao;

  @Autowired
  private Province province;

  /**
   * Implementa la funzionalità della ricerca di un annuncio con o senza filtro province.
   *
   * @param nomeAttivita Utilizzato per la ricerca degli annunci.
   * @param provincia    Utilizzato per il filtro per provincie per gli annunci.
   * @param pagina       Utilizzata per la paginazione della lista di annunci.
   * @param model        Utilizzato per passare degli attributi alla view.
   * @return Restituisce la view da reindirizzare.
   * @throws IllegalArgumentException se i dati non sono previsti dal sistema.
   */
  @GetMapping
  public String get(@RequestParam(defaultValue = "", required = false) String nomeAttivita,
                    @RequestParam(defaultValue = "", required = false) String provincia,
                    @RequestParam(defaultValue = "0", required = false) Integer pagina,
                    Model model) {
    if (!nomeAttivita.isBlank() && nomeAttivita.length() > 40) {
      throw new IllegalArgumentException(
          "La ricerca degli annunci non va a buon fine poiché"
              + " il nome inserito dall’utente è troppo lungo.");
    }
    if (!provincia.isBlank() && !province.getProvince().contains(provincia)) {
      throw new IllegalArgumentException("Provincia non esistente");
    }

    Annuncio annuncio = new Annuncio();
    annuncio.setNomeAttivita(nomeAttivita);
    annuncio.setProvinciaAttivita(provincia);
    annuncio.setStato(Annuncio.Stato.APPROVATO);

    Example<Annuncio> annuncioExample = Example.of(
        annuncio,
        ExampleMatcher.matching()
            .withIgnoreCase()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
    );

    Page<Annuncio> annuncioPage = annuncioDao.findAll(
        annuncioExample,
        PageRequest.of(pagina, 20, Sort.by(Sort.Direction.ASC, "nomeAttivita"))
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
    model.addAttribute("nomeAttivita", nomeAttivita);
    model.addAttribute("provincia", provincia);
    model.addAttribute("pagineTotali", totalPages);

    return ricercaAnnunciView;
  }
}