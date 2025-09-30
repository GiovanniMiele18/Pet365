package it.unisa.justTraditions.storage.gestioneProfiliStorage.dao;

import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Utente;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Questa classe rappresenta il DAO di un Utente.
 */
@NoRepositoryBean
public interface UtenteDao<T extends Utente>
    extends JpaRepository<T, Long> {


  /**
   * Implementa la funzionalità di ricerca di un utente tramite l email.
   *
   * @param email Utilizzata per la ricerca.
   * @return Optional T  Utente o una sua specializzazione.
   */
  Optional<T> findByEmail(String email);

  /**
   * Implementa la funzione di ricerca di un email all'interno del database.
   *
   * @param email Utilizzase per la ricerca.
   * @return vero se esiste falso se non esiste nell database.
   */
  boolean existsByEmail(String email);
}
