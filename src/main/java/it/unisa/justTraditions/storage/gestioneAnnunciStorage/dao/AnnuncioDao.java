package it.unisa.justTraditions.storage.gestioneAnnunciStorage.dao;

import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Annuncio;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Artigiano;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnuncioDao extends JpaRepository<Annuncio, Long> {

  long countByStato(Annuncio.Stato stato);

  Page<Annuncio> findByArtigiano(Artigiano artigiano, Pageable pageable);
}
