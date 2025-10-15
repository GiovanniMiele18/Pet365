package it.unisa.justTraditions.storage.shopStorage.dao;

import it.unisa.justTraditions.storage.shopStorage.entity.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdottoDao extends JpaRepository<Prodotto, Long> {
}
