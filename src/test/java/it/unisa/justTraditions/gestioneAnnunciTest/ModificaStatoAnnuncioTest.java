package it.unisa.justTraditions.gestioneAnnunciTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import it.unisa.justTraditions.applicationLogic.autenticazioneControl.util.SessionAmministratore;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.dao.AnnuncioDao;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Annuncio;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Foto;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.dao.AmministratoreDao;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Amministratore;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Artigiano;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

/**
 * Implementa il test di unità per ModificaStatoAnnuncioController.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ModificaStatoAnnuncioTest {

  private static final String modificaStatoAnnuncioView =
      "gestioneAnnunciView/modificaStatoAnnuncio";
  private static final String modificaAnnuncioSuccessView =
      "gestioneAnnunciView/modificaAnnuncioSuccess";

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AnnuncioDao annuncioDao;

  @MockBean
  private AmministratoreDao amministratoreDao;

  @MockBean
  private SessionAmministratore sessionAmministratore;

  @Test
  public void inRevisioneVersoPropostoNonConsentito()
      throws Exception {
    test(Annuncio.Stato.PROPOSTO, "", view().name(modificaStatoAnnuncioView));
  }

  @Test
  public void rifiutatoConMotivoDelRifiutoVuotoNonConsentito()
      throws Exception {
    test(Annuncio.Stato.RIFIUTATO, "",
        view().name(modificaStatoAnnuncioView));
  }

  @Test
  public void motivoDelRifiutoTroppoLungo()
      throws Exception {
    test(Annuncio.Stato.RIFIUTATO,
        "hcidbcsdcnsbkmcxmbxcuibebfkmdsifuirbmdbdofoierhfurbcm vdfkfhrbjvbirjifghre"
            + "iohgkjnkjnvjdfnasònaaoegferjafpeir94t547685yghdknfvfkjn0392’23058485345t74tghfviev"
            + "nerhgihsosnfdiohfregfdfsodoehrbvvxcn,msdpofjerhgurvdfls ojferfgeuldfiurhrigishs"
            + "jldhf8efnvjkfndvvoòghneog",
        view().name(modificaStatoAnnuncioView));
  }

  @Test
  public void operazioneEffettuata()
      throws Exception {
    test(Annuncio.Stato.RIFIUTATO,
        "L’annuncio è stato rifiutato perché non conforme agli standard della piattaforma",
        view().name(modificaAnnuncioSuccessView));
  }

  @Test
  public void nuovoStatoApprovato()
      throws Exception {
    test(Annuncio.Stato.APPROVATO, "", view().name(modificaAnnuncioSuccessView));
  }

  private void test(Annuncio.Stato nuovoStato, String motivoDelRifiuto, ResultMatcher resultMatcher)
      throws Exception {
    Annuncio annuncio = new Annuncio();
    annuncio.setStato(Annuncio.Stato.IN_REVISIONE);
    annuncio.addFoto(new Foto());
    Artigiano artigiano = new Artigiano();
    artigiano.addAnnuncio(annuncio);
    Amministratore amministratore = new Amministratore();

    when(annuncioDao.findById(any())).thenReturn(Optional.of(annuncio));
    when(sessionAmministratore.getAmministratore())
        .thenReturn(Optional.of(amministratore));

    mockMvc.perform(post("/modificaStatoAnnuncio")
        .param("idAnnuncio", String.valueOf(1L))
        .param("nuovoStato", String.valueOf(nuovoStato))
        .param("motivoDelRifiuto", motivoDelRifiuto)
    ).andExpect(resultMatcher).andDo(result -> {
      if (result.getModelAndView().getViewName().equals(modificaStatoAnnuncioView)) {
        assertEquals("stato modificato", Annuncio.Stato.IN_REVISIONE, annuncio.getStato());
        assertEquals("motivo del rifiuto modificato", null, annuncio.getMotivoDelRifiuto());
      } else if (result.getModelAndView().getViewName().equals(modificaAnnuncioSuccessView)) {
        assertEquals("stato non modificato", nuovoStato, annuncio.getStato());

        if (nuovoStato.equals(Annuncio.Stato.RIFIUTATO)) {
          assertEquals("motivo del rifiuto non modificato", motivoDelRifiuto,
              annuncio.getMotivoDelRifiuto());
        } else {
          assertEquals("motivo del rifiuto modificato", null,
              annuncio.getMotivoDelRifiuto());
        }

        if (nuovoStato.equals(Annuncio.Stato.APPROVATO)) {
          assertEquals("amministratore non aggiunto", amministratore, annuncio.getAmministratore());
        } else {
          assertEquals("amministratore aggiunto", null, annuncio.getAmministratore());
        }
      }
    });
  }

  @Test
  public void approvatoVersoInRevisione()
      throws Exception {
    Annuncio annuncio = new Annuncio();
    annuncio.setStato(Annuncio.Stato.APPROVATO);
    annuncio.addFoto(new Foto());
    Artigiano artigiano = new Artigiano();
    artigiano.addAnnuncio(annuncio);
    Amministratore amministratore = new Amministratore();
    amministratore.addAnnuncioApprovato(annuncio);

    when(annuncioDao.findById(any())).thenReturn(Optional.of(annuncio));
    when(sessionAmministratore.getAmministratore())
        .thenReturn(Optional.of(new Amministratore()));

    mockMvc.perform(post("/modificaStatoAnnuncio")
        .param("idAnnuncio", String.valueOf(1L))
        .param("nuovoStato", String.valueOf(Annuncio.Stato.IN_REVISIONE))
    ).andExpect(view().name(modificaAnnuncioSuccessView));

    assertEquals("stato non modificato", Annuncio.Stato.IN_REVISIONE, annuncio.getStato());
    assertEquals("amministratore non rimosso", null, annuncio.getAmministratore());
  }

  @Test
  public void propostoVersoInRevisione()
      throws Exception {
    Annuncio annuncio = new Annuncio();
    annuncio.setStato(Annuncio.Stato.PROPOSTO);
    annuncio.addFoto(new Foto());
    Artigiano artigiano = new Artigiano();
    artigiano.addAnnuncio(annuncio);

    when(annuncioDao.findById(any())).thenReturn(Optional.of(annuncio));
    when(sessionAmministratore.getAmministratore())
        .thenReturn(Optional.of(new Amministratore()));

    mockMvc.perform(post("/modificaStatoAnnuncio")
        .param("idAnnuncio", String.valueOf(1L))
        .param("nuovoStato", String.valueOf(Annuncio.Stato.IN_REVISIONE))
    ).andExpect(view().name(modificaAnnuncioSuccessView));

    assertEquals("stato non modificato", Annuncio.Stato.IN_REVISIONE, annuncio.getStato());
  }
}
