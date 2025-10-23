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
 * Implementa il controller per il login dell'Amministratore.
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
   * Mostra la pagina di login per l'amministratore.
   */
  @GetMapping
  public String get(@ModelAttribute LoginForm loginForm, Model model, HttpSession session) {
    // ✅ Se è già loggato, lo manda alla home
    Amministratore amministratore = sessionAmministratore.getAmministratore();
    if (amministratore != null && amministratore.getId() != null) {
      return "redirect:" + homeAmministratoreController;
    }

    model.addAttribute("nameLogin", "/loginAmministratore");
    return loginView;
  }

  /**
   * Gestisce il login tramite JSON (AJAX).
   */
  @PostMapping(consumes = "application/json", produces = "application/json")
  @ResponseBody
  public ResponseEntity<LoginResponse> postJson(
      @RequestBody @Valid LoginForm loginForm,
      BindingResult bindingResult,
      HttpSession session) {

    // ❌ Campi non validi
    if (bindingResult.hasErrors()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new LoginResponse(false, "Campi non validi"));
    }

    // ✅ Recupera l'amministratore tramite Optional
    Optional<Amministratore> optionalAmministratore = amministratoreDao.findByEmail(loginForm.getEmail());

    if (optionalAmministratore.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new LoginResponse(false, "E-mail non registrata"));
    }

    Amministratore amministratore = optionalAmministratore.get();

    // 🔒 Verifica la password cifrata
    if (!passwordEncryptor.checkPassword(loginForm.getPassword(), amministratore.getPassword())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new LoginResponse(false, "Password o e-mail errata"));
    }

    // ✅ Login corretto: salva in sessione
    sessionAmministratore.setAmministratore(amministratore);

    // 🔁 Se c’è un redirect salvato prima del login, lo usa
    String redirectAfterLogin = (String) session.getAttribute("redirectAfterLogin");
    if (redirectAfterLogin != null) {
      session.removeAttribute("redirectAfterLogin");
      return ResponseEntity.ok(new LoginResponse(true, redirectAfterLogin));
    }

    // ✅ Default: va alla home amministratore
    return ResponseEntity.ok(new LoginResponse(true, homeAmministratoreController));
  }

  /**
   * Risposta JSON standard per AJAX.
   */
  public record LoginResponse(boolean success, String message) {}
}
