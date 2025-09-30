package it.unisa.justTraditions.applicationLogic.autenticazioneControl.form;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotazione per la convalida di un oggetto RegistrazioneForm se l'utente
 * registrato si e dichiarato Artigiano controllare.
 * * l'aggiunta dell attributo RegistrazioneForm.iban è la sua correttezza.
 */
@Documented
@Constraint(validatedBy = IsArtigianoValidator.class)
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsArtigianoConstraint {
  /**
   * Permette di specificare il messaggio di errore.
   *
   * @return Aggiungi un iban valido se vuoi essere un Artigiano.
   */
  String message() default "Aggiungi un iban valido se vuoi essere un Artigiano";

  /**
   * Permetti di specificare la gruppo di validazione.
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
