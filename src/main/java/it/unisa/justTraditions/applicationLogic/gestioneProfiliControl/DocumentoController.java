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
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

    /**
     * Recupera il cliente autenticato o lancia eccezione
     */
    private Cliente getClienteAutenticato() {
        return sessionCliente.getCliente()
                .orElseThrow(() -> new RuntimeException("Utente non autenticato"));
    }

    /**
     * GET -> Mostra form per aggiungere documento
     */
    @GetMapping("/aggiungi/{animaleId}")
    public String showForm(@PathVariable Long animaleId, Model model) {
        Cliente cliente = getClienteAutenticato();

        Animale animale = animaleService.findById(animaleId)
                .orElseThrow(() -> new RuntimeException("Animale non trovato"));

        if (!animale.getCliente().getId().equals(cliente.getId())) {
            model.addAttribute("message", "Non puoi aggiungere un documento a un animale che non ti appartiene!");
            return "error";
        }

        model.addAttribute("documentoForm", new AggiuntaDocumentoForm());
        model.addAttribute("animaleId", animaleId);
        return "gestioneProfiliView/aggiungiDocumento";
    }

    /**
     * POST -> Salvataggio documento
     */
    @PostMapping("/aggiungi/{animaleId}")
    public String salvaDocumento(@PathVariable Long animaleId,
                                 @Valid @ModelAttribute("documentoForm") AggiuntaDocumentoForm documentoForm,
                                 BindingResult result,
                                 Model model) {
        Cliente cliente = getClienteAutenticato();

        Animale animale = animaleService.findById(animaleId)
                .orElseThrow(() -> new RuntimeException("Animale non trovato"));

        if (!animale.getCliente().getId().equals(cliente.getId())) {
            model.addAttribute("message", "Non puoi aggiungere un documento a un animale che non ti appartiene!");
            return "error";
        }

        if (result.hasErrors()) {
            return "gestioneProfiliView/aggiungiDocumento";
        }

        try {
            MultipartFile file = documentoForm.getFile();
            if (file == null || file.isEmpty()) {
                model.addAttribute("message", "Devi selezionare un file da caricare!");
                return "error";
            }

            // --- Estrai estensione dal file originale ---
            String fileOriginale = file.getOriginalFilename();
            String estensione = "";
            if (fileOriginale != null && fileOriginale.contains(".")) {
                estensione = fileOriginale.substring(fileOriginale.lastIndexOf(".")); // ".pdf", ".jpg" ecc.
            }

            // --- Salva documento (metadati) ---
            Documento documento = new Documento();
            documento.setNome(documentoForm.getNome());       // nome logico
            documento.setDescrizione(documentoForm.getDescrizione());
            documento.setTipo(file.getContentType());         // MIME type
            documento.setEstensione(estensione);              // estensione file
            documento.setAnimale(animale);
            documentoDao.save(documento);

            // --- Salva contenuto binario ---
            DatiDocumento datiDocumento = new DatiDocumento(file.getBytes(), documento);
            documento.setDatiDocumento(datiDocumento);
            datiDocumentoDao.save(datiDocumento);

        } catch (IOException e) {
            model.addAttribute("message", "Errore durante il caricamento del file!");
            return "error";
        }

        return "redirect:/visualizzazioneProfiloPersonale";
    }

    /**
     * GET -> Download documento
     */
    @GetMapping("/download/{documentoId}")
    public ResponseEntity<byte[]> downloadDocumento(@PathVariable Long documentoId) {
        Documento documento = documentoDao.findById(documentoId)
                .orElseThrow(() -> new RuntimeException("Documento non trovato"));

        DatiDocumento datiDocumento = documento.getDatiDocumento();
        if (datiDocumento == null) {
            throw new RuntimeException("Dati documento mancanti");
        }

        // --- Nome file completo = nome logico + estensione ---
        String nomeFile = documento.getNome();
        if (documento.getEstensione() != null && !documento.getEstensione().isBlank()) {
            nomeFile += documento.getEstensione();
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeFile + "\"")
                .contentType(MediaType.parseMediaType(
                        documento.getTipo() != null ? documento.getTipo() : MediaType.APPLICATION_OCTET_STREAM_VALUE
                ))
                .body(datiDocumento.getDati());
    }

    @PostMapping("/elimina/{documentoId}")
    public String eliminaDocumento(@PathVariable Long documentoId, Model model) {
        Cliente cliente = getClienteAutenticato();

        Documento documento = documentoDao.findById(documentoId)
                .orElseThrow(() -> new RuntimeException("Documento non trovato"));

        // Controllo che il documento appartenga ad un animale del cliente loggato
        if (!documento.getAnimale().getCliente().getId().equals(cliente.getId())) {
            model.addAttribute("message", "Non puoi eliminare un documento che non ti appartiene!");
            return "error";
        }

        documentoDao.delete(documento);

        return "redirect:/visualizzazioneProfiloPersonale";
    }

}
