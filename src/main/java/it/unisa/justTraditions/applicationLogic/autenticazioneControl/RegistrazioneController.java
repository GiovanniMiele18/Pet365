package it.unisa.justTraditions.applicationLogic.autenticazioneControl;

import it.unisa.justTraditions.applicationLogic.autenticazioneControl.form.RegistrazioneForm;
import it.unisa.justTraditions.applicationLogic.autenticazioneControl.util.PasswordEncryptor;
import it.unisa.justTraditions.applicationLogic.autenticazioneControl.util.SessionCliente;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.dao.ClienteDao;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Artigiano;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Cliente;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Controller per la registrazione di nuovi clienti o artigiani.
 */
@Controller
@RequestMapping("/registrazione")
public class RegistrazioneController {

  @Autowired
  private ClienteDao clienteDao;

  @Autowired
  private PasswordEncryptor passwordEncryptor;

  @Autowired
  private SessionCliente sessionCliente;

  private static final String LOGIN_REDIRECT = "/login";
  private static final String HOME_REDIRECT = "/";

  /**
   * ✅ Mostra la pagina di registrazione.
   * Se l'utente è già loggato → redirect alla home.
   */
  @GetMapping
  public String get(HttpSession session) {
    if (sessionCliente != null && sessionCliente.getCliente().isPresent()) {
      return "redirect:" + HOME_REDIRECT;
    }
    return "autenticazioneView/registrazione";
  }

  /**
   * ✅ Gestisce la registrazione AJAX (JSON)
   */
  @PostMapping(consumes = "application/json", produces = "application/json")
  @ResponseBody
  public ResponseEntity<RegistrazioneResponse> registra(
          @RequestBody @Valid RegistrazioneForm form,
          HttpSession session) {

    // 🔒 Se l’utente è già autenticato → blocca
    if (sessionCliente != null && sessionCliente.getCliente().isPresent()) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
          .body(new RegistrazioneResponse(false, "Hai già effettuato l'accesso.", HOME_REDIRECT));
    }

    try {
      // 🔨 Crea il cliente o artigiano
      Cliente cliente = form.isLavoratore()
          ? new Artigiano(
              form.getEmail(),
              passwordEncryptor.encryptPassword(form.getPassword()),
              form.getNome(),
              form.getCognome(),
              form.getCodiceFiscale())
          : new Cliente(
              form.getEmail(),
              passwordEncryptor.encryptPassword(form.getPassword()),
              form.getNome(),
              form.getCognome(),
              form.getCodiceFiscale());

      clienteDao.save(cliente);

      return ResponseEntity.ok(
          new RegistrazioneResponse(true, "Registrazione completata con successo!", LOGIN_REDIRECT)
      );

    } catch (DataIntegrityViolationException e) {
      // ⚠️ Email o codice fiscale duplicato
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body(new RegistrazioneResponse(false, "Email o codice fiscale già registrati.", null));

    } catch (Exception e) {
      // ⚠️ Altro errore generico
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new RegistrazioneResponse(false, "Errore interno del server.", null));
    }
  }

  /**
   * ✅ Oggetto di risposta JSON compatibile con il frontend
   */
  public record RegistrazioneResponse(boolean success, String message, String redirect) {}
}
