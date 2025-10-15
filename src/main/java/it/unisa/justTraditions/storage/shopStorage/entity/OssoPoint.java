package it.unisa.justTraditions.storage.shopStorage.entity;

import it.unisa.justTraditions.storage.gestioneProfiliStorage.entity.Cliente;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "osso_points")
public class OssoPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "cliente_id", referencedColumnName = "id", nullable = false, unique = true)
    private Cliente cliente;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valore = BigDecimal.ZERO;

    public OssoPoint() {}

    public OssoPoint(Cliente cliente, BigDecimal valore) {
        this.cliente = cliente;
        this.valore = valore;
    }

    public Long getId() {
        return id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public BigDecimal getValore() {
        return valore;
    }

    public void setValore(BigDecimal valore) {
        this.valore = valore;
    }

    public void aggiungi(BigDecimal punti) {
        this.valore = this.valore.add(punti);
    }
}
