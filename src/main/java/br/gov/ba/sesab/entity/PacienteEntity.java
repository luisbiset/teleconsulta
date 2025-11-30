package br.gov.ba.sesab.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "paciente")
public class PacienteEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private UsuarioEntity usuario;


    @NotBlank(message = "RG é obrigatório")
    @Pattern(regexp = "\\d{10}", message = "RG deve conter 10 dígitos")
    @Column(nullable = false, length = 10)
    private String rg;

    @NotBlank(message = "Telefone é obrigatório")
    @Size(min = 11, max = 11, message = "Telefone deve conter 11 dígitos")
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

    private String nomeSocial;

    @NotBlank(message = "CNS é obrigatório")
    @Pattern(regexp = "\\d{15}", message = "CNS deve conter 15 dígitos")
    @Column(nullable = false, unique = true, length = 15)
    private String cns;


    public Long getId() {
        return id;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
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
        if (telefone != null) {
            this.telefone = telefone.replaceAll("\\D", "");
        } else {
            this.telefone = null;
        }
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

    public String getNomeMae() {
        return nomeMae;
    }

    public void setNomeMae(String nomeMae) {
        this.nomeMae = nomeMae;
    }

    public String getNomePai() {
        return nomePai;
    }

    public void setNomePai(String nomePai) {
        this.nomePai = nomePai;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNomeSocial() {
        return nomeSocial;
    }

    public void setNomeSocial(String nomeSocial) {
        this.nomeSocial = nomeSocial;
    }

    public String getCns() {
        return cns;
    }

    public void setCns(String cns) {
        this.cns = cns;
    }
}
