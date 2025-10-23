package it.unisa.justTraditions.applicationLogic.autenticazioneControl;

import it.unisa.justTraditions.applicationLogic.autenticazioneControl.form.LoginForm;
import it.unisa.justTraditions.applicationLogic.autenticazioneControl.util.PasswordEncryptor;
import it.unisa.justTraditions.applicationLogic.autenticazioneControl.util.SessionAmministratore;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.dao.AmministratoreDao;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Amministratore;
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
 * Controller per la gestione del login dell'Amministratore.
 */
@Controller
@RequestMapping("/loginAmministratore")
public class LoginAmministratoreController {

  private static final String loginView = "autenticazioneView/login";
  private static final String homeAmministratoreController = "/homeAmministratore";

  @Autowired
  private AmministratoreDao amministratoreDao;

  @Autowired
  private SessionAmministratore sessionAmministratore;

  @Autowired
  private PasswordEncryptor passwordEncryptor;

  /**
   * Mostra la pagina di login per l’amministratore.
   * Se è già autenticato → viene reindirizzato alla home.
   */
  @GetMapping
  public String get(@ModelAttribute LoginForm loginForm, Model model) {
    // ✅ Evita loop: se loggato, manda alla home
    if (sessionAmministratore.getAmministratore().isPresent()) {
      return "redirect:" + homeAmministratoreController;
    }

    model.addAttribute("nameLogin", "/loginAmministratore");
    return loginView;
  }

  /**
   * Gestisce il login via AJAX (JSON).
   */
  @PostMapping(consumes = "application/json", produces = "application/json")
  @ResponseBody
  public ResponseEntity<LoginResponse> postJson(@RequestBody @Valid LoginForm loginForm,
                                                BindingResult bindingResult,
                                                HttpSession session) {

    if (bindingResult.hasErrors()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new LoginResponse(false, "Campi non validi"));
    }

    // 🔍 Cerca l’amministratore nella tabella AMMINISTRATORE
    Optional<Amministratore> optionalAmministratore =
        amministratoreDao.findByEmail(loginForm.getEmail());

    // ❌ Email non trovata
    if (optionalAmministratore.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new LoginResponse(false, "E-mail non registrata"));
    }

    Amministratore amministratore = optionalAmministratore.get();

    // 🔒 Password errata
    if (!passwordEncryptor.checkPassword(loginForm.getPassword(), amministratore.getPassword())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new LoginResponse(false, "Password o e-mail errata"));
    }

    // ✅ Login corretto → salva in sessione
    sessionAmministratore.setAmministratore(amministratore);

    // 🔁 Se c’è un redirect salvato prima del login, usalo
    String redirectAfterLogin = (String) session.getAttribute("redirectAfterLogin");
    if (redirectAfterLogin != null) {
      session.removeAttribute("redirectAfterLogin");
      return ResponseEntity.ok(new LoginResponse(true, redirectAfterLogin));
    }

    // ✅ Default → home amministratore
    return ResponseEntity.ok(new LoginResponse(true, homeAmministratoreController));
  }

  /**
   * Gestisce anche il login via form classico (no AJAX),
   * utile se non si usa fetch() o se JavaScript è disattivato.
   */
  @PostMapping(consumes = "application/x-www-form-urlencoded")
  public String postForm(@ModelAttribute @Valid LoginForm loginForm,
                         BindingResult bindingResult,
                         Model model,
                         HttpSession session) {

    model.addAttribute("nameLogin", "/loginAmministratore");

    if (bindingResult.hasErrors()) {
      return loginView;
    }

    Optional<Amministratore> optionalAmministratore =
        amministratoreDao.findByEmail(loginForm.getEmail());

    if (optionalAmministratore.isEmpty()) {
      model.addAttribute("existsEmail", false);
      return loginView;
    }

    Amministratore amministratore = optionalAmministratore.get();

    if (!passwordEncryptor.checkPassword(loginForm.getPassword(), amministratore.getPassword())) {
      model.addAttribute("passwordErrata", true);
      return loginView;
    }

    // ✅ Login corretto
    sessionAmministratore.setAmministratore(amministratore);

    String redirectAfterLogin = (String) session.getAttribute("redirectAfterLogin");
    if (redirectAfterLogin != null) {
      session.removeAttribute("redirectAfterLogin");
      return "redirect:" + redirectAfterLogin;
    }

    return "redirect:" + homeAmministratoreController;
  }

  /**
   * Risposta JSON standard.
   */
  public record LoginResponse(boolean success, String message) {}
}
