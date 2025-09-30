package it.unisa.justTraditions.applicationLogic.autenticazioneControl.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Implementa la creazione di un FilterRegistrationBean
 * per configurare il filtro AmministratoreLoginFilter.
 */
@Configuration
public class AmministratoreLoginFilterConfiguration {

  @Autowired
  private AmministratoreLoginFilter amministratoreLoginFilter;

  /**
   * Creazione di un FilterRegistrationBean con gli URL accessibili solo dal Amministratore.
   *
   * @return FilterRegistrationBean AmministratoreLoginFilter
   */
  @Bean
  public FilterRegistrationBean<AmministratoreLoginFilter> filterRegistrationBeanAmministratore() {
    FilterRegistrationBean<AmministratoreLoginFilter> registrationBean
        = new FilterRegistrationBean<>();

    registrationBean.setFilter(amministratoreLoginFilter);
    registrationBean.addUrlPatterns("/modificaStatoAnnuncio", "/rimozioneAmministratore",
        "/visualizzazioneAmministratori", "/visualizzazioneListaAnnunci", "/homeAmministratore",
        "/aggiuntaAmministratore");

    return registrationBean;
  }
}