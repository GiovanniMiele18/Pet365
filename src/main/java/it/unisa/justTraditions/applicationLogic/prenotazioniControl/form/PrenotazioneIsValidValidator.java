package it.unisa.justTraditions.applicationLogic.prenotazioniControl.form;

import it.unisa.justTraditions.storage.gestioneAnnunciStorage.dao.VisitaDao;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Visita;
import it.unisa.justTraditions.storage.prenotazioniStorage.dao.PrenotazioneDao;
import it.unisa.justTraditions.storage.prenotazioniStorage.entity.Prenotazione;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementa la logica per convalidare di un oggetto Prenotazioneform.
 * Controlla l'esistenza e la validità della visita.
 * Controlla se la data della prenotazione si trova dopo la data corrente e
 * corrisponda al giorno della settimana della visita.
 * Controlla che il numero di persone non sia maggiore dei posti disponibili.
 */
public class PrenotazioneIsValidValidator
    implements ConstraintValidator<PrenotazioneIsValidConstraint, PrenotazioneForm> {

  @Autowired
  private PrenotazioneDao prenotazioneDao;

  @Autowired
  private VisitaDao visitaDao;

  @Override
  public void initialize(PrenotazioneIsValidConstraint constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(PrenotazioneForm prenotazioneForm,
                         ConstraintValidatorContext constraintValidatorContext) {
    Optional<Visita> optionalVisita = visitaDao.findById(prenotazioneForm.getIdVisita());
    if (optionalVisita.isEmpty()) {
      return false;
    }
    Visita visita = optionalVisita.get();

    if (!visita.getValidita()) {
      return false;
    }

    if (prenotazioneForm.getDataVisita() == null
        || !prenotazioneForm.getDataVisita().isAfter(LocalDate.now())) {
      return false;
    }

    if (!prenotazioneForm.getDataVisita().getDayOfWeek().equals(visita.getGiorno())) {
      return false;
    }

    int numeroPersonePrenotate =
        prenotazioneDao.findByVisitaAndDataVisita(visita, prenotazioneForm.getDataVisita()).stream()
            .mapToInt(Prenotazione::getNumPersonePrenotate)
            .sum();

    return prenotazioneForm.getNumeroPersone()
        <= (visita.getAnnuncio().getNumMaxPersonePerVisita() - numeroPersonePrenotate);
  }
}
