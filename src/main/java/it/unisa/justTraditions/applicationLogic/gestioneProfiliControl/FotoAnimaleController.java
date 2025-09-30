package it.unisa.justTraditions.applicationLogic.gestioneProfiliControl;

import it.unisa.justTraditions.applicationLogic.autenticazioneControl.util.SessionCliente;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.dao.FotoDao;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Foto;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.dao.FotoAnimaleDao;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Cliente;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.FotoAnimale;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Implementa il controller per la visualizzazione delle foto.
 */
@Controller
@RequestMapping("/immagineanimale")
public class FotoAnimaleController {

    @Autowired
    private FotoAnimaleDao fotoAnimaleDao;

    @Autowired
    private SessionCliente sessionCliente;

    @GetMapping
    public void get(@RequestParam Long id, HttpServletResponse response) throws IOException {
        // 1. Recupero cliente loggato
        Cliente clienteAutenticato = sessionCliente.getCliente().orElse(null);
        if (clienteAutenticato == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Devi effettuare il login");
            return;
        }

        // 2. Recupero foto dal DAO
        FotoAnimale fotoAnimale = fotoAnimaleDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Foto non trovata"));

        // 3. Verifico che la foto appartenga al cliente loggato
        Cliente proprietario = fotoAnimale.getAnimale().getCliente();
        if (!proprietario.getId().equals(clienteAutenticato.getId())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Non hai accesso a questa foto");
            return;
        }

        // 4. Invio immagine
        response.setContentType("image/jpeg");
        response.getOutputStream().write(fotoAnimale.getDati());
    }
}