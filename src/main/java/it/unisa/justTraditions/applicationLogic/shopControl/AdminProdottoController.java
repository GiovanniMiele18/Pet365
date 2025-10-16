package it.unisa.justTraditions.applicationLogic.shopControl;

import it.unisa.justTraditions.applicationLogic.shopControl.form.AggiuntaProdottoForm;
import it.unisa.justTraditions.storage.shopStorage.dao.ProdottoDao;
import it.unisa.justTraditions.storage.shopStorage.entity.FotoProdotto;
import it.unisa.justTraditions.storage.shopStorage.entity.Prodotto;
import jakarta.validation.Valid;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin/prodotti")
public class AdminProdottoController {

    @Autowired
    private ProdottoDao prodottoDao;

    // 📄 LISTA PRODOTTI
    @GetMapping
    public String visualizzaProdotti(Model model) {
        model.addAttribute("prodotti", prodottoDao.findAll());
        return "shopView/visualizzazioneProdotti";
    }

    // ➕ FORM AGGIUNTA
    @GetMapping("/nuovo")
    public String mostraFormAggiunta(Model model) {
        model.addAttribute("form", new AggiuntaProdottoForm());
        return "shopView/aggiuntaProdotto";
    }

    // 💾 SALVA NUOVO PRODOTTO
    @PostMapping("/salva")
    public String salvaProdotto(@Valid @ModelAttribute("form") AggiuntaProdottoForm form,
                                BindingResult result,
                                Model model) {

        if (result.hasErrors()) {
            return "shopView/aggiuntaProdotto";
        }

        Prodotto prodotto = new Prodotto(
                form.getNome(),
                form.getDescrizione(),
                form.getPrezzo(),
                form.getQuantitaDisponibile(),
                form.getCategoria()
        );

        // 🖼️ Gestione foto salvate come byte[]
        if (form.getFoto() != null) {
            for (MultipartFile file : form.getFoto()) {
                try {
                    if (!file.isEmpty()) {
                        prodotto.addFoto(new FotoProdotto(file.getBytes()));
                    }
                } catch (IOException e) {
                    model.addAttribute("erroreFile", true);
                    return "shopView/aggiuntaProdotto";
                }
            }
        }

        prodottoDao.save(prodotto);
        return "redirect:/admin/prodotti";
    }

    // ✏️ FORM MODIFICA
    @GetMapping("/modifica/{id}")
    public String mostraFormModifica(@PathVariable Long id, Model model) {
        Prodotto prodotto = prodottoDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato"));

        AggiuntaProdottoForm form = new AggiuntaProdottoForm(
                prodotto.getId(),
                prodotto.getNome(),
                prodotto.getDescrizione(),
                prodotto.getPrezzo(),
                prodotto.getQuantitaDisponibile(),
                prodotto.getCategoria()
        );

        model.addAttribute("form", form);
        model.addAttribute("prodotto", prodotto);
        return "shopView/modificaProdotto";
    }

    // 💾 AGGIORNA PRODOTTO
    @PostMapping("/aggiorna")
    public String aggiornaProdotto(@Valid @ModelAttribute("form") AggiuntaProdottoForm form,
                                   BindingResult result,
                                   Model model) {

        if (result.hasErrors()) {
            return "shopView/modificaProdotto";
        }

        Prodotto prodotto = prodottoDao.findById(form.getId())
                .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato"));

        prodotto.setNome(form.getNome());
        prodotto.setDescrizione(form.getDescrizione());
        prodotto.setPrezzo(form.getPrezzo());
        prodotto.setQuantitaDisponibile(form.getQuantitaDisponibile());
        prodotto.setCategoria(form.getCategoria());

        // Aggiunta nuove foto
        if (form.getFoto() != null) {
            for (MultipartFile file : form.getFoto()) {
                try {
                    if (!file.isEmpty()) {
                        prodotto.addFoto(new FotoProdotto(file.getBytes()));
                    }
                } catch (IOException e) {
                    model.addAttribute("erroreFile", true);
                    return "shopView/modificaProdotto";
                }
            }
        }

        prodottoDao.save(prodotto);
        return "redirect:/admin/prodotti";
    }

    // ❌ RIMUOVI PRODOTTO
    @GetMapping("/rimuovi/{id}")
    public String rimuoviProdotto(@PathVariable Long id) {
        prodottoDao.deleteById(id);
        return "redirect:/admin/prodotti";
    }
}
