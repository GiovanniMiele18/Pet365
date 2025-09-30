package it.unisa.justTraditions.applicationLogic.autenticazioneControl.util;

import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Amministratore;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Implementa i metodi per passare gli oggetti di sessione alle view.S
 */
@ControllerAdvice
public class SessionControllerAdvice {

  @Autowired
  private SessionCliente sessionCliente;

  @Autowired
  private SessionAmministratore sessionAmministratore;

  @ModelAttribute("cliente")
  public Cliente getCliente() {
    return sessionCliente.getCliente().orElse(null);
  }

  @ModelAttribute("amministratore")
  public Amministratore getAmministratore() {
    return sessionAmministratore.getAmministratore().orElse(null);
  }
}
