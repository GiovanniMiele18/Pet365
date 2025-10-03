package it.unisa.justTraditions.applicationLogic.gestioneProfiliControl.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class AggiuntaDocumentoForm {

    @NotNull(message = "Animale non valido")
    private Long animaleId;

    @NotBlank(message = "Nome documento obbligatorio")
    private String nome;

    private String descrizione;

    private String tipo;

    @NotNull(message = "Devi caricare un file")
    private MultipartFile file;

    // Getter & Setter
    public Long getAnimaleId() { return animaleId; }
    public void setAnimaleId(Long animaleId) { this.animaleId = animaleId; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public MultipartFile getFile() { return file; }
    public void setFile(MultipartFile file) { this.file = file; }
}