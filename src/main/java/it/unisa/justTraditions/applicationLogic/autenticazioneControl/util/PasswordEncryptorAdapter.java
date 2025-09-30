package it.unisa.justTraditions.applicationLogic.autenticazioneControl.util;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.stereotype.Component;

/**
 * Adapter per la classe PasswordEncryptor.
 */
@Component
public class PasswordEncryptorAdapter
    implements PasswordEncryptor {

  private final org.jasypt.util.password.PasswordEncryptor passwordEncryptor =
      new BasicPasswordEncryptor();

  @Override
  public String encryptPassword(String password) {
    return passwordEncryptor.encryptPassword(password);
  }

  @Override
  public boolean checkPassword(String password, String encryptedPassword) {
    return passwordEncryptor.checkPassword(password, encryptedPassword);
  }
}
