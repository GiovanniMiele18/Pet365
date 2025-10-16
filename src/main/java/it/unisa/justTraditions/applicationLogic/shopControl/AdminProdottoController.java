package it.unisa.justTraditions.applicationLogic.shopControl;

import it.unisa.justTraditions.applicationLogic.shopControl.form.AggiuntaProdottoForm;
import it.unisa.justTraditions.storage.shopStorage.dao.FotoProdottoDao;
import it.unisa.justTraditions.storage.shopStorage.dao.ProdottoDao;
import it.unisa.justTraditions.storage.shopStorage.entity.FotoProdotto;
import it.unisa.justTraditions.storage.shopStorage.entity.Prodotto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/admin/prodotti")
public class AdminProdottoController {

    @Autowired
    private ProdottoDao prodottoDao;

    @Autowired
    private FotoProdottoDao fotoProdottoDao;

    // 📋 Lista prodotti
    @GetMapping
    public String visualizzaProdotti(Model model) {
        model.addAttribute("prodotti", prodottoDao.findAll());
        return "shopView/visualizzazioneProdotti";
    }

    // ➕ Nuovo prodotto
    @GetMapping("/nuovo")
    public String mostraFormAggiunta(Model model) {
        model.addAttribute("form", new AggiuntaProdottoForm());
        return "shopView/aggiuntaProdotto";
    }

    @PostMapping("/salva")
    public String salvaProdotto(@Valid @ModelAttribute("form") AggiuntaProdottoForm form,
                                BindingResult result, Model model) {

        if (result.hasErrors()) return "shopView/aggiuntaProdotto";

        Prodotto prodotto = new Prodotto(
                form.getNome(),
                form.getDescrizione(),
                form.getPrezzo(),
                form.getQuantitaDisponibile(),
                form.getCategoria()
        );

        // 📸 Salvataggio foto iniziali
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

    // ✏️ Form modifica
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

    // 💾 Aggiorna prodotto (AGGIUNGE le nuove foto)
    @PostMapping("/aggiorna")
    public String aggiornaProdotto(@Valid @ModelAttribute("form") AggiuntaProdottoForm form,
                                   BindingResult result, Model model) {

        if (result.hasErrors()) return "shopView/modificaProdotto";

        Prodotto prodotto = prodottoDao.findById(form.getId())
                .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato"));

        prodotto.setNome(form.getNome());
        prodotto.setDescrizione(form.getDescrizione());
        prodotto.setPrezzo(form.getPrezzo());
        prodotto.setQuantitaDisponibile(form.getQuantitaDisponibile());
        prodotto.setCategoria(form.getCategoria());

        // 📸 Aggiunge nuove foto, senza cancellare le esistenti
        if (form.getFoto() != null) {
            for (MultipartFile file : form.getFoto()) {
                try {
                    if (!file.isEmpty()) {
                        FotoProdotto nuovaFoto = new FotoProdotto(file.getBytes());
                        prodotto.addFoto(nuovaFoto);
                    }
                } catch (IOException e) {
                    model.addAttribute("erroreFile", true);
                    model.addAttribute("prodotto", prodotto);
                    return "shopView/modificaProdotto";
                }
            }
        }

        prodottoDao.save(prodotto);
        return "redirect:/admin/prodotti";
    }

    // ❌ Rimuovi singola foto (AJAX DELETE)
    @DeleteMapping("/{idProdotto}/foto/rimuovi/{idFoto}")
    @ResponseBody
    public ResponseEntity<Void> rimuoviFoto(@PathVariable Long idProdotto, @PathVariable Long idFoto) {
        Prodotto prodotto = prodottoDao.findById(idProdotto)
                .orElseThrow(() -> new IllegalArgumentException("Prodotto non trovato"));

        boolean removed = prodotto.getFoto().removeIf(f -> f.getId().equals(idFoto));
        if (removed) {
            prodottoDao.save(prodotto);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // ❌ Rimuovi intero prodotto
    @GetMapping("/rimuovi/{id}")
    public String rimuoviProdotto(@PathVariable Long id) {
        prodottoDao.deleteById(id);
        return "redirect:/admin/prodotti";
    }
}
