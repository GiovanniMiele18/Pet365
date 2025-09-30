package it.unisa.justTraditions.applicationLogic.autenticazioneControl.util;

import it.unisa.justTraditions.storage.gestioneProfiliStorage.dao.AmministratoreDao;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Amministratore;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

/**
 * Implementa la funzionalità che permette
 * di aggiungere un Amministratore alla sessione.
 */
@Component
@SessionScope
public class SessionAmministratore {

  private Long idAmministratore;

  @Autowired
  private AmministratoreDao amministratoreDao;


  /**
   * Implementa la funzionalità di restituire l amministratore loggato nella sessione.
   *
   * @return Amministratore.
   */
  public Optional<Amministratore> getAmministratore() {
    if (idAmministratore != null) {
      return amministratoreDao.findById(idAmministratore);
    }
    return Optional.empty();
  }


  /**
   * Implemmenta la funzionalita di settate l amministratore loggato nella sessione.
   *
   * @param amministratore Oggetto amministratore identifica l amministratore loggato.
   */
  public void setAmministratore(Amministratore amministratore) {
    if (amministratore == null) {
      this.idAmministratore = null;
    } else {
      this.idAmministratore = amministratore.getId();
    }
  }
}