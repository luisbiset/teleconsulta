package br.gov.ba.sesab.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "unidade_saude")
public class UnidadeSaudeEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Nome é obrigatório")
	@Size(min = 3, max = 150, message = "Nome deve ter entre 3 e 150 caracteres")
	@Pattern(regexp = "^[A-Za-zÀ-ÿ]+( [A-Za-zÀ-ÿ]+)*$", message = "Nome não pode conter números, símbolos ou espaços duplicados")
	@Column(nullable = false, length = 150)
	private String nome;

	@NotBlank(message = "Razão Social é obrigatória")
	@Size(min = 3, max = 200, message = "Razão Social deve ter entre 3 e 200 caracteres")
	@Column(name = "razao_social", nullable = false, length = 200)
	private String razaoSocial;

	@NotBlank(message = "Sigla é obrigatória")
	@Size(min = 2, max = 10, message = "Sigla deve ter entre 2 e 10 caracteres")
	@Pattern(regexp = "^[A-Za-z]+$", message = "Sigla deve conter apenas letras")
	@Column(nullable = false, length = 10)
	private String sigla;

	@OneToMany(mappedBy = "unidade", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SalaEntity> salas = new ArrayList<>();

	@NotBlank(message = "CNPJ é obrigatório")
	@Pattern(regexp = "\\d{14}", message = "CNPJ deve conter exatamente 14 números (apenas números)")
	@Column(nullable = false, unique = true, length = 14)
	private String cnpj;

	@NotBlank(message = "CNES é obrigatório")
	@Pattern(regexp = "\\d{7}", message = "CNES deve conter exatamente 7 números")
	@Column(nullable = false, unique = true, length = 7)
	private String cnes;

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

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getCnes() {
		return cnes;
	}

	public void setCnes(String cnes) {
		this.cnes = cnes;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		UnidadeSaudeEntity other = (UnidadeSaudeEntity) obj;
		return id != null && id.equals(other.id);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}

	@Override
	public String toString() {
		return "UnidadeSaudeEntity [id=" + id + ", nome=" + nome + ", razaoSocial=" + razaoSocial + ", sigla=" + sigla
				+ ", cnpj=" + cnpj + ", cnes=" + cnes + "]";
	}

}
