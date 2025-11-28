package br.gov.ba.sesab.controller;

import java.io.Serializable;

import br.gov.ba.sesab.entity.UsuarioEntity;
import br.gov.ba.sesab.enums.PerfilUsuario;
import br.gov.ba.sesab.service.UsuarioService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@SessionScoped
public class LoginController implements Serializable {

    private String cpf;
    private String senha;

    private UsuarioEntity usuarioLogado;

    @Inject
    private UsuarioService usuarioService;

    // ============================
    // AÇÃO DE LOGIN
    // ============================
    public String entrar() {

        String cpfLimpo = cpf.replace(".", "").replace("-", "").trim();
        String senhaLimpa = senha.trim();

        UsuarioEntity usuario = usuarioService.autenticar(cpfLimpo, senhaLimpa);

        if (usuario != null) {
            this.usuarioLogado = usuario;

            // ✅ MANTÉM COMPATIBILIDADE COM O QUE JÁ EXISTE
            FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .put("loginController", this);

            // ✅ TAMBÉM SALVA O USUÁRIO DIRETAMENTE (PARA SERVICE, ETC.)
            FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .put("usuarioLogado", usuario);

            return "/menu/menu.xhtml?faces-redirect=true";
        }

        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR,
                "CPF ou senha inválidos", null));

        return null;
    }

    public boolean isAdmin() {
        return usuarioLogado != null 
            && usuarioLogado.getPerfil() == PerfilUsuario.ADMIN;
    }

    // ============================
    // LOGOUT
    // ============================
    public String sair() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml?faces-redirect=true";
    }

    // ============================
    // GETTERS E SETTERS
    // ============================
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public UsuarioEntity getUsuarioLogado() {
        return usuarioLogado;
    }

    public void setUsuarioLogado(UsuarioEntity usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }

    public boolean isLogado() {
        return usuarioLogado != null;
    }
}
