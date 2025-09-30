package it.unisa.justTraditions.storage.gestioneProfiliStorage.dao;

import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Cliente;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Questa classe rappresenta il DAO di un ClienteGenerico.
 */
@NoRepositoryBean
public interface ClienteGenericDao<T extends Cliente>
    extends UtenteDao<T> {
}
