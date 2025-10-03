package it.unisa.justTraditions.storage.prenotazioniStorage.entity;

import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Visita;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Animale;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Cliente;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Prenotazione {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private LocalDate dataVisita;

  // 🔗 Cliente che ha effettuato la prenotazione
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cliente_id", nullable = false)
  private Cliente cliente;

  // 🔗 Animale associato alla prenotazione
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "animale_id", nullable = false)
  private Animale animale;

  // 🔗 Visita collegata
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "visita_id", nullable = false)
  private Visita visita;

  public Prenotazione() {}

  /**
   * Costruttore per la prenotazione legata a un animale.
   */
  public Prenotazione(Visita visita, Animale animale, LocalDate dataVisita) {
    this.visita = visita;
    this.animale = animale;
    this.dataVisita = dataVisita;
  }

  // --- GETTER & SETTER ---
  public Long getId() { return id; }

  public LocalDate getDataVisita() { return dataVisita; }
  public void setDataVisita(LocalDate dataVisita) { this.dataVisita = dataVisita; }

  public Cliente getCliente() { return cliente; }
  public void setCliente(Cliente cliente) { this.cliente = cliente; }

  public Animale getAnimale() { return animale; }
  public void setAnimale(Animale animale) { this.animale = animale; }

  public Visita getVisita() { return visita; }
  public void setVisita(Visita visita) { this.visita = visita; }

  // --- equals, hashCode, toString ---
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Prenotazione that = (Prenotazione) o;
    return id != null && id.equals(that.id);
  }

  @Override
  public int hashCode() { return id != null ? id.hashCode() : 0; }

  @Override
  public String toString() {
    return "Prenotazione{" +
            "id=" + id +
            ", dataVisita=" + dataVisita +
            ", cliente=" + (cliente != null ? cliente.getId() : null) +
            ", animale=" + (animale != null ? animale.getId() : null) +
            ", visita=" + (visita != null ? visita.getId() : null) +
            '}';
  }
}
