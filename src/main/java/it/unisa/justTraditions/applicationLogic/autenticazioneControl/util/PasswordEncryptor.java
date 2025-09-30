package it.unisa.justTraditions.applicationLogic.autenticazioneControl.util;

/**
 * Implementa l interfaccia per le funzioni
 * di criptaggio o decriptaggio della password.
 */
public interface PasswordEncryptor {
  String encryptPassword(String password);

  boolean checkPassword(String password, String encryptedPassword);
}
