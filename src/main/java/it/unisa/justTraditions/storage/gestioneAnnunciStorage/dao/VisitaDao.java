package it.unisa.justTraditions.storage.gestioneAnnunciStorage.dao;

import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Annuncio;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Visita;
import java.time.DayOfWeek;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Questa classe rappresenta il DAO di una visita.
 */
public interface VisitaDao
    extends JpaRepository<Visita, Long> {
  /**
   * Implementa la funzionalità di prendere una visita
   * di un determinato giorno della settimana di un determinato annuncio.
   *
   * @param annuncio Utilizzato per specificare le visita di che annuncio sono.
   * @param giorno   Utilizzato per filtrare le visite per giorni della settimana.
   * @return Lista di visite di un annuncio di uno specifico giorno della settimana.
   */
  List<Visita> findByAnnuncioAndGiornoAndValiditaTrue(Annuncio annuncio, DayOfWeek giorno);
}
