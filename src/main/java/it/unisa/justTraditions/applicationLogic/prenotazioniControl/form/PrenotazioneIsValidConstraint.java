package it.unisa.justTraditions.applicationLogic.prenotazioniControl.form;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Implementa l annotazione per convalidare di un oggetto Prenotazioneform.
 * Controlla l'esistenza e la validità della visita.
 * Controlla se la data della prenotazione si trova dopo la data corrente e
 * corrisponda al giorno della settimana della visita.
 * Controlla che il numero di persone non sia maggiore dei posti disponibili.
 */

@Documented
@Constraint(validatedBy = PrenotazioneIsValidValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PrenotazioneIsValidConstraint {
  /**
   * Permette di specificare il messaggio di errore.
   *
   * @return Prenotazione non valida.
   */
  String message() default "Prenotazione non valida";

  /**
   * Permetti di specificare la gruppo di validazione.
   *
   * @return restituisce le classi del gruppo di validazione.
   */
  Class<?>[] groups() default {};

  /**
   * Permette di specificare un payload per trasportare le informazioni sui metadati
   * utilizzate da un client di convalida.
   *
   * @return restituisce le classi che rappresentano il payload.
   */
  Class<? extends Payload>[] payload() default {};
}
