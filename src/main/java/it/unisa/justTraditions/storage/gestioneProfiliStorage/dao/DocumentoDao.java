package it.unisa.justTraditions.storage.gestioneProfiliStorage.dao;

import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Documento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentoDao extends JpaRepository<Documento, Long> {}