package it.unisa.justTraditions.applicationLogic.gestioneAnnunciControl.form;

import it.unisa.justTraditions.storage.gestioneAnnunciStorage.dao.AnnuncioDao;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Annuncio;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Implementa la logica per convalidare orarioInizio e orarioFine in ModificaStatoAnnuncioForm .
 */
public class NuovoStatoIsValidValidator
    implements ConstraintValidator<NuovoStatoIsValidConstraint, ModificaStatoAnnuncioForm> {

  @Autowired
  private AnnuncioDao annuncioDao;

  @Override
  public boolean isValid(ModificaStatoAnnuncioForm modificaStatoAnnuncioForm,
                         ConstraintValidatorContext constraintValidatorContext) {
    Annuncio.Stato stato =
        annuncioDao.findById(modificaStatoAnnuncioForm.getIdAnnuncio())
            .orElseThrow(IllegalArgumentException::new).getStato();
    Annuncio.Stato nuovoStato = modificaStatoAnnuncioForm.getNuovoStato();
    return !((stato == Annuncio.Stato.PROPOSTO
        && nuovoStato != Annuncio.Stato.IN_REVISIONE)
        ||
        (stato == Annuncio.Stato.IN_REVISIONE
            && nuovoStato != Annuncio.Stato.APPROVATO
            && nuovoStato != Annuncio.Stato.RIFIUTATO)
        ||
        (stato == Annuncio.Stato.APPROVATO
            && nuovoStato != Annuncio.Stato.IN_REVISIONE)
        ||
        (stato == Annuncio.Stato.RIFIUTATO));
  }

  @Override
  public void initialize(NuovoStatoIsValidConstraint constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }
}
