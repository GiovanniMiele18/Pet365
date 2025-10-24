package it.unisa.justTraditions.applicationLogic.visualizzazioneAnnunciControl;

import it.unisa.justTraditions.storage.gestioneAnnunciStorage.dao.AnnuncioDao;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Annuncio;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller per la visualizzazione dei dettagli di un annuncio.
 */
@Controller
@RequestMapping("/visualizzazioneDettagliAnnuncio")
public class VisualizzazioneDettagliAnnuncioController {

    private static final String VIEW_PATH =
            "visualizzazioneAnnunciView/visualizzazioneDettagliAnnuncio";

    @Autowired
    private AnnuncioDao annuncioDao;

    /**
     * Mostra i dettagli di un annuncio e prepara il link di ritorno per login/registrazione.
     *
     * @param id ID dell'annuncio da visualizzare.
     * @param request Oggetto HttpServletRequest usato per costruire l'URL corrente.
     * @return ModelAndView con annuncio, numero foto e URL corrente.
     */
    @GetMapping
    public ModelAndView get(@RequestParam Long id, HttpServletRequest request) {
        Annuncio annuncio = annuncioDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Annuncio non trovato"));

        if (!annuncio.getStato().equals(Annuncio.Stato.APPROVATO)) {
            throw new IllegalArgumentException("Annuncio non approvato");
        }

        // 🔗 Costruisce l’URL corrente (inclusa query string)
        String currentUrl = request.getRequestURI()
                + (request.getQueryString() != null ? "?" + request.getQueryString() : "");

        return new ModelAndView(VIEW_PATH)
                .addObject("annuncio", annuncio)
                .addObject("totalFoto", annuncio.getFoto().size())
                .addObject("currentUrl", currentUrl);
    }
}
