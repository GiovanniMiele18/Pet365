package it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity;

import it.unisa.justTraditions.storage.util.OnlyStorageCall;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

/**
 * Questa classe rappresenta una foto di un annuncio.
 */
@Entity
public class Foto {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Lob
  @Basic(fetch = FetchType.LAZY)
  @Column(nullable = false, length = 16777215)
  private byte[] dati;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "annuncio", nullable = false)
  private Annuncio annuncio;

  public Foto() {
  }

  public Foto(byte[] dati) {
    this.dati = dati;
  }

  public Long getId() {
    return id;
  }

  public byte[] getDati() {
    return dati;
  }

  public void setDati(byte[] dati) {
    this.dati = dati;
  }

  public Annuncio getAnnuncio() {
    return annuncio;
  }

  public void setAnnuncio(Annuncio annuncio) {
    OnlyStorageCall.validateCall();
    this.annuncio = annuncio;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Foto foto = (Foto) o;

    return id.equals(foto.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Foto{");
    sb.append("id=").append(id);
    sb.append(", dati=").append(dati == null ? "null" : dati.length + "bytes");
    sb.append(", annuncio=").append(annuncio);
    sb.append('}');
    return sb.toString();
  }
}
