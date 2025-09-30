package it.unisa.justTraditions.applicationLogic.autenticazioneControl;

import it.unisa.justTraditions.applicationLogic.autenticazioneControl.util.SessionCliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Implementa il controller per il logout del Cliente.
 */
@Controller
@RequestMapping("/logout")
public class LogoutController {

  private static final String homeController = "/";

  @Autowired
  private SessionCliente sessionCliente;

  /**
   * Implementa la funzionalità di logout di un Cliente.
   *
   * @return redirect:/
   */
  @GetMapping
  public String get() {
    sessionCliente.setCliente(null);
    return "redirect:" + homeController;
  }
}
