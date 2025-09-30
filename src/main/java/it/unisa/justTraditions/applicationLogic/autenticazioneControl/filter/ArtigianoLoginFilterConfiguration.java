package it.unisa.justTraditions.applicationLogic.autenticazioneControl.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Implementa la creazione di un FilterRegistrationBean
 * per configurare il filtro ArtigianoLoginFilter.
 */
@Configuration
public class ArtigianoLoginFilterConfiguration {

  @Autowired
  private ArtigianoLoginFilter artigianoLoginFilter;

  /**
   * Creazione di un FilterRegistrationBean con gli URL accessibili solo dal Artigiano.
   *
   * @return FilterRegistrationBean ArtigianoLoginFilter
   */
  @Bean
  public FilterRegistrationBean<ArtigianoLoginFilter> filterRegistrationBeanArtigianoLogin() {
    FilterRegistrationBean<ArtigianoLoginFilter> registrationBean
        = new FilterRegistrationBean<>();

    registrationBean.setFilter(artigianoLoginFilter);
    registrationBean.addUrlPatterns("/sottomissioneAnnuncio", "/rimozioneAnnuncio",
        "/modificaAnnuncio", "/visualizzazioneListaAnnunciSottomessi",
        "/visualizzazionePrenotazioniAnnuncio");

    return registrationBean;
  }
}
