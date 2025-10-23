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
 * Controller per il login dei clienti.
 */
@Controller
@RequestMapping("/login")
public class LoginController {

  private static final String loginView = "autenticazioneView/login";
  private static final String home = "/";

  @Autowired
  private ClienteDao clienteDao;

  @Autowired
  private SessionCliente sessionCliente;

  @Autowired
  private PasswordEncryptor passwordEncryptor;

  /**
   * Mostra la pagina di login solo se l'utente non è autenticato.
   */
  @GetMapping
  public String get(@ModelAttribute LoginForm loginForm, Model model) {
    // ✅ Se il cliente è già loggato → redirect alla home
    if (sessionCliente != null && sessionCliente.getCliente() != null) {
      return "redirect:" + home;
    }

    model.addAttribute("nameLogin", "/login");
    return loginView;
  }

  /**
   * Gestisce il login AJAX (JSON) senza refresh della pagina.
   */
  @PostMapping(consumes = "application/json", produces = "application/json")
  @ResponseBody
  public ResponseEntity<LoginResponse> postJson(@RequestBody @Valid LoginForm loginForm,
                                                BindingResult bindingResult,
                                                HttpSession session) {
    // ❌ Se l’utente è già autenticato, blocca l’accesso
    if (sessionCliente != null && sessionCliente.getCliente() != null) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
          .body(new LoginResponse(false, "Sei già autenticato."));
    }

    // ❌ Validazione form
    if (bindingResult.hasErrors()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new LoginResponse(false, "Campi non validi."));
    }

    // 🔍 Cerca il cliente
    Optional<Cliente> optional = clienteDao.findByEmail(loginForm.getEmail());
    if (optional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new LoginResponse(false, "E-mail non registrata."));
    }

    Cliente cliente = optional.get();

    // 🔒 Verifica password
    if (!passwordEncryptor.checkPassword(loginForm.getPassword(), cliente.getPassword())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new LoginResponse(false, "Password o e-mail errata."));
    }

    // ✅ Login corretto → salva il cliente in sessione
    sessionCliente.setCliente(cliente);

    // 🔁 Reindirizza dove serviva o alla home
    String redirectAfterLogin = (String) session.getAttribute("redirectAfterLogin");
    if (redirectAfterLogin != null) {
      session.removeAttribute("redirectAfterLogin");
      return ResponseEntity.ok(new LoginResponse(true, redirectAfterLogin));
    }

    return ResponseEntity.ok(new LoginResponse(true, home));
  }

  public record LoginResponse(boolean success, String message) {}
}
