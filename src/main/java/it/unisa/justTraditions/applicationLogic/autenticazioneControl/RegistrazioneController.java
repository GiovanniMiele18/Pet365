package it.unisa.justTraditions.applicationLogic.autenticazioneControl;

import it.unisa.justTraditions.applicationLogic.autenticazioneControl.form.RegistrazioneForm;
import it.unisa.justTraditions.applicationLogic.autenticazioneControl.util.PasswordEncryptor;
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

@Controller
@RequestMapping("/registrazione")
public class RegistrazioneController {

  private static final String registrazioneView = "autenticazioneView/registrazione";
  private static final String loginController = "/login";

  @Autowired
  private ClienteDao clienteDao;

  @Autowired
  private PasswordEncryptor passwordEncryptor;

  @GetMapping
  public String get(@ModelAttribute RegistrazioneForm registrazioneForm) {
    return registrazioneView;
  }

  @PostMapping
  public String post(@ModelAttribute @Valid RegistrazioneForm registrazioneForm,
                     BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return registrazioneView;
    }

    Cliente cliente;

    if (registrazioneForm.isLavoratore()) {
      // ✅ Se spunta "lavoratore" → creo un Artigiano
      cliente = new Artigiano(
              registrazioneForm.getEmail(),
              passwordEncryptor.encryptPassword(registrazioneForm.getPassword()),
              registrazioneForm.getNome(),
              registrazioneForm.getCognome(),
              registrazioneForm.getCodiceFiscale()
      );
    } else {
      // ✅ Altrimenti creo un Cliente normale
      cliente = new Cliente(
              registrazioneForm.getEmail(),
              passwordEncryptor.encryptPassword(registrazioneForm.getPassword()),
              registrazioneForm.getNome(),
              registrazioneForm.getCognome(),
              registrazioneForm.getCodiceFiscale()
      );
    }

    clienteDao.save(cliente);
    return "redirect:" + loginController;
  }
}
