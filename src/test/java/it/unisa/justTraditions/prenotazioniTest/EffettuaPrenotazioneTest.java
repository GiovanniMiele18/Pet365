package it.unisa.justTraditions.prenotazioniTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import it.unisa.justTraditions.applicationLogic.autenticazioneControl.util.SessionCliente;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.dao.VisitaDao;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Annuncio;
import it.unisa.justTraditions.storage.gestioneAnnunciStorage.entity.Visita;
import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Cliente;
import it.unisa.justTraditions.storage.prenotazioniStorage.dao.PrenotazioneDao;
import it.unisa.justTraditions.storage.prenotazioniStorage.entity.Prenotazione;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

/**
 * Implementa il test di unità per EffettuaPrenotazioneController.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class EffettuaPrenotazioneTest {

  private static final Long idVisitaValido = 1L;
  private static final LocalDate dataVisitaValida = LocalDate.now().plusDays(1L);

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private VisitaDao visitaDao;

  @MockBean
  private SessionCliente sessionCliente;

  @MockBean
  private PrenotazioneDao prenotazioneDao;

  @Test
  public void formatoDataErrato() {
    assertThatThrownBy(
        () -> test("05/12/2022", idVisitaValido, 5, null)
    ).hasCause(new IllegalArgumentException());
  }

  @Test
  public void dataNelPassato() {
    LocalDate localDate = LocalDate.now().minusDays(1L);
    assertThatThrownBy(
        () -> test(localDate.toString(), idVisitaValido, 5, null)
    ).hasCause(new IllegalArgumentException());
  }

  @Test
  public void dataNonValida() {
    LocalDate localDate = LocalDate.now().plusDays(2L);
    assertThatThrownBy(
        () -> test(localDate.toString(), idVisitaValido, 5, null)
    ).hasCause(new IllegalArgumentException());
  }

  @Test
  public void idVisitaNonValido() {
    assertThatThrownBy(
        () -> test(dataVisitaValida.toString(), 0L, 5, null)
    ).hasCause(new IllegalArgumentException());
  }

  @Test
  public void numeroPersoneNegativo() {
    assertThatThrownBy(
        () -> test(dataVisitaValida.toString(), idVisitaValido, -1, null)
    ).hasCause(new IllegalArgumentException());
  }

  @Test
  public void postiNonDisponibili() {
    assertThatThrownBy(
        () -> test(dataVisitaValida.toString(), idVisitaValido, 3, null)
    ).hasCause(new IllegalArgumentException());
  }

  @Test
  public void prenotazioneEffettuata()
      throws Exception {
    test(dataVisitaValida.toString(), idVisitaValido, 2, status().isOk());
  }

  private void test(String dataVisita, Long idVisita, Integer numeroPersone,
                    ResultMatcher resultMatcher) throws Exception {
    Visita visita = new Visita();
    visita.setValidita(true);
    visita.setGiorno(dataVisitaValida.getDayOfWeek());
    Annuncio annuncio = new Annuncio();
    annuncio.addVisita(visita);
    annuncio.setNumMaxPersonePerVisita(2);

    when(visitaDao.findById(any())).thenAnswer((Answer<Optional<Visita>>) invocationOnMock -> {
      if (invocationOnMock.getArgument(0, Long.class).equals(idVisitaValido)) {
        return Optional.of(visita);
      } else {
        return Optional.empty();
      }
    });
    when(sessionCliente.getCliente()).thenReturn(Optional.of(new Cliente()));
    when(prenotazioneDao.findByVisitaAndDataVisita(any(), any())).thenReturn(List.of());

    mockMvc.perform(post("/effettuaPrenotazione")
        .param("idVisita", String.valueOf(idVisita))
        .param("numeroPersone", String.valueOf(numeroPersone))
        .param("dataVisita", dataVisita)
    ).andExpect(resultMatcher).andDo(result -> {
      if (result.getResponse().getStatus() == HttpServletResponse.SC_OK) {
        Prenotazione prenotazione = visita.getPrenotazioni().get(0);

        assertNotNull("prenotazione nulla", prenotazione);
        assertEquals("dataVisita non inserita", LocalDate.parse(dataVisita),
            prenotazione.getDataVisita());
        assertEquals("numeroPersone non inserito", numeroPersone,
            prenotazione.getNumPersonePrenotate());
      }
    });
  }
}
