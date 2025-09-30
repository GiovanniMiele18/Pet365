package it.unisa.justTraditions.applicationLogic.autenticazioneControl.form;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Implementa l annotazione per convalidare l'email
 * se essa è presente all'interno del database.
 */
@Documented
@Constraint(validatedBy = ExistsEmailValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistsEmailConstraint {

  /**
   * Permette di definire il messaggio di errore.
   *
   * @return Email già esistente
   */
  String message() default "Email già esistente";

  /**
   * Permetti di specificare il gruppo di validazione.
   *
   * @return restituisce le classi del gruppo di validazione.
   */
  Class<?>[] groups() default {};

  /**
   * ermette di specificare un payload per trasportare le informazioni sui metadati
   * utilizzate da un client di convalida.
   *
   * @return restituisce le classi che rappresentano il payload.
   */
  Class<? extends Payload>[] payload() default {};

}
