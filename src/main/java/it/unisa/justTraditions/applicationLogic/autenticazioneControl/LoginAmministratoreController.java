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

@Controller
@RequestMapping("/loginAmministratore")
public class LoginAmministratoreController {

  private static final String loginView = "autenticazioneView/login";
  private static final String homeAmministratore = "/homeAmministratore";

  @Autowired
  private AmministratoreDao amministratoreDao;

  @Autowired
  private SessionAmministratore sessionAmministratore;

  @Autowired
  private PasswordEncryptor passwordEncryptor;

  @GetMapping
  public String get(@ModelAttribute LoginForm loginForm, Model model) {
    model.addAttribute("nameLogin", "/loginAmministratore");
    return loginView;
  }

  @PostMapping(consumes = "application/json", produces = "application/json")
  @ResponseBody
  public ResponseEntity<LoginResponse> postJson(@RequestBody @Valid LoginForm loginForm,
                                                BindingResult bindingResult,
                                                HttpSession session) {
    if (bindingResult.hasErrors()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new LoginResponse(false, "Campi non validi."));
    }

    Optional<Amministratore> optional = amministratoreDao.findByEmail(loginForm.getEmail());
    if (optional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new LoginResponse(false, "E-mail non registrata come amministratore."));
    }

    Amministratore amministratore = optional.get();
    if (!passwordEncryptor.checkPassword(loginForm.getPassword(), amministratore.getPassword())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new LoginResponse(false, "Password errata."));
    }

    sessionAmministratore.setAmministratore(amministratore);
    return ResponseEntity.ok(new LoginResponse(true, homeAmministratore));
  }

  public record LoginResponse(boolean success, String message) {}
}
