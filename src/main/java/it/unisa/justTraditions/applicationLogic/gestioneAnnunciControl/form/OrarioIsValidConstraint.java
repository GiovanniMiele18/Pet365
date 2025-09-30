package it.unisa.justTraditions.applicationLogic.gestioneAnnunciControl.form;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Implementa l annotazione per convalidare OrarioInizio e orarioFine di VisitaForm.
 * OrarioInizio deve venire prima di orarioFine.
 */
@Documented
@Constraint(validatedBy = OrarioIsValidValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OrarioIsValidConstraint {
  /**
   * Permette di modificare il messaggio di errore.
   *
   * @return Orario di fine antecedente a quello di inizio.
   */
  String message() default "Formato orario errato o orario di fine antecedente a quello di inizio";

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
