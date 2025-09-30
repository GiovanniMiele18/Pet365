package it.unisa.justTraditions.applicationLogic.gestioneAnnunciControl.form;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Implementa l annotazione per convalidare
 * la giusta transazione di stato per ModificaStatoAnnuncioForm.
 */
@Documented
@Constraint(validatedBy = NuovoStatoIsValidValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NuovoStatoIsValidConstraint {
  /**
   * Permette di specificare il messaggio di errore.
   *
   * @return Transizione di stato non valida.
   */
  String message() default "Transizione di stato non valida";

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
