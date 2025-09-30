package it.unisa.justTraditions.applicationLogic.gestioneProfiliControl.form;

import it.unisa.justTraditions.storage.gestioneProfiliStorage.dao.AmministratoreDao;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementa la logica per convalidare l email dell amministratore.
 */
public class ExistsEmailAmministratoreValidator
    implements ConstraintValidator<ExistsEmailAmministratoreConstraint, String> {

  @Autowired
  private AmministratoreDao amministratoreDao;

  @Override
  public void initialize(ExistsEmailAmministratoreConstraint constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
    return !amministratoreDao.existsByEmail(email);
  }
}
