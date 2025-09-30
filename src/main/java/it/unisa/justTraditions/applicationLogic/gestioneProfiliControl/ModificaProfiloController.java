package it.unisa.justTraditions.applicationLogic.gestioneProfiliControl;

import it.unisa.justTraditions.applicationLogic.autenticazioneControl.form.RegistrazioneForm;
import it.unisa.justTraditions.applicationLogic.autenticazioneControl.util.SessionCliente;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.dao.ClienteDao;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Artigiano;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Cliente;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Implementa il controller per la modifica di un profilo.
 */
@Controller
@RequestMapping("/modificaProfilo")
public class ModificaProfiloController {

  private static final String modificaProfiloView = "gestioneProfiliView/modificaProfilo";
  private static final String visualizzazioneProfiloPersonaleController =
      "/visualizzazioneProfiloPersonale";

  @Autowired
  private SessionCliente sessionCliente;

  @Autowired
  private ClienteDao clienteDao;

  /**
   * Implementa la funzionalità di smistare il Cliente
   * su la view di gestioneProfiliView/modificaProfilo.
   *
   * @param registrazioneForm Utilizzato per mappare il Form della view.
   * @return Restituisce la view da reindirizzare.
   */
  @GetMapping
  public String get(@ModelAttribute RegistrazioneForm registrazioneForm) {
    Cliente cliente = sessionCliente.getCliente().get();
    registrazioneForm.setNome(cliente.getNome());
    registrazioneForm.setCognome(cliente.getCognome());
    registrazioneForm.setCodiceFiscale(cliente.getCodiceFiscale());
    registrazioneForm.setEmail(cliente.getEmail());



    return modificaProfiloView;
  }

  /**
   * Implementa la funzionalità di Modifica profilo di un Cliente.
   *
   * @param registrazioneForm Utilizzato per mappare il Form della view.
   * @param bindingResult     Utilizzato per mappare gli errori dei dati di registrazioneForm.
   * @return Restituisce la view da reindirizzare.
   */
  @PostMapping
  public String post(@ModelAttribute @Valid RegistrazioneForm registrazioneForm,
                     BindingResult bindingResult) {
    if (bindingResult.hasFieldErrors("nome") || bindingResult.hasFieldErrors("cognome")
        || bindingResult.hasFieldErrors("email") || bindingResult.hasFieldErrors("codiceFiscale")
        || bindingResult.hasFieldErrors("artigiano") || bindingResult.hasFieldErrors("iban")
        || bindingResult.hasGlobalErrors()) {
      return modificaProfiloView;
    }

    Cliente cliente = sessionCliente.getCliente().get();
    cliente.setNome(registrazioneForm.getNome());
    cliente.setCognome(registrazioneForm.getCognome());
    cliente.setEmail(registrazioneForm.getEmail());
    cliente.setCodiceFiscale(registrazioneForm.getCodiceFiscale());

    clienteDao.save(cliente);

    return "redirect:" + visualizzazioneProfiloPersonaleController;
  }
}