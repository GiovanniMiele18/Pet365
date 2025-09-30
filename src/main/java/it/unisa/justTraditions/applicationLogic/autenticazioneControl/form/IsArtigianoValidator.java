package it.unisa.justTraditions.applicationLogic.autenticazioneControl.form;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validatore personalizzato per controllare l'IBAN
 * solo se l'utente si dichiara Artigiano.
 */
public class IsArtigianoValidator
        implements ConstraintValidator<IsArtigianoConstraint, RegistrazioneForm> {

  @Override
  public void initialize(IsArtigianoConstraint constraintAnnotation) {
    // Nessuna inizializzazione necessaria
  }

  @Override
  public boolean isValid(RegistrazioneForm form, ConstraintValidatorContext context) {
    if (form == null) {
      return true; // lascia che altri validatori gestiscano null
    }


    // Se non è artigiano, accettiamo anche iban mancante
    return true;
  }
}
