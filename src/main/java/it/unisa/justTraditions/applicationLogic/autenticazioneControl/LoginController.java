package it.unisa.justTraditions.applicationLogic.autenticazioneControl;

import it.unisa.justTraditions.applicationLogic.autenticazioneControl.form.LoginForm;
import it.unisa.justTraditions.applicationLogic.autenticazioneControl.util.PasswordEncryptor;
import it.unisa.justTraditions.applicationLogic.autenticazioneControl.util.SessionCliente;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.dao.ClienteDao;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Cliente;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Implementa il controller per il login del Cliente.
 */
@Controller
@RequestMapping("/login")
public class LoginController {

  private static final String loginView = "autenticazioneView/login";
  private static final String homeController = "/";

  @Autowired
  private ClienteDao clienteDao;

  @Autowired
  private SessionCliente sessionCliente;

  @Autowired
  private PasswordEncryptor passwordEncryptor;

  /**
   * Mostra la pagina di login classica.
   */
  @GetMapping
  public String get(@ModelAttribute LoginForm loginForm, Model model) {
    model.addAttribute("nameLogin", "/login");
    return loginView;
  }

  /**
   * Gestisce il login tramite chiamata AJAX (JSON) senza refresh.
   */
  @PostMapping(consumes = "application/json", produces = "application/json")
  @ResponseBody
  public ResponseEntity<?> postJson(@RequestBody @Valid LoginForm loginForm,
                                    BindingResult bindingResult,
                                    HttpSession session) {

    // Se ci sono errori di validazione (campo vuoto, ecc.)
    if (bindingResult.hasErrors()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new LoginResponse(false, "Campi non validi"));
    }

    Optional<Cliente> optionalCliente = clienteDao.findByEmail(loginForm.getEmail());

    // Email non trovata → 404
    if (optionalCliente.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new LoginResponse(false, "E-mail non registrata"));
    }

    Cliente cliente = optionalCliente.get();

    // Password errata → 401
    if (!passwordEncryptor.checkPassword(loginForm.getPassword(), cliente.getPassword())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new LoginResponse(false, "Password o e-mail errata"));
    }

    // ✅ Login corretto
    sessionCliente.setCliente(cliente);

    String redirectAfterLogin = (String) session.getAttribute("redirectAfterLogin");
    if (redirectAfterLogin != null) {
      session.removeAttribute("redirectAfterLogin");
      return ResponseEntity.ok(new LoginResponse(true, redirectAfterLogin));
    }

    // Redirect di default alla home
    return ResponseEntity.ok(new LoginResponse(true, homeController));
  }

  /**
   * Piccola classe di risposta JSON.
   */
  public record LoginResponse(boolean success, String message) {}
}
