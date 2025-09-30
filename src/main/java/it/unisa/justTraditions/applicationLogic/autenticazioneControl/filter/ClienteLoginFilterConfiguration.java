package it.unisa.justTraditions.applicationLogic.autenticazioneControl.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Implementa la creazione di un FilterRegistrationBean
 * per configurare il filtro ClienteLoginFilter.
 */
@Configuration
public class ClienteLoginFilterConfiguration {

  @Autowired
  private ClienteLoginFilter clienteLoginFilter;

  /**
   * Creazione di un FilterRegistrationBean con gli URL accessibili solo dal Cliente.
   *
   * @return FilterRegistrationBean ClienteLoginFilter
   */
  @Bean
  public FilterRegistrationBean<ClienteLoginFilter> filterRegistrationBeanClienteLogin() {
    FilterRegistrationBean<ClienteLoginFilter> registrationBean
            = new FilterRegistrationBean<>();

    registrationBean.setFilter(clienteLoginFilter);
    registrationBean.addUrlPatterns(
            "/modificaProfilo",
            "/visualizzazioneProfiloPersonale",
            "/visualizzazionePrenotazioniPersonali",
            "/cancellazioneAccount",
            "/effettuaPrenotazione",
            "/immagineanimale"   // 🔒 nuova protezione qui
    );

    return registrationBean;
  }
}
