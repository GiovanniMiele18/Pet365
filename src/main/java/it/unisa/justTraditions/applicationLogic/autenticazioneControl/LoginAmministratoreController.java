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
   * Mostra la pagina di login dell’amministratore.
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

    Optional<Amministratore> optionalAmministratore =
        amministratoreDao.findByEmail(loginForm.getEmail());

    if (optionalAmministratore.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new LoginResponse(false, "E-mail non registrata"));
    }

    Amministratore amministratore = optionalAmministratore.get();

    if (!passwordEncryptor.checkPassword(loginForm.getPassword(), amministratore.getPassword())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new LoginResponse(false, "Password o e-mail errata"));
    }

    // ✅ Login corretto
    sessionAmministratore.setAmministratore(amministratore);

    String redirectAfterLogin = (String) session.getAttribute("redirectAfterLogin");
    if (redirectAfterLogin != null) {
      session.removeAttribute("redirectAfterLogin");
      return ResponseEntity.ok(new LoginResponse(true, redirectAfterLogin));
    }

    return ResponseEntity.ok(new LoginResponse(true, homeAmministratoreController));
  }

  /**
   * Gestisce il login via form classico (senza AJAX).
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
