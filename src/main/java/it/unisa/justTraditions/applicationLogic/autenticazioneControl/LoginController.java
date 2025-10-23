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

  @GetMapping
  public String get(@ModelAttribute LoginForm loginForm, Model model) {
    model.addAttribute("nameLogin", "/login");
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

    Optional<Cliente> optional = clienteDao.findByEmail(loginForm.getEmail());
    if (optional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new LoginResponse(false, "E-mail non registrata."));
    }

    Cliente cliente = optional.get();
    if (!passwordEncryptor.checkPassword(loginForm.getPassword(), cliente.getPassword())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new LoginResponse(false, "Password errata."));
    }

    sessionCliente.setCliente(cliente);
    return ResponseEntity.ok(new LoginResponse(true, home));
  }

  public record LoginResponse(boolean success, String message) {}
}
