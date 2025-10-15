package it.unisa.justTraditions.applicationLogic.shopControl;

import it.unisa.justTraditions.storage.shopStorage.dao.ProdottoDao;
import it.unisa.justTraditions.storage.shopStorage.entity.Prodotto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    private ProdottoDao prodottoDao;

    @GetMapping
    public String mostraShop(Model model) {
        List<Prodotto> prodotti = prodottoDao.findAll();
        prodotti.sort(Comparator.comparing(p -> p.getQuantitaDisponibile() == 0));
        model.addAttribute("prodotti", prodotti);
        return "shopView/shop";
    }
}
