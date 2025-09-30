package it.unisa.justTraditions.applicationLogic.gestioneAnnunciControl.form;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 * Implementa la logica per convalidare la lista di foto.
 * Controlla che le foto non siano vuote o superino i 15 MB.
 */
public class FotoListIsValidValidator
    implements ConstraintValidator<FotoListIsValidConstraint, List<MultipartFile>> {

  @Override
  public boolean isValid(List<MultipartFile> foto,
                         ConstraintValidatorContext constraintValidatorContext) {
    for (MultipartFile file : foto) {
      if (file.isEmpty() || file.getSize() > 1024 * 1024 * 15) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void initialize(FotoListIsValidConstraint constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }
}
