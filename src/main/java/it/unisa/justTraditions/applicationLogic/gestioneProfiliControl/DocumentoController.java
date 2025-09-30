package it.unisa.justTraditions.applicationLogic.gestioneProfiliControl;

import it.unisa.justTraditions.applicationLogic.autenticazioneControl.util.SessionCliente;
import it.unisa.justTraditions.applicationLogic.gestioneProfiliControl.form.AggiuntaDocumentoForm;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.AnimaleService;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.dao.DatiDocumentoDao;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.dao.DocumentoDao;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Animale;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Cliente;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.DatiDocumento;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Documento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/documenti")
public class DocumentoController {

    @Autowired
    private DocumentoDao documentoDao;

    @Autowired
    private DatiDocumentoDao datiDocumentoDao;

    @Autowired
    private AnimaleService animaleService;

    @Autowired
    private SessionCliente sessionCliente;

    // GET -> Mostra form caricamento documento
    @GetMapping("/aggiungi/{animaleId}")
    public String showForm(@PathVariable Long animaleId, Model model) {
        if (sessionCliente.getCliente().isEmpty()) {
            return "redirect:/login";
        }

        Cliente cliente = sessionCliente.getCliente().get();

        Animale animale = animaleService.findById(animaleId)
                .orElseThrow(() -> new RuntimeException("Animale non trovato"));

        // 🔒 Controllo ownership
        if (!animale.getCliente().getId().equals(cliente.getId())) {
            model.addAttribute("message",
                    "Non puoi aggiungere un documento a un animale che non ti appartiene!");
            model.addAttribute("cliente", cliente);
            return "error"; // ora punta direttamente a templates/error.html
        }

        model.addAttribute("documentoForm", new AggiuntaDocumentoForm());
        model.addAttribute("animaleId", animaleId);

        return "gestioneProfiliView/aggiungiDocumento";
    }

    // POST -> Salva documento
    @PostMapping("/aggiungi/{animaleId}")
    public String salvaDocumento(@PathVariable Long animaleId,
                                 @ModelAttribute AggiuntaDocumentoForm documentoForm,
                                 Model model) {
        if (sessionCliente.getCliente().isEmpty()) {
            return "redirect:/login";
        }

        Cliente cliente = sessionCliente.getCliente().get();

        Animale animale = animaleService.findById(animaleId)
                .orElseThrow(() -> new RuntimeException("Animale non trovato"));

        // 🔒 Controllo ownership
        if (!animale.getCliente().getId().equals(cliente.getId())) {
            model.addAttribute("message",
                    "Non puoi aggiungere un documento a un animale che non ti appartiene!");
            model.addAttribute("cliente", cliente);
            return "error";
        }

        try {
            MultipartFile file = documentoForm.getFile();
            if (file == null || file.isEmpty()) {
                model.addAttribute("message", "Devi selezionare un file da caricare!");
                model.addAttribute("cliente", cliente);
                return "error";
            }

            // Salvataggio documento
            Documento documento = new Documento();
            documento.setNome(documentoForm.getNome());
            documento.setDescrizione(documentoForm.getDescrizione());
            documento.setTipo(file.getContentType());
            documento.setAnimale(animale);
            documentoDao.save(documento);

            // Salvataggio dati documento
            DatiDocumento datiDocumento = new DatiDocumento(file.getBytes(),documento);
            datiDocumento.setDocumento(documento);
            documento.setDatiDocumento(datiDocumento);
            datiDocumentoDao.save(datiDocumento);

        } catch (IOException e) {
            model.addAttribute("message", "Errore durante il caricamento del file!");
            model.addAttribute("cliente", cliente);
            return "error";
        }

        return "redirect:/visualizzazioneProfiloPersonale";
    }
}
