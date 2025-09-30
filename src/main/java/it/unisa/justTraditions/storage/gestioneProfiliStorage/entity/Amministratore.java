package it.unisa.justTraditions.storage.gestioneProfiliStorage.entity;

import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REFRESH;

import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Annuncio;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Questa classe rappresenta un Amministratore.
 * Un amministratore puo approvare o rifiutare uno o piu annunci.
 */
@Entity
public class Amministratore
    extends Utente {
  @OneToMany(mappedBy = "amministratore", cascade = {MERGE, PERSIST, REFRESH})
  private List<Annuncio> annunciApprovati = new ArrayList<>();

  public Amministratore() {
  }

  public Amministratore(String email, String password, String nome, String cognome) {
    super(email, password, nome, cognome);
  }

  public List<Annuncio> getAnnunciApprovati() {
    return Collections.unmodifiableList(annunciApprovati);
  }

  public void addAnnuncioApprovato(Annuncio annuncio) {
    annunciApprovati.add(annuncio);
    annuncio.setAmministratore(this);
  }

  public void removeAnnuncioApprovato(Annuncio annuncio) {
    annunciApprovati.remove(annuncio);
    annuncio.setAmministratore(null);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Amministratore{");
    sb.append('}').append(" is a ").append(super.toString());
    return sb.toString();
  }
}
