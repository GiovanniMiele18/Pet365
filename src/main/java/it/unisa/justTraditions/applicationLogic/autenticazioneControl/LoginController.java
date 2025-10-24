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
   * Mostra la pagina di login, gestendo eventuale redirect post-login.
   */
  @GetMapping
  public String get(@ModelAttribute LoginForm loginForm,
                    @RequestParam(value = "redirect", required = false) String redirect,
                    Model model,
                    HttpSession session) {

    // ✅ Se l’utente è già loggato, reindirizza subito
    if (sessionCliente != null && sessionCliente.getCliente().isPresent()) {
      return "redirect:" + home;
    }

    // 🔁 Salva temporaneamente il redirect nella sessione
    if (redirect != null && !redirect.isBlank()) {
      session.setAttribute("redirectAfterLogin", redirect);
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

    // ❌ Se l’utente è già autenticato
    if (sessionCliente != null && sessionCliente.getCliente().isPresent()) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
          .body(new LoginResponse(false, "Sei già autenticato."));
    }

    // ❌ Validazione
    if (bindingResult.hasErrors()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new LoginResponse(false, "Campi non validi."));
    }

    // 🔍 Cerca cliente
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

    // ✅ Login corretto
    sessionCliente.setCliente(cliente);

    // 🔁 Recupera il redirect salvato nella sessione
    String redirectAfterLogin = (String) session.getAttribute("redirectAfterLogin");
    if (redirectAfterLogin != null && !redirectAfterLogin.isBlank()) {
      session.removeAttribute("redirectAfterLogin");
      return ResponseEntity.ok(new LoginResponse(true, redirectAfterLogin));
    }

    return ResponseEntity.ok(new LoginResponse(true, home));
  }

  /**
   * Gestisce anche il login classico (non-AJAX, con form POST normale).
   */
  @PostMapping(consumes = "application/x-www-form-urlencoded")
  public String postForm(@ModelAttribute @Valid LoginForm loginForm,
                         BindingResult bindingResult,
                         @RequestParam(value = "redirect", required = false) String redirect,
                         HttpSession session,
                         Model model) {

    if (bindingResult.hasErrors()) {
      model.addAttribute("error", "Campi non validi.");
      return loginView;
    }

    Optional<Cliente> optional = clienteDao.findByEmail(loginForm.getEmail());
    if (optional.isEmpty()) {
      model.addAttribute("error", "E-mail non registrata.");
      return loginView;
    }

    Cliente cliente = optional.get();
    if (!passwordEncryptor.checkPassword(loginForm.getPassword(), cliente.getPassword())) {
      model.addAttribute("error", "Password o e-mail errata.");
      return loginView;
    }

    sessionCliente.setCliente(cliente);

    // 🔁 Redirect prioritario (parametro URL > sessione)
    if (redirect != null && !redirect.isBlank()) {
      return "redirect:" + redirect;
    }

    String redirectAfterLogin = (String) session.getAttribute("redirectAfterLogin");
    if (redirectAfterLogin != null && !redirectAfterLogin.isBlank()) {
      session.removeAttribute("redirectAfterLogin");
      return "redirect:" + redirectAfterLogin;
    }

    return "redirect:" + home;
  }

  public record LoginResponse(boolean success, String message) {}
}
