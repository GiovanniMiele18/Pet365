package it.unisa.justTraditions.applicationLogic.shopControl;

import it.unisa.justTraditions.applicationLogic.autenticazioneControl.util.SessionCliente;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Cliente;
import it.unisa.justTraditions.storage.shopStorage.dao.OssoPointDao;
import it.unisa.justTraditions.storage.shopStorage.dao.ProdottoDao;
import it.unisa.justTraditions.storage.shopStorage.entity.OssoPoint;
import it.unisa.justTraditions.storage.shopStorage.entity.Prodotto;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * Controller per la gestione del carrello e del checkout nello shop Pet365.
 */
@Controller
@RequestMapping("/carrello")
public class CarrelloController {

    @Autowired
    private ProdottoDao prodottoDao;

    @Autowired
    private OssoPointDao ossoPointDao;

    @Autowired
    private SessionCliente sessionCliente;

    /**
     * 📦 Mostra il carrello con i prodotti correnti.
     */
    @GetMapping
    public String visualizzaCarrello(HttpSession session, Model model) {
        Map<Long, Integer> carrello = (Map<Long, Integer>) session.getAttribute("carrello");
        if (carrello == null) carrello = new HashMap<>();

        List<Prodotto> prodotti = prodottoDao.findAllById(carrello.keySet());
        final Map<Long, Integer> carrelloFinal = carrello;

        // ✅ Calcolo totale prodotti
        BigDecimal totaleProdotti = prodotti.stream()
                .filter(p -> carrelloFinal.containsKey(p.getId()))
                .map(p -> p.getPrezzo().multiply(BigDecimal.valueOf(carrelloFinal.getOrDefault(p.getId(), 0))))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal spedizione = totaleProdotti.compareTo(new BigDecimal("50")) >= 0
                ? BigDecimal.ZERO
                : new BigDecimal("3.99");

        BigDecimal totale = totaleProdotti.add(spedizione);

        model.addAttribute("prodotti", prodotti);
        model.addAttribute("carrello", carrello);
        model.addAttribute("totaleProdotti", totaleProdotti);
        model.addAttribute("spedizione", spedizione);
        model.addAttribute("totale", totale);

        return "shopView/carrello";
    }

    /**
     * 🔍 Verifica disponibilità prodotto (AJAX)
     */
    @GetMapping("/verifica/{id}")
    @ResponseBody
    public Map<String, Object> verificaDisponibilita(@PathVariable Long id, @RequestParam int quantita) {
        Map<String, Object> res = new HashMap<>();
        Optional<Prodotto> opt = prodottoDao.findById(id);

        if (opt.isEmpty()) {
            res.put("ok", false);
            res.put("msg", "Prodotto non trovato");
            return res;
        }

        Prodotto p = opt.get();

        if (p.getQuantitaDisponibile() == 0) {
            res.put("ok", false);
            res.put("msg", "Prodotto esaurito ❌");
            res.put("disponibili", 0);
            return res;
        }

        if (quantita > p.getQuantitaDisponibile()) {
            res.put("ok", false);
            res.put("msg", "Solo " + p.getQuantitaDisponibile() + " pezzi disponibili ❗");
            res.put("disponibili", p.getQuantitaDisponibile());
        } else {
            res.put("ok", true);
        }

        return res;
    }

    /**
     * ➕ Aggiunge un prodotto al carrello (AJAX)
     */
    @PostMapping("/aggiungi/{id}")
    @ResponseBody
    public Map<String, Object> aggiungiProdotto(@PathVariable Long id, @RequestParam int quantita, HttpSession session) {
        Map<String, Object> res = new HashMap<>();

        Map<Long, Integer> carrello = (Map<Long, Integer>) session.getAttribute("carrello");
        if (carrello == null) carrello = new HashMap<>();

        Prodotto p = prodottoDao.findById(id).orElse(null);
        if (p == null) {
            res.put("ok", false);
            res.put("msg", "Prodotto non trovato");
            return res;
        }

        if (p.getQuantitaDisponibile() == 0) {
            res.put("ok", false);
            res.put("msg", "Prodotto esaurito ❌");
            return res;
        }

        int nuovaQuantita = carrello.getOrDefault(id, 0) + quantita;
        if (nuovaQuantita > p.getQuantitaDisponibile()) {
            res.put("ok", false);
            res.put("msg", "Solo " + p.getQuantitaDisponibile() + " pezzi disponibili ❗");
            return res;
        }

        carrello.put(id, nuovaQuantita);
        session.setAttribute("carrello", carrello);

        String nome = p.getNome();
        String msg = (quantita > 1)
                ? quantita + "x " + nome + " aggiunti al carrello 🛒"
                : nome + " aggiunto al carrello 🛒";

        res.put("ok", true);
        res.put("msg", msg);
        res.put("cartCount", carrello.values().stream().mapToInt(Integer::intValue).sum());
        return res;
    }

