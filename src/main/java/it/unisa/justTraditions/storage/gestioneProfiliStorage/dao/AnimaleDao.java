package it.unisa.justTraditions.storage.gestioneProfiliStorage.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Animale;
import java.util.List;

public interface AnimaleDao extends JpaRepository<Animale, Long> {
    List<Animale> findByClienteId(Long clienteId);
}
