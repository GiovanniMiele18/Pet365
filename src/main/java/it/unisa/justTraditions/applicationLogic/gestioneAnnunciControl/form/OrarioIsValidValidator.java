package it.unisa.justTraditions.applicationLogic.gestioneAnnunciControl.form;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Implementa la logica per convalidare OrarioInizio e orarioFine di VisitaForm.
 * OrarioInizio deve venire prima di orarioFine.
 */
public class OrarioIsValidValidator
    implements ConstraintValidator<OrarioIsValidConstraint, VisitaForm> {

  @Override
  public boolean isValid(VisitaForm visitaForm,
                         ConstraintValidatorContext constraintValidatorContext) {
    if (visitaForm.getOrarioFine() == null || visitaForm.getOrarioInizio() == null) {
      return false;
    }
    return !visitaForm.getOrarioFine().isBefore(visitaForm.getOrarioInizio());
  }

  @Override
  public void initialize(OrarioIsValidConstraint constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }
}
