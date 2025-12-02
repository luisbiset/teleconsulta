package br.gov.ba.sesab.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "reserva")
public class ReservaEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario_solicitante", nullable = false)
    private UsuarioEntity usuarioSolicitante;

    @ManyToOne
    @JoinColumn(name = "id_sala", nullable = false)
    private SalaEntity sala;

    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDateTime dataFim;

    // =====================
    // GETTERS E SETTERS
    // =====================

    
    
    
    public UsuarioEntity getUsuarioSolicitante() {
        return usuarioSolicitante;
    }

    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setUsuarioSolicitante(UsuarioEntity usuarioSolicitante) {
        this.usuarioSolicitante = usuarioSolicitante;
    }

    public SalaEntity getSala() {
        return sala;
    }

    public void setSala(SalaEntity sala) {
        this.sala = sala;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReservaEntity other = (ReservaEntity) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "ReservaEntity [id=" + id + ", usuarioSolicitante=" + usuarioSolicitante + ", sala=" + sala
				+ ", dataInicio=" + dataInicio + ", dataFim=" + dataFim + "]";
	}
    
    
}

