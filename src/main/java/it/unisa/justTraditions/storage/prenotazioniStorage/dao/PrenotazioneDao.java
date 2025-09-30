package it.unisa.justTraditions.storage.prenotazioniStorage.dao;

import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Annuncio;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Visita;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Cliente;
import it.unisa.justTraditions.storage.prenotazioniStorage.entity.Prenotazione;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Questa classe rappresenta il DAO di una Prenotazione.
 */
public interface PrenotazioneDao
    extends JpaRepository<Prenotazione, Long> {
  /**
   * Implementa la funzionalità di ricerca delle
   * prenotazioni di un annuncio in una determinata data.
   *
   * @param annuncio   Utilizzata per la ricerca delle prenotazioni.
   * @param dataVisita Utilizzata per la ricerca delle prenotazioni.
   * @param pageable   Utilizzata per configurare l oggetto Page.
   * @return Page di una lista di prenotazioni di un annuncio in una determinata data.
   */
  Page<Prenotazione> findByVisitaAnnuncioAndDataVisita(Annuncio annuncio, LocalDate dataVisita,
                                                       Pageable pageable);

  /**
   * Implementa la funzionalità di ricerca delle prenotazioni di una visita in una determinata data.
   *
   * @param visita     Utilizzata per la ricerca delle prenotazioni.
   * @param dataVisita Utilizzata per la ricerca delle prenotazioni.
   * @return Lista di prenotazioni di una visita in una determinata data.
   */
  List<Prenotazione> findByVisitaAndDataVisita(Visita visita, LocalDate dataVisita);

  /**
   * Implementa la funzionalità di ricerca delle prenotazioni di un cliente.
   *
   * @param cliente  Utilizzata per la ricerca delle prenotazioni.
   * @param pageable Utilizzata per configurare l oggetto Page.
   * @return Page di una lista di prenotazioni di un cliente.
   */
  Page<Prenotazione> findByCliente(Cliente cliente, Pageable pageable);
}
