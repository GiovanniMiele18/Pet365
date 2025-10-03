package it.unisa.justTraditions.applicationLogic.gestioneAnnunciControl;

import it.unisa.justTraditions.applicationLogic.autenticazioneControl.util.SessionCliente;
import it.unisa.justTraditions.applicationLogic.gestioneAnnunciControl.form.AnnuncioForm;
import it.unisa.justTraditions.applicationLogic.gestioneAnnunciControl.form.VisitaForm;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.dao.AnnuncioDao;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Annuncio;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Foto;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Artigiano;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/modificaAnnuncio")
public class ModificaAnnuncioController {

  private static final String modificaAnnuncioView = "gestioneAnnunciView/modificaAnnuncio";
  private static final String modificaAnnuncioSuccessView = "gestioneAnnunciView/modificaAnnuncioSuccess";

  @Autowired
  private AnnuncioDao annuncioDao;

  @Autowired
  private SessionCliente sessionCliente;

  @GetMapping
  public String get(@ModelAttribute AnnuncioForm annuncioForm, Long id, Model model) {
    Annuncio annuncio = annuncioDao.findById(id).orElseThrow();
    annuncioForm.setIdAnnuncio(annuncio.getId());
    annuncioForm.setNomeAttivita(annuncio.getNomeAttivita());
    annuncioForm.setProvinciaAttivita(annuncio.getProvinciaAttivita());
    annuncioForm.setIndirizzoAttivita(annuncio.getIndirizzoAttivita());
    annuncioForm.setDescrizione(annuncio.getDescrizione());
    annuncioForm.setServiziOfferti(AnnuncioForm.ServiziOffertiEnum.valueOf(annuncio.getServiziOfferti().name()));
    model.addAttribute("idFoto", annuncio.getFoto().stream().map(Foto::getId).collect(Collectors.toList()));
    return modificaAnnuncioView;
  }

  @PostMapping
  public String post(@ModelAttribute @Valid AnnuncioForm annuncioForm,
                     BindingResult bindingResult, Model model) {

    if (bindingResult.hasErrors()) {
      return modificaAnnuncioView;
    }

    Annuncio annuncio = annuncioDao.findById(annuncioForm.getIdAnnuncio()).orElseThrow();

    annuncio.setNomeAttivita(annuncioForm.getNomeAttivita());
    annuncio.setProvinciaAttivita(annuncioForm.getProvinciaAttivita());
    annuncio.setIndirizzoAttivita(annuncioForm.getIndirizzoAttivita());
    annuncio.setDescrizione(annuncioForm.getDescrizione());
    annuncio.setServiziOfferti(Annuncio.ServizioOfferto.valueOf(annuncioForm.getServiziOfferti().name()));

    for (MultipartFile file : annuncioForm.getFoto()) {
      try {
        annuncio.addFoto(new Foto(file.getBytes()));
      } catch (IOException e) {
        model.addAttribute("erroreFile", true);
        return modificaAnnuncioView;
      }
    }

    annuncioDao.save(annuncio);
    return modificaAnnuncioSuccessView;
  }
}
