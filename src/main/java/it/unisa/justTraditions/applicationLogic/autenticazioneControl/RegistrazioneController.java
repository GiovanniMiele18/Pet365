package it.unisa.justTraditions.applicationLogic.autenticazioneControl;

import it.unisa.justTraditions.applicationLogic.autenticazioneControl.form.RegistrazioneForm;
import it.unisa.justTraditions.applicationLogic.autenticazioneControl.util.PasswordEncryptor;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.dao.ClienteDao;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Artigiano;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Cliente;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/registrazione")
public class RegistrazioneController {

  @Autowired
  private ClienteDao clienteDao;

  @Autowired
  private PasswordEncryptor passwordEncryptor;

  private static final String LOGIN_REDIRECT = "/login";

  @GetMapping
  public String get() {
    return "autenticazioneView/registrazione";
  }

  @PostMapping(consumes = "application/json", produces = "application/json")
  @ResponseBody
  public ResponseEntity<RegistrazioneResponse> registra(@RequestBody @Valid RegistrazioneForm form) {
    try {
      Cliente cliente = form.isLavoratore()
          ? new Artigiano(form.getEmail(), passwordEncryptor.encryptPassword(form.getPassword()),
              form.getNome(), form.getCognome(), form.getCodiceFiscale())
          : new Cliente(form.getEmail(), passwordEncryptor.encryptPassword(form.getPassword()),
              form.getNome(), form.getCognome(), form.getCodiceFiscale());

      clienteDao.save(cliente);

      return ResponseEntity.ok(
          new RegistrazioneResponse(true, "Registrazione completata con successo!", LOGIN_REDIRECT)
      );

    } catch (DataIntegrityViolationException e) {
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body(new RegistrazioneResponse(false, "Email o codice fiscale già registrati.", null));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new RegistrazioneResponse(false, "Errore interno del server.", null));
    }
  }

  public record RegistrazioneResponse(boolean success, String message, String redirect) {}
}
