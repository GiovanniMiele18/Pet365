package it.unisa.justTraditions.storage.shopStorage.dao;

import it.unisa.justTraditions.storage.shopStorage.entity.FotoProdotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FotoProdottoDao extends JpaRepository<FotoProdotto, Long> {
}
