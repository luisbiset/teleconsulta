package br.gov.ba.sesab.controller;

import java.io.Serializable;

import br.gov.ba.sesab.entity.UsuarioEntity;
import br.gov.ba.sesab.service.UsuarioService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class SistemaController extends AbstractController implements Serializable {

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private LoginController loginController;

	private UsuarioEntity usuario;

	private String senhaAtual;
	private String novaSenha;
	private String confirmarSenha;

	@PostConstruct
	public void init() {
		usuario = loginController.getUsuarioLogado();
	}

	public void alterarSenha() {

		if (senhaAtual == null || novaSenha == null || confirmarSenha == null) {
			msgErro("Todos os campos são obrigatórios.");
			return;
		}

		if (!novaSenha.equals(confirmarSenha)) {
			msgErro("Nova senha e confirmação não conferem.");
			return;
		}

		try {
			usuarioService.alterarSenha(usuario, senhaAtual, novaSenha);

			msgSucesso("Senha alterada com sucesso!");

			senhaAtual = null;
			novaSenha = null;
			confirmarSenha = null;

		} catch (Exception e) {
			msgErro(e.getMessage());
		}
	}

	private void msgErro(String msg) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
	}

	private void msgSucesso(String msg) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
	}

	// GETTERS
	public UsuarioEntity getUsuario() {
		return usuario;
	}

	public String getSenhaAtual() {
		return senhaAtual;
	}

	public void setSenhaAtual(String senhaAtual) {
		this.senhaAtual = senhaAtual;
	}

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

	public String getConfirmarSenha() {
		return confirmarSenha;
	}

	public void setConfirmarSenha(String confirmarSenha) {
		this.confirmarSenha = confirmarSenha;
	}
}
