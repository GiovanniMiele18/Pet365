package it.unisa.justTraditions.applicationLogic.autenticazioneControl;

import it.unisa.justTraditions.applicationLogic.autenticazioneControl.form.LoginForm;
import it.unisa.justTraditions.applicationLogic.autenticazioneControl.util.PasswordEncryptor;
import it.unisa.justTraditions.applicationLogic.autenticazioneControl.util.SessionAmministratore;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.dao.AmministratoreDao;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Amministratore;
import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Implementa il controller per il login per l'Amministratore.
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
   * Mostra la pagina di login amministratore.
   */
  @GetMapping
  public String get(@ModelAttribute LoginForm loginForm, Model model) {
    // Se l’amministratore è già loggato → reindirizza alla home
    if (sessionAmministratore.getAmministratore().isPresent()) {
      return "redirect:" + homeAmministratoreController;
    }

    model.addAttribute("nameLogin", "/loginAmministratore");
    return loginView;
  }

  /**
   * Gestisce il login dell'amministratore (solo tabella amministratore).
   */
  @PostMapping
  public String post(@ModelAttribute @Valid LoginForm loginForm,
                     BindingResult bindingResult, Model model) {

    model.addAttribute("nameLogin", "/loginAmministratore");

    // ✅ Se ci sono errori di validazione (es. campi vuoti)
    if (bindingResult.hasErrors()) {
      return loginView;
    }

    // ✅ Cerca l’amministratore per email SOLO nella tabella amministratori
    Optional<Amministratore> optionalAmministratore =
        amministratoreDao.findByEmail(loginForm.getEmail());

    // ❌ Email non trovata
    if (optionalAmministratore.isEmpty()) {
      model.addAttribute("existsEmail", false);
      return loginView;
    }

    Amministratore amministratore = optionalAmministratore.get();

    // ❌ Password errata
    if (!passwordEncryptor.checkPassword(loginForm.getPassword(), amministratore.getPassword())) {
      model.addAttribute("passwordErrata", true);
      return loginView;
    }

    // ✅ Login corretto → salva l’amministratore in sessione
    sessionAmministratore.setAmministratore(amministratore);

    // ✅ Reindirizza alla home dell’amministratore
    return "redirect:" + homeAmministratoreController;
  }
}
