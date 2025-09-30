package it.unisa.justTraditions.storage.gestioneAnnunciStorage.dao;

import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Annuncio;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Artigiano;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Questa classe rappresenta il DAO di un annuncio.
 */
public interface AnnuncioDao
    extends JpaRepository<Annuncio, Long> {
  /**
   * Implementa la funzionalità di prendere il numero di annunci con uno stato specifico.
   *
   * @param stato Utilizzato per filtrare gli annunci per stato.
   * @return long numero di annunci con uno stato specifico.
   */
  long countByStato(Annuncio.Stato stato);

  /**
   * Implementa la funzionalità di prendere una lista di annunci e li inserisce in un oggetto Page.
   *
   * @param artigiano Utilizzato ottenere tutti gli annunci di quel artigliano.
   * @param pageable  Utilizzato per configurare l oggetto Page.
   * @return Page Annuncio con una lista di annunci di un artigliano.
   */
  Page<Annuncio> findByArtigiano(Artigiano artigiano, Pageable pageable);
}
