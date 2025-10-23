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
   * Mostra la pagina di login.
   * Se l'utente è già autenticato, viene reindirizzato alla home.
   */
  @GetMapping
  public String get(@ModelAttribute LoginForm loginForm, Model model) {
    // ✅ Impedisce di accedere alla pagina di login se già loggato
    if (sessionCliente.getCliente() != null) {
      return "redirect:" + homeController;
    }

    model.addAttribute("nameLogin", "/login");
    return loginView;
  }

  /**
   * Gestisce il login tramite chiamata AJAX (JSON) senza refresh della pagina.
   */
  @PostMapping(consumes = "application/json", produces = "application/json")
  @ResponseBody
  public ResponseEntity<LoginResponse> postJson(@RequestBody @Valid LoginForm loginForm,
                                                BindingResult bindingResult,
                                                HttpSession session) {

    // ❌ Campi non validi
    if (bindingResult.hasErrors()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(new LoginResponse(false, "Campi non validi"));
    }

    // 🔍 Verifica se esiste un cliente con l’email fornita
    Optional<Cliente> optionalCliente = clienteDao.findByEmail(loginForm.getEmail());

    if (optionalCliente.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(new LoginResponse(false, "E-mail non registrata"));
    }

    Cliente cliente = optionalCliente.get();

    // 🔒 Verifica la password cifrata
    if (!passwordEncryptor.checkPassword(loginForm.getPassword(), cliente.getPassword())) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new LoginResponse(false, "Password o e-mail errata"));
    }

    // ✅ Login corretto → salva il cliente in sessione
    sessionCliente.setCliente(cliente);

    // 🔁 Se esiste un redirect salvato prima del login, usa quello
    String redirectAfterLogin = (String) session.getAttribute("redirectAfterLogin");
    if (redirectAfterLogin != null) {
      session.removeAttribute("redirectAfterLogin");
      return ResponseEntity.ok(new LoginResponse(true, redirectAfterLogin));
    }

    // ✅ Altrimenti, redirect alla home di default
    return ResponseEntity.ok(new LoginResponse(true, homeController));
  }

  /**
   * Risposta JSON standard per le chiamate AJAX.
   */
  public record LoginResponse(boolean success, String message) {}
}
