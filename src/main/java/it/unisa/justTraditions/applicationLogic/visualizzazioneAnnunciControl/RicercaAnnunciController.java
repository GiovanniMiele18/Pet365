package it.unisa.justTraditions.applicationLogic.visualizzazioneAnnunciControl;

import it.unisa.justTraditions.applicationLogic.util.Province;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.dao.AnnuncioDao;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Annuncio;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
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

@Controller
@RequestMapping("/ricercaAnnunci")
public class RicercaAnnunciController {

  private static final String ricercaAnnunciView = "visualizzazioneAnnunciView/ricercaAnnunci";
  private static final String ricercaAnnunciFragment = "visualizzazioneAnnunciView/ricercaAnnunci :: risultati";

  @Autowired
  private AnnuncioDao annuncioDao;

  @Autowired
  private Province province;

  @GetMapping
  public String get(@RequestParam(defaultValue = "", required = false) String nomeAttivita,
                    @RequestParam(defaultValue = "", required = false) String provincia,
                    @RequestParam(defaultValue = "", required = false) String serviziOfferti,
                    @RequestParam(defaultValue = "0", required = false) Integer pagina,
                    Model model,
                    HttpServletRequest request) {

    if (!nomeAttivita.isBlank() && nomeAttivita.length() > 40) {
      throw new IllegalArgumentException("Nome attività troppo lungo");
    }
    if (!provincia.isBlank() && !province.getProvince().contains(provincia)) {
      throw new IllegalArgumentException("Provincia non esistente");
    }

    Annuncio annuncio = new Annuncio();
    annuncio.setNomeAttivita(nomeAttivita);
    annuncio.setProvinciaAttivita(provincia);
    annuncio.setStato(Annuncio.Stato.APPROVATO);

    if (!serviziOfferti.isBlank()) {
      try {
        annuncio.setServiziOfferti(Annuncio.ServizioOfferto.valueOf(serviziOfferti));
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("Servizio non valido");
      }
    }

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
    model.addAttribute("serviziOfferti", serviziOfferti);
    model.addAttribute("pagineTotali", totalPages);
    model.addAttribute("listaServizi", Annuncio.ServizioOfferto.values());

    // Se è una chiamata AJAX → restituisco solo il fragment
    if ("XMLHttpRequest".equals(request.getHeader("X-Requested-With"))) {
      return ricercaAnnunciFragment;
    }

    // Altrimenti → pagina intera
    return ricercaAnnunciView;
  }
}
