package it.unisa.justTraditions.applicationLogic.gestioneProfiliControl;

import it.unisa.justTraditions.applicationLogic.autenticazioneControl.util.SessionCliente;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.dao.ClienteDao;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Implementa il controller per la concellazione di un account.
 */
@Controller
@RequestMapping("/cancellazioneAccount")
public class CancellazioneAccountController {

  private static final String homeController =
      "/";

  @Autowired
  private ClienteDao clienteDao;

  @Autowired
  private SessionCliente sessionCliente;

  /**
   * Implementa la funzionalità di eliminare l'account di un Cliente.
   *
   * @return redirect:/.
   */
  @GetMapping
  public String get() {
    Cliente cliente = sessionCliente.getCliente().get();
    sessionCliente.setCliente(null);
    clienteDao.delete(cliente);

    return "redirect:" + homeController;
  }
}