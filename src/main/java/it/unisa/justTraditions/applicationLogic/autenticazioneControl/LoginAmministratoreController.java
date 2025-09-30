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
 * Implementa il controller per il login per L'Amministratore.
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
   * Implementa la funzionalità di smistare l'Amministratore sulla view di autenticazioneView/login.
   *
   * @param loginForm Utilizzato per mappare il Form della view.
   * @param model     Utilizzato per passare degli attributi alla view.
   * @return Restituisce la view da reindirizzare.
   */
  @GetMapping
  public String get(@ModelAttribute LoginForm loginForm, Model model) {
    model.addAttribute("nameLogin", "/loginAmministratore");
    return loginView;
  }

  /**
   * Implementa la funzionalità di login di un amministratore.
   *
   * @param loginForm     Utilizzato per mappare il Form della view.
   * @param bindingResult Utilizzato per mappare gli errori dei dati di loginForm
   * @param model         Utilizzato per passare degli attributi alla view.
   * @return Restituisce la view da reindirizzare.
   */
  @PostMapping
  public String post(@ModelAttribute @Valid LoginForm loginForm,
                     BindingResult bindingResult, Model model) {
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
    if (passwordEncryptor.checkPassword(loginForm.getPassword(), amministratore.getPassword())) {
      sessionAmministratore.setAmministratore(amministratore);
    } else {
      model.addAttribute("passwordErrata", true);
      return loginView;
    }

    return "redirect:" + homeAmministratoreController;
  }
}