    /**
     * 🗑️ Rimuove un prodotto dal carrello (AJAX)
     */
    @PostMapping("/rimuovi/{id}")
    @ResponseBody
    public ResponseEntity<?> rimuoviProdotto(@PathVariable Long id, HttpSession session) {
        Map<Long, Integer> carrello = (Map<Long, Integer>) session.getAttribute("carrello");
        if (carrello != null) {
            carrello.remove(id);
            session.setAttribute("carrello", carrello);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 💳 Checkout completo: verifica stock, aggiorna punti e riduce quantità disponibili.
     */
    @PostMapping("/checkout")
    @Transactional
    public String checkout(HttpSession session, Model model) {
        Map<Long, Integer> carrello = (Map<Long, Integer>) session.getAttribute("carrello");
        if (carrello == null || carrello.isEmpty()) {
            model.addAttribute("errore", "Il carrello è vuoto!");
            return "shopView/carrello";
        }

        Optional<Cliente> clienteOpt = sessionCliente.getCliente();
        if (clienteOpt.isEmpty()) {
            session.setAttribute("redirectAfterLogin", "/carrello");
            return "redirect:/login";
        }

        Cliente cliente = clienteOpt.get();
        List<Prodotto> prodotti = prodottoDao.findAllById(carrello.keySet());
        final Map<Long, Integer> carrelloFinal = carrello;

        BigDecimal totaleProdotti = BigDecimal.ZERO;
        List<String> errori = new ArrayList<>();

        for (Prodotto p : prodotti) {
            int richiesta = carrelloFinal.getOrDefault(p.getId(), 0);
            if (richiesta > p.getQuantitaDisponibile()) {
                errori.add("Il prodotto '" + p.getNome() + "' ha solo " + p.getQuantitaDisponibile() + " pezzi disponibili.");
            } else {
                totaleProdotti = totaleProdotti.add(p.getPrezzo().multiply(BigDecimal.valueOf(richiesta)));
            }
        }

        if (!errori.isEmpty()) {
            model.addAttribute("errore", String.join("<br>", errori));
            return "shopView/carrello";
        }

        BigDecimal spedizione = totaleProdotti.compareTo(new BigDecimal("50")) >= 0
                ? BigDecimal.ZERO
                : new BigDecimal("3.99");

        BigDecimal totale = totaleProdotti.add(spedizione);

        for (Prodotto p : prodotti) {
            int richiesta = carrelloFinal.getOrDefault(p.getId(), 0);
            if (richiesta > 0) {
                p.setQuantitaDisponibile(Math.max(0, p.getQuantitaDisponibile() - richiesta));
                prodottoDao.save(p);
            }
        }

        OssoPoint punti = ossoPointDao.findByCliente(cliente).orElseGet(() -> {
            OssoPoint o = new OssoPoint();
            o.setCliente(cliente);
            o.setValore(BigDecimal.ZERO);
            return o;
        });

        BigDecimal puntiGuadagnati = totaleProdotti.divide(new BigDecimal("25"), 2, BigDecimal.ROUND_HALF_UP);
        punti.setValore(punti.getValore().add(puntiGuadagnati));
        ossoPointDao.saveAndFlush(punti);

        session.removeAttribute("carrello");

        model.addAttribute("cliente", cliente);
        model.addAttribute("totale", totale);
        model.addAttribute("totaleProdotti", totaleProdotti);
        model.addAttribute("spedizione", spedizione);
        model.addAttribute("puntiGuadagnati", puntiGuadagnati);
        model.addAttribute("saldoPunti", punti.getValore());

        return "shopView/confermaAcquisto";
    }
}
