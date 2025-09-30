package it.unisa.justTraditions.applicationLogic.gestioneProfiliControl.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class AggiuntaAnimaleForm {

    @NotBlank(message = "Nome vuoto")
    @Size(max = 50, message = "Nome troppo lungo")
    private String nome;

    @NotBlank(message = "Specie vuota")
    @Size(max = 30, message = "Specie troppo lunga")
    private String specie;

    private String razza;

    @Min(value = 0, message = "Età non valida")
    private int eta;

    private String sesso;

    @Min(value = 0, message = "Peso non valido")
    private double peso;

    private String colore;

    private String note;

    private List<MultipartFile> foto;

    // Getter & Setter
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSpecie() { return specie; }
    public void setSpecie(String specie) { this.specie = specie; }

    public String getRazza() { return razza; }
    public void setRazza(String razza) { this.razza = razza; }

    public int getEta() { return eta; }
    public void setEta(int eta) { this.eta = eta; }

    public String getSesso() { return sesso; }
    public void setSesso(String sesso) { this.sesso = sesso; }

    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }

    public String getColore() { return colore; }
    public void setColore(String colore) { this.colore = colore; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public List<MultipartFile> getFoto() { return foto; }
    public void setFoto(List<MultipartFile> foto) { this.foto = foto; }
}
