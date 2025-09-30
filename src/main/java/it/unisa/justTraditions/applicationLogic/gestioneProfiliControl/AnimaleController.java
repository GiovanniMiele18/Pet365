package it.unisa.justTraditions.applicationLogic.gestioneProfiliControl;

import it.unisa.justTraditions.applicationLogic.autenticazioneControl.util.SessionCliente;
import it.unisa.justTraditions.applicationLogic.gestioneProfiliControl.form.AggiuntaAnimaleForm;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Foto;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.AnimaleService;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Animale;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Cliente;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.FotoAnimale;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/aggiungiAnimale")
public class AnimaleController {

    private static final String animaleFormView = "gestioneProfiliView/animaleForm";
    private static final String dettagliClienteView = "gestioneProfiliView/clienteDettagli";

    @Autowired
    private AnimaleService animaleService;

    @Autowired
    private SessionCliente sessionCliente;

    @GetMapping
    public String showForm(@ModelAttribute("animaleForm") AggiuntaAnimaleForm animaleForm) {
        // 🔒 controllo login
        if (sessionCliente.getCliente().isEmpty()) {
            return "redirect:/login";
        }
        return animaleFormView;
    }

    @PostMapping
    public String salvaAnimale(@ModelAttribute("animaleForm") @Valid AggiuntaAnimaleForm animaleForm,
                               BindingResult bindingResult,
                               Model model) {
        // 🔒 controllo login
        if (sessionCliente.getCliente().isEmpty()) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            return animaleFormView;
        }

        Cliente cliente = sessionCliente.getCliente().get();

        Animale animale = new Animale();
        animale.setNome(animaleForm.getNome().trim());
        animale.setSpecie(animaleForm.getSpecie().trim());
        animale.setRazza(animaleForm.getRazza());
        animale.setEta(animaleForm.getEta());
        animale.setSesso(animaleForm.getSesso());
        animale.setPeso(animaleForm.getPeso());
        animale.setColore(animaleForm.getColore());
        animale.setNote(animaleForm.getNote());
        animale.setCliente(cliente);

        // gestione foto (se ci sono)
        if (animaleForm.getFoto() != null) {
            for (MultipartFile file : animaleForm.getFoto()) {
                try {
                    animale.addFoto(new FotoAnimale(file.getBytes()));
                } catch (IOException e) {
                    model.addAttribute("erroreFile", true);
                    return animaleFormView;
                }
            }
        }

        animaleService.aggiungiAnimale(cliente.getId(), animale);

        return "redirect:/visualizzazioneProfiloPersonale";
    }
}
