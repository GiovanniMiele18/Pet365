package it.unisa.justTraditions.storage.gestioneProfiliStorage.entity;

import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Annuncio;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Questa classe rappresenta un Artigiano.
 * Un artigiano puo pubblicare uno o piu annunci.
 */
@Entity
@DiscriminatorValue("Artigiano")
public class Artigiano
    extends Cliente {

  @OneToMany(mappedBy = "artigiano", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Annuncio> annunci = new ArrayList<>();

  public Artigiano() {
  }

  public Artigiano(String email, String password, String nome, String cognome, String codiceFiscale) {
    super(email, password, nome, cognome, codiceFiscale);
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
    return "Artigiano{" +
            "annunci=" + annunci +
            '}';
  }
}
