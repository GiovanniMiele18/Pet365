package it.unisa.justTraditions.applicationLogic.autenticazioneControl.filter;

import it.unisa.justTraditions.applicationLogic.autenticazioneControl.util.SessionCliente;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implementa il filtro per le pagine riservate al Cliente.
 */
@Component
public class ClienteLoginFilter
    implements Filter {

  @Autowired
  private SessionCliente sessionCliente;

  /**
   * Fa sì che venga richiamato il filtro successivo nella catena o,
   * se il filtro chiamante è l'ultimo filtro nella catena,
   * che venga richiamata la risorsa alla fine della catena o,
   * se l utente loggato non e un Cliente viene reindirizzato alla control login.
   *
   * @param servletRequest  La richiesta da passaggio lungo la catena.
   * @param servletResponse La risposta da passare lungo la catena.
   * @param filterChain     Utilizzato per richiamare il filtro successivo.
   * @throws IOException      se si è verificato un errore relativo all'I/O durante l'elaborazione.
   * @throws ServletException se si verifica un'eccezione che interferisce con il
   *                          normale funzionamento del filtro.
   */
  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                       FilterChain filterChain) throws IOException, ServletException {
    if (sessionCliente.getCliente().isPresent()) {
      filterChain.doFilter(servletRequest, servletResponse);
    } else {
      ((HttpServletResponse) servletResponse).sendRedirect("/login");
    }
  }
}
