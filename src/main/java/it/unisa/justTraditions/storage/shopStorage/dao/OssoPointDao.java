package it.unisa.justTraditions.storage.shopStorage.dao;

import it.unisa.justTraditions.storage.shopStorage.entity.OssoPoint;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * DAO per la gestione dei punti fedeltà (OssoPoint) dei clienti.
 */
public interface OssoPointDao extends JpaRepository<OssoPoint, Long> {

    // Recupera i punti tramite l'entità Cliente
    Optional<OssoPoint> findByCliente(Cliente cliente);

    // Recupera i punti tramite l'id del Cliente
    Optional<OssoPoint> findByClienteId(Long clienteId);
}
