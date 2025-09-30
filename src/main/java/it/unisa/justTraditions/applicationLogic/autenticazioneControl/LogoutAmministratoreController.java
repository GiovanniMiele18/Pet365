package it.unisa.justTraditions.applicationLogic.autenticazioneControl;

import it.unisa.justTraditions.applicationLogic.autenticazioneControl.util.SessionAmministratore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Implementa il controller per il logout dell amministratore.
 */
@Controller
@RequestMapping("/logoutAmministratore")
public class LogoutAmministratoreController {

  private static final String loginAmministratoreController = "/loginAmministratore";

  @Autowired
  private SessionAmministratore sessionAmministratore;

  /**
   * Implementa la funzionalità di logout di un amministratore.
   *
   * @return redirect: loginAmministratore
   */
  @GetMapping
  public String get() {
    sessionAmministratore.setAmministratore(null);
    return "redirect:" + loginAmministratoreController;
  }
}
