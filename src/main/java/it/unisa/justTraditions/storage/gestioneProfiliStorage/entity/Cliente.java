package it.unisa.justTraditions.storage.gestioneProfiliStorage.entity;

import it.unisa.justTraditions.storage.prenotazioniStorage.entity.Prenotazione;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Questa classe rappresenta un Cliente.
 * Un puo prenotarsi a uno o piu visite.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", length = 9)
public class Cliente
    extends Utente {
  @Column(nullable = false, unique = true, columnDefinition = "CHAR(16)")
  private String codiceFiscale;
  @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
  private List<Prenotazione> prenotazioni = new ArrayList<>();
  @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Animale> animali = new ArrayList<>();

  // getter e setter
  public List<Animale> getAnimali() {
    return animali;
  }

  public void setAnimali(List<Animale> animali) {
    this.animali = animali;
  }

  public void addAnimale(Animale animale) {
    animali.add(animale);
    animale.setCliente(this);
  }


  public Cliente() {
  }

  public Cliente(String email, String password, String nome, String cognome, String codiceFiscale) {
    super(email, password, nome, cognome);
    this.codiceFiscale = codiceFiscale;
  }

  public String getCodiceFiscale() {
    return codiceFiscale;
  }

  public void setCodiceFiscale(String codiceFiscale) {
    this.codiceFiscale = codiceFiscale;
  }

  public List<Prenotazione> getPrenotazioni() {
    return Collections.unmodifiableList(prenotazioni);
  }

  public void addPrenotazione(Prenotazione prenotazione) {
    prenotazioni.add(prenotazione);
    prenotazione.setCliente(this);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Cliente{");
    sb.append("codiceFiscale='").append(codiceFiscale).append('\'');
    sb.append('}').append(" is a ").append(super.toString());
    return sb.toString();
  }
}
