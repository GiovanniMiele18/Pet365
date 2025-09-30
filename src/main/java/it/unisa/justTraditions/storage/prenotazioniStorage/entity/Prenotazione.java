package it.unisa.justTraditions.storage.prenotazioniStorage.entity;

import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Visita;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Cliente;
import it.unisa.justTraditions.storage.util.OnlyStorageCall;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Questa classe rappresenta una Prenotazione.
 * Una prenotazione è effettuata da un cliente.
 * Una prenotazione è associata a una visita.
 */
@Entity
public class Prenotazione {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(nullable = false, precision = 5, scale = 2)
  private BigDecimal prezzoVisita;
  @Column(nullable = false)
  private LocalDate dataVisita;
  @Column(nullable = false)
  private Integer numPersonePrenotate;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cliente", nullable = false)
  private Cliente cliente;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "visita")
  private Visita visita;

  public Prenotazione() {
  }

  /**
   * Costruttore per una Prenotazione.
   *
   * @param prezzoVisita        Prezzo della visita al momento della prenotazione.
   * @param dataVisita          Data della prenotazione.
   * @param numPersonePrenotate Numero di persone prenotate.
   */
  public Prenotazione(BigDecimal prezzoVisita, LocalDate dataVisita, Integer numPersonePrenotate) {
    this.prezzoVisita = prezzoVisita;
    this.dataVisita = dataVisita;
    this.numPersonePrenotate = numPersonePrenotate;
  }

  public Long getId() {
    return id;
  }

  public BigDecimal getPrezzoVisita() {
    return prezzoVisita;
  }

  public void setPrezzoVisita(BigDecimal prezzoVisita) {
    this.prezzoVisita = prezzoVisita;
  }

  public LocalDate getDataVisita() {
    return dataVisita;
  }

  public void setDataVisita(LocalDate dataVisita) {
    this.dataVisita = dataVisita;
  }

  public Integer getNumPersonePrenotate() {
    return numPersonePrenotate;
  }

  public void setNumPersonePrenotate(Integer numPersonePrenotate) {
    this.numPersonePrenotate = numPersonePrenotate;
  }

  public Cliente getCliente() {
    return cliente;
  }

  public void setCliente(Cliente cliente) {
    OnlyStorageCall.validateCall();
    this.cliente = cliente;
  }

  public Visita getVisita() {
    return visita;
  }

  public void setVisita(Visita visita) {
    OnlyStorageCall.validateCall();
    this.visita = visita;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Prenotazione that = (Prenotazione) o;

    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("Prenotazione{");
    sb.append("id=").append(id);
    sb.append(", prezzoVisita=").append(prezzoVisita);
    sb.append(", dataVisita=").append(dataVisita);
    sb.append(", numPersonePrenotate=").append(numPersonePrenotate);
    sb.append(", cliente=").append(cliente);
    sb.append(", visita=").append(visita);
    sb.append('}');
    return sb.toString();
  }
}
