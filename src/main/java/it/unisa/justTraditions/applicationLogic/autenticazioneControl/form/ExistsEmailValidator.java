package it.unisa.justTraditions.applicationLogic.autenticazioneControl.form;

import it.unisa.justTraditions.applicationLogic.autenticazioneControl.util.SessionCliente;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.dao.ClienteDao;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Cliente;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementa la logica per convalidare l'email
 * se essa è presente all'interno del database.
 */
public class ExistsEmailValidator
    implements ConstraintValidator<ExistsEmailConstraint, String> {

  @Autowired
  private ClienteDao clienteDao;

  @Autowired
  private SessionCliente sessionCliente;

  /**
   * Inizializza il validatore in preparazione per le
   * isValid(String email, ConstraintValidatorContext).
   *
   * @param constraintAnnotation istanza di annotazione per una data dichiarazione di vincolo
   */
  @Override
  public void initialize(ExistsEmailConstraint constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  /**
   * Implementa la logica di convalida.
   *
   * @param email                      oggetto da convalidare.
   * @param constraintValidatorContext contesto in cui viene valutato il vincolo.
   * @return true se l'email esiste , false se non esiste nel database.
   */
  @Override
  public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
    Optional<Cliente> optionalCliente = sessionCliente.getCliente();
    if (optionalCliente.isEmpty()) {
      return !clienteDao.existsByEmail(email);
    } else {
      Cliente cliente = optionalCliente.get();
      if (cliente.getEmail().equals(email)) {
        return true;
      } else {
        return !clienteDao.existsByEmail(email);
      }
    }
  }
}
