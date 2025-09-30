package it.unisa.justTraditions.prenotazioniTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import it.unisa.justTraditions.applicationLogic.autenticazioneControl.util.SessionCliente;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.dao.AnnuncioDao;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Annuncio;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Artigiano;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Utente;
import it.unisa.justTraditions.storage.prenotazioniStorage.dao.PrenotazioneDao;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

/**
 * Implementa il test di unità per VisualizzazionePrenotazioniAnnuncioController.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class VisualizzazionePrenotazioniAnnuncioTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AnnuncioDao annuncioDao;

  @MockBean
  private PrenotazioneDao prenotazioneDao;

  @MockBean
  private SessionCliente sessionCliente;

  @Test
  public void dataNonRispettaIlFormato()
      throws Exception {
    test("12/25/2022", status().isBadRequest());
  }

  @Test
  public void dataRispettaIlFormato()
      throws Exception {
    test("2023-02-13", status().isOk());
  }

  private void test(String data, ResultMatcher resultMatcher)
      throws Exception {
    Artigiano artigiano = new Artigiano();
    Annuncio annuncio = new Annuncio();
    artigiano.addAnnuncio(annuncio);

    when(annuncioDao.findById(any())).thenReturn(Optional.of(annuncio));
    when(sessionCliente.getCliente()).thenReturn(Optional.of(artigiano));
    when(prenotazioneDao.findByVisitaAnnuncioAndDataVisita(any(), any(), any())).thenReturn(
        Page.empty());

    mockMvc.perform(post("/visualizzazionePrenotazioniAnnuncio")
        .param("idAnnuncio", String.valueOf(1L))
        .param("dataVisita", data)
    ).andExpect(resultMatcher);
  }

  @Test
  public void annuncioNonAppartieneAdArtigianoLoggato()
      throws Exception {
    Artigiano artigiano = new Artigiano();
    Field field = Utente.class.getDeclaredField("id");
    field.setAccessible(true);
    field.set(artigiano, 1L);
    Annuncio annuncio = new Annuncio();
    artigiano.addAnnuncio(annuncio);

    Artigiano artigiano1 = new Artigiano();
    field.set(artigiano1, 2L);

    when(annuncioDao.findById(any())).thenReturn(Optional.of(annuncio));
    when(sessionCliente.getCliente()).thenReturn(Optional.of(artigiano1));
    when(prenotazioneDao.findByVisitaAnnuncioAndDataVisita(any(), any(), any())).thenReturn(
        Page.empty());

    assertThatThrownBy(
        () -> mockMvc.perform(post("/visualizzazionePrenotazioniAnnuncio")
            .param("idAnnuncio", String.valueOf(1L))
            .param("dataVisita", "2023-02-13")
        )
    ).hasCause(new IllegalArgumentException());
  }

  @Test
  public void paginaNonValida() {
    Artigiano artigiano = new Artigiano();
    Annuncio annuncio = new Annuncio();
    artigiano.addAnnuncio(annuncio);

    when(annuncioDao.findById(any())).thenReturn(Optional.of(annuncio));
    when(sessionCliente.getCliente()).thenReturn(Optional.of(artigiano));
    when(prenotazioneDao.findByVisitaAnnuncioAndDataVisita(any(), any(), any())).thenReturn(
        Page.empty());

    assertThatThrownBy(
        () -> mockMvc.perform(post("/visualizzazionePrenotazioniAnnuncio")
            .param("idAnnuncio", String.valueOf(1L))
            .param("dataVisita", "2023-02-13")
            .param("pagina", "1")
        )
    ).hasCause(new IllegalArgumentException());
  }

  @Test
  public void zeroPagine()
      throws Exception {
    Artigiano artigiano = new Artigiano();
    Annuncio annuncio = new Annuncio();
    artigiano.addAnnuncio(annuncio);

    when(annuncioDao.findById(any())).thenReturn(Optional.of(annuncio));
    when(sessionCliente.getCliente()).thenReturn(Optional.of(artigiano));
    when(prenotazioneDao.findByVisitaAnnuncioAndDataVisita(any(), any(), any())).thenReturn(
        new PageImpl<>(List.of(), PageRequest.ofSize(1), 0));

    mockMvc.perform(post("/visualizzazionePrenotazioniAnnuncio")
        .param("idAnnuncio", String.valueOf(1L))
        .param("dataVisita", "2023-02-13")
    ).andExpect(status().isOk());
  }
}
