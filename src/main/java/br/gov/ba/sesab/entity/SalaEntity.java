package br.gov.ba.sesab.entity;

import java.io.Serializable;
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
@Table(name = "sala")
public class SalaEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nome", nullable = false)
    private String nome;
    
    @Column(name = "capacidade", nullable = false)
    private Integer capacidade;


    @ManyToOne
    @JoinColumn(name = "unidade_id")
    private UnidadeSaudeEntity unidade;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

    public UnidadeSaudeEntity getUnidade() {
        return unidade;
    }

    public void setUnidade(UnidadeSaudeEntity unidade) {
        this.unidade = unidade;
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
		SalaEntity other = (SalaEntity) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "SalaEntity [id=" + id + ", nome=" + nome + ", capacidade=" + capacidade + ", unidade=" + unidade + "]";
	}
    
    
    
    
}
