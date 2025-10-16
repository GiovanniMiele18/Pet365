package it.unisa.justTraditions.applicationLogic.shopControl.form;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * Form di validazione per l'aggiunta o modifica di un prodotto nello shop.
 */
public class AggiuntaProdottoForm {
    
    private Long id;

     private List<MultipartFile> foto;

    @NotBlank(message = "Il nome del prodotto non può essere vuoto.")
    @Size(max = 100, message = "Il nome del prodotto è troppo lungo (max 100 caratteri).")
    private String nome;

    @NotBlank(message = "La descrizione non può essere vuota.")
    @Size(max = 2000, message = "La descrizione è troppo lunga (max 2000 caratteri).")
    private String descrizione;

    @NotNull(message = "Il prezzo è obbligatorio.")
    @DecimalMin(value = "0.01", message = "Il prezzo deve essere maggiore di 0.")
    @Digits(integer = 8, fraction = 2, message = "Formato prezzo non valido (es: 12.99).")
    private BigDecimal prezzo;

    @NotNull(message = "La quantità è obbligatoria.")
    @Min(value = 0, message = "La quantità non può essere negativa.")
    private Integer quantitaDisponibile;

    @NotBlank(message = "La categoria non può essere vuota.")
    @Pattern(regexp = "^[A-Za-zÀ-ÿ\\s]+$", message = "La categoria può contenere solo lettere e spazi.")
    @Size(max = 50, message = "Categoria troppo lunga (max 50 caratteri).")
    private String categoria;

    public AggiuntaProdottoForm() {}

    public AggiuntaProdottoForm(Long id, String nome, String descrizione, BigDecimal prezzo, Integer quantitaDisponibile, String categoria) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
        this.quantitaDisponibile = quantitaDisponibile;
        this.categoria = categoria;
    }

    // Getters e Setters
      

    public List<MultipartFile> getFoto() { return foto; }
    public void setFoto(List<MultipartFile> foto) { this.foto = foto; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
    public BigDecimal getPrezzo() { return prezzo; }
    public void setPrezzo(BigDecimal prezzo) { this.prezzo = prezzo; }
    public Integer getQuantitaDisponibile() { return quantitaDisponibile; }
    public void setQuantitaDisponibile(Integer quantitaDisponibile) { this.quantitaDisponibile = quantitaDisponibile; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
}
