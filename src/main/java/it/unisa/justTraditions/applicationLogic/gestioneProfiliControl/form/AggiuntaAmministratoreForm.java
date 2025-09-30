package it.unisa.justTraditions.applicationLogic.gestioneProfiliControl.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Classe che rappresenta il form l'aggiunta di un amministratore.
 */
public class AggiuntaAmministratoreForm {

  @NotBlank(message = "Nome vuoto")
  @Size(max = 30, message = "Nome troppo lungo")
  private String nome;

  @NotBlank(message = "Cognome vuoto")
  @Size(max = 30, message = "Cognome troppo lungo")
  private String cognome;

  @NotBlank(message = "Email vuota")
  @Email(message = "Formato email errato")
  @Size(max = 319, message = "Email troppo lunga")
  @ExistsEmailAmministratoreConstraint
  private String email;

  @NotBlank(message = "Password vuota")
  @Pattern(
      regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*\\p{Punct})[A-Za-z\\d\\p{Punct}]{8,}$",
      message =
          "La password deve contenere almeno 8 caratteri, di cui almeno una lettera maiuscola, "
              + "almeno una minuscola, almeno un numero e almeno un carattere speciale"
  )
  private String password;


  public AggiuntaAmministratoreForm() {
  }

  /**
   * Costruttore di un amministratore.
   *
   * @param nome     Nome dell amministratore.
   * @param cognome  cognome dell amministratore.
   * @param email    email dell amministratore.
   * @param password password dell amministratore.
   */
  public AggiuntaAmministratoreForm(String nome, String cognome, String email, String password) {
    this.nome = nome;
    this.cognome = cognome;
    this.email = email;
    this.password = password;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getCognome() {
    return cognome;
  }

  public void setCognome(String cognome) {
    this.cognome = cognome;
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

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("AggiuntaAmministratoreForm{");
    sb.append("nome='").append(nome).append('\'');
    sb.append(", cognome='").append(cognome).append('\'');
    sb.append(", email='").append(email).append('\'');
    sb.append(", password='").append(password).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
