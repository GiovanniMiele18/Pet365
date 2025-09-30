package it.unisa.justTraditions.storage.gestioneProfiliStorage;

import it.unisa.justTraditions.storage.gestioneProfiliStorage.dao.ClienteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.dao.AnimaleDao;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Animale;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Cliente;
import java.util.List;

@Service
public class AnimaleService {

    @Autowired
    private AnimaleDao animaleDAO;

    @Autowired
    private ClienteDao clienteDAO;

    public Animale aggiungiAnimale(Long clienteId, Animale animale) {
        Cliente cliente = clienteDAO.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente non trovato"));

        animale.setCliente(cliente);
        animale = animaleDAO.save(animale);

        cliente.getAnimali().add(animale);
        clienteDAO.save(cliente);

        return animale;
    }

    public List<Animale> getAnimaliByCliente(Long clienteId) {
        return animaleDAO.findByClienteId(clienteId);
    }
}
