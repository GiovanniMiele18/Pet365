package it.unisa.justTraditions.applicationLogic.autenticazioneControl.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Classe che rappresenta il form per il login di un Cliente.
 */
public class LoginForm {

  @NotBlank(message = "email vuota")
  @Email(message = "formato email errato")
  @Size(max = 319, message = "email troppo lunga")
  private String email;

  @NotBlank(message = "password vuota")
  @Pattern(
      regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*\\p{Punct})[A-Za-z\\d\\p{Punct}]{8,}$",
      message =
          "La password deve contenere almeno 8 caratteri, di cui almeno una lettera maiuscola, "
              + "almeno una minuscola, almeno un numero e almeno un carattere speciale"
  )
  private String password;

  public LoginForm() {
  }

  public LoginForm(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
