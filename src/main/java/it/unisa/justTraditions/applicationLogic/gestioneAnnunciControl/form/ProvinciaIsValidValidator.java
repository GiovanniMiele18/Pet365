package it.unisa.justTraditions.applicationLogic.gestioneAnnunciControl.form;

import it.unisa.justTraditions.applicationLogic.util.Province;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementa la logica per convalidare la provincia.
 * Controlla che la provincia esista.
 */
public class ProvinciaIsValidValidator
    implements ConstraintValidator<ProvinciaIsValidConstraint, String> {

  @Autowired
  private Province province;

  @Override
  public boolean isValid(String provincia,
                         ConstraintValidatorContext constraintValidatorContext) {
    return province.getProvince().contains(provincia);
  }

  @Override
  public void initialize(ProvinciaIsValidConstraint constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }
}
