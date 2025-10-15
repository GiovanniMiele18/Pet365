package it.unisa.justTraditions.applicationLogic.shopControl;

import it.unisa.justTraditions.storage.shopStorage.dao.FotoProdottoDao;
import it.unisa.justTraditions.storage.shopStorage.entity.FotoProdotto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

/**
 * Controller per la visualizzazione delle immagini dei prodotti.
 */
@Controller
@RequestMapping("/imgProdotto")
public class FotoProdottoController {

    @Autowired
    private FotoProdottoDao fotoProdottoDao;

    @GetMapping
    public void get(@RequestParam Long id, HttpServletResponse response) throws IOException {
        FotoProdotto foto = fotoProdottoDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Foto non trovata"));

        response.setContentType("image/jpeg");
        response.getOutputStream().write(foto.getDati());
    }
}
