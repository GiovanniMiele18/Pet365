package it.unisa.justTraditions.storage.gestioneProfiliStorage.entity;

import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Annuncio;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Questa classe rappresenta un Artigiano.
 * Un artigiano puo pubblicare uno o piu annunci.
 */
@Entity
public class Artigiano
    extends Cliente {
  @Column(columnDefinition = "CHAR(27)")
  private String iban;
  @OneToMany(mappedBy = "artigiano", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Annuncio> annunci = new ArrayList<>();

  public Artigiano() {
  }

  public Artigiano(String email, String password, String nome, String cognome, String codiceFiscale,
                   String iban) {
    super(email, password, nome, cognome, codiceFiscale);
    this.iban = iban;
  }

  public String getIban() {
    return iban;
  }

  public void setIban(String iban) {
    this.iban = iban;
  }

  public List<Annuncio> getAnnunci() {
    return Collections.unmodifiableList(annunci);
  }

  public void addAnnuncio(Annuncio annuncio) {
    annunci.add(annuncio);
    annuncio.setArtigiano(this);
  }

  public void removeAnnuncio(Annuncio annuncio) {
    annunci.remove(annuncio);
    annuncio.setArtigiano(null);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Artigiano{");
    sb.append("iban='").append(iban).append('\'');
    sb.append('}').append(" is a ").append(super.toString());
    return sb.toString();
  }
}
