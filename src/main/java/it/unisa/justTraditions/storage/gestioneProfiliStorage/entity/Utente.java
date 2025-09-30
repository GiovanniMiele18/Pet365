package it.unisa.justTraditions.storage.gestioneProfiliStorage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

/**
 * Questa classe rappresenta un Utente.
 */
@MappedSuperclass
public abstract class Utente {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false, unique = true, length = 319)
  private String email;
  @Column(nullable = false, columnDefinition = "CHAR(32)")
  private String password;
  @Column(nullable = false, length = 30)
  private String nome;
  @Column(nullable = false, length = 30)
  private String cognome;

  protected Utente() {
  }

  protected Utente(String email, String password, String nome, String cognome) {
    this.email = email;
    this.password = password;
    this.nome = nome;
    this.cognome = cognome;
  }

  public Long getId() {
    return id;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Utente utente = (Utente) o;

    return id.equals(utente.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Utente{");
    sb.append("id=").append(id);
    sb.append(", email='").append(email).append('\'');
    sb.append(", password='").append(password).append('\'');
    sb.append(", nome='").append(nome).append('\'');
    sb.append(", cognome='").append(cognome).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
