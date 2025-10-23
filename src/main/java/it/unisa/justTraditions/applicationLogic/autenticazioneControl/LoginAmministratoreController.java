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
   * Se l'amministratore è già loggato → redirect automatico alla home amministratore.
   */
  @GetMapping
  public String get(@ModelAttribute LoginForm loginForm, Model model) {
    // ✅ Se c’è un amministratore già in sessione → redirect
    if (sessionAmministratore.getAmministratore() != null) {
      return "redirect:" + homeAmministratoreController;
    }

    model.addAttribute("nameLogin", "/loginAmministratore");
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

    // Campi mancanti o non validi
    if (bindingResult.hasErrors()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new LoginResponse(false, "Campi non validi"));
    }

    Optional<Amministratore> optionalAmministratore = amministratoreDao.findByEmail(loginForm.getEmail());

    // Email non trovata
    if (optionalAmministratore.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new LoginResponse(false, "E-mail non registrata"));
    }

    Amministratore amministratore = optionalAmministratore.get();

    // Password errata
    if (!passwordEncryptor.checkPassword(loginForm.getPassword(), amministratore.getPassword())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new LoginResponse(false, "Password o e-mail errata"));
    }

    // ✅ Login corretto
    sessionAmministratore.setAmministratore(amministratore);


  /**
   * Piccola classe di risposta JSON.
   */
  public record LoginResponse(boolean success, String message) {}
}
