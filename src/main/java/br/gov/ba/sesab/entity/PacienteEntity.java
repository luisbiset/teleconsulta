package br.gov.ba.sesab.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "paciente")
public class PacienteEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Nome é obrigatório")
	@Size(min = 3, max = 150, message = "Nome deve ter entre 3 e 150 caracteres")
	@Pattern(regexp = "^[A-Za-zÀ-ÿ]+( [A-Za-zÀ-ÿ]+)*$", message = "Nome não pode conter números, símbolos ou espaços duplicados")
	@Column(nullable = false, length = 150)
	private String nome;

	@NotBlank(message = "CPF é obrigatório")
	@Column(nullable = false, unique = true, length = 11)
	private String cpf;

	@NotBlank(message = "RG é obrigatório")
	@Pattern(regexp = "\\d{10}", message = "RG deve conter 10 dígitos")
	@Column(nullable = false, length = 20)
	private String rg;

	@NotBlank(message = "Telefone é obrigatório")
	@Column(nullable = false, length = 11)
	private String telefone;

	@NotNull(message = "Data de nascimento é obrigatória")
	@Temporal(TemporalType.DATE)
	@Column(name = "data_nascimento", nullable = false)
	@PastOrPresent
	private Date dataNascimento;

	@NotBlank(message = "Sexo é obrigatório")
	@Column(nullable = false, length = 10)
	private String sexo;

	@NotBlank(message = "Nome da mãe é obrigatório")
	@Column(nullable = false, length = 150)
	private String nomeMae;

	@NotBlank(message = "Nome do pai é obrigatório")
	@Column(nullable = false, length = 150)
	private String nomePai;

	@NotBlank(message = "Endereço é obrigatório")
	@Column(nullable = false, length = 200)
	private String endereco;

	// ✅ ÚNICO OPCIONAL
	@Email(message = "Email inválido")
	@Column(nullable = true, length = 150)
	private String email;

	// OPCIONAIS / COMPLEMENTARES
	private String nomeSocial;

	@NotBlank(message = "CNS é obrigatório")
	@Pattern(regexp = "\\d{15}", message = "CNS deve conter 15 dígitos")
	@Column(nullable = false, unique = true, length = 15)
	private String cns;

	// ======================
	// GETTERS / SETTERS
	// ======================

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
		if (nome != null) {
			this.nome = nome.trim().replaceAll("\\s+", " ");
		} else {
			this.nome = null;
		}
	}

	public String getNomeMae() {
		return nomeMae;
	}

	public void setNomeMae(String nomeMae) {
		if (nomeMae != null) {
			this.nomeMae = nomeMae.trim().replaceAll("\\s+", " ");
		} else {
			this.nomeMae = null;
		}
	}

	public String getNomePai() {
		return nomePai;
	}

	public void setNomePai(String nomePai) {
		if (nomePai != null) {
			this.nomePai = nomePai.trim().replaceAll("\\s+", " ");
		} else {
			this.nomePai = null;
		}
	}

	public String getNomeSocial() {
		return nomeSocial;
	}

	public void setNomeSocial(String nomeSocial) {
		if (nomeSocial != null) {
			this.nomeSocial = nomeSocial.trim().replaceAll("\\s+", " ");
		} else {
			this.nomeSocial = null;
		}
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		if (endereco != null) {
			this.endereco = endereco.trim().replaceAll("\\s+", " ");
		} else {
			this.endereco = null;
		}
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCns() {
		return cns;
	}

	public void setCns(String cns) {
		this.cns = cns;
	}
}
