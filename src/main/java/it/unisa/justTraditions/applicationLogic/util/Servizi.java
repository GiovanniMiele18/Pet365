package it.unisa.justTraditions.applicationLogic.util;

import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Classe per la creazione di un componente che contiene una lista con tutti i servizi offerti
 */
@Component
public class Servizi {

    private final List<String> servizi = List.of(
            "PETSITTER",
            "VETERINARI_H24",
            "SHOP",
            "DOCCE_TOELETTATURA",
            "ADDESTRAMENTO",
            "PENSIONI",
            "COMMEMORAZIONI_E_SERVIZI_FUNEBRI",
            "SERVIZI_DI_ACCOPPIAMENTO",
            "COMPRA_O_ADOTTA_UN_CUCCIOLO",
            "SFILATE_E_EVENTI",
            "TRASPORTO_SICURO_PER_ANIMALI",
            "ASSICURAZIONE_ANIMALI",
            "PET_PHOTOGRAPHY",
            "TERAPIE_E_BENESSERE",
            "NUTRIZIONE_PERSONALIZZATA"
    );

    public List<String> getServizi() {
        return servizi;
    }
}
