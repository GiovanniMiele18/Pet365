package it.unisa.justTraditions.storage.gestioneAnnunciStorage.dao;

import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Foto;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Questa classe rappresenta il DAO di una foto.
 */
public interface FotoDao
    extends JpaRepository<Foto, Long> {
}
