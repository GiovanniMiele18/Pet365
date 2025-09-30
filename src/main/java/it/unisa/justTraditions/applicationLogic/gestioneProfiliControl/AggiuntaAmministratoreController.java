package it.unisa.justTraditions.applicationLogic.gestioneProfiliControl;

import it.unisa.justTraditions.applicationLogic.autenticazioneControl.util.PasswordEncryptor;
import it.unisa.justTraditions.applicationLogic.gestioneProfiliControl.form.AggiuntaAmministratoreForm;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.dao.AmministratoreDao;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Amministratore;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Implementa il controller per l'aggiunta di un amministratore.
 */
@Controller
@RequestMapping("/aggiuntaAmministratore")
public class AggiuntaAmministratoreController {

  private static final String aggiuntaAmministratoreView =
      "gestioneProfiliView/aggiuntaAmministratore";
  private static final String visualizzazioneAmministratoriController =
      "/visualizzazioneAmministratori";

  @Autowired
  private PasswordEncryptor passwordEncryptor;

  @Autowired
  private AmministratoreDao amministratoreDao;


  /**
   * Implementa la funzionalità di smistare l'Amministratore
   * sulla view di gestioneAnnunciView/modificaAnnuncio.
   *
   * @param aggiuntaAmministratoreForm Utilizzato per mappare il Form della view.
   * @return Restituisce la view da reindirizzare.
   */
  @GetMapping
  public String get(@ModelAttribute AggiuntaAmministratoreForm aggiuntaAmministratoreForm) {
    return aggiuntaAmministratoreView;
  }

  /**
   * Implementa la funzionalità di aggiunta di un l'Amministratore.
   *
   * @param aggiuntaAmministratoreForm Utilizzato per mappare il Form della view.
   * @param bindingResult              Utilizzato per mappare gli errori dei dati di
   *                                   aggiuntaAmministratoreForm.
   * @return Restituisce la view da reindirizzare.
   */
  @PostMapping
  public String post(@ModelAttribute @Valid AggiuntaAmministratoreForm aggiuntaAmministratoreForm,
                     BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return aggiuntaAmministratoreView;
    }

    Amministratore amministratore = new Amministratore(
        aggiuntaAmministratoreForm.getEmail(),
        passwordEncryptor.encryptPassword(aggiuntaAmministratoreForm.getPassword()),
        aggiuntaAmministratoreForm.getNome(),
        aggiuntaAmministratoreForm.getCognome()
    );
    amministratoreDao.save(amministratore);

    return "redirect:" + visualizzazioneAmministratoriController;
  }
}