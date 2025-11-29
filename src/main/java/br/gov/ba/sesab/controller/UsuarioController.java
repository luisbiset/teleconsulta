package br.gov.ba.sesab.controller;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import br.gov.ba.sesab.entity.UsuarioEntity;
import br.gov.ba.sesab.enums.PerfilUsuario;
import br.gov.ba.sesab.service.UsuarioService;
import br.gov.ba.sesab.util.SessaoUtil;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class UsuarioController implements Serializable {

    private static final long serialVersionUID = 1L;

    private UsuarioEntity usuario;
    private List<UsuarioEntity> usuarios;
    private String senhaAtual;
    private String novaSenha;
    private String confirmarSenha;

    
    @Inject
    private UsuarioService usuarioService;

    @PostConstruct
    public void init() {
    	System.out.println("TIMEZONE JVM: " + java.util.TimeZone.getDefault());
        usuario = new UsuarioEntity();
        listar();
    }
    
    public void novo() {
        this.usuario = new UsuarioEntity();
    }

    public void salvar() {
        try {

        	if (usuario.getId() == null) {

        	    if (usuario.getSenha() == null || usuario.getSenha().isBlank()) {
        	        addMensagemErro("Senha é obrigatória para novo usuário.");
        	        return;
        	    }

        	    usuario.setDataCadastro(new Date());
        	    

        	    if (usuario.getPerfil() == null) {
        	        usuario.setPerfil(PerfilUsuario.OPERADOR);
        	    }
        	}
            else {

                UsuarioEntity usuarioBanco = usuarioService.buscarPorId(usuario.getId());

                // ✅ SE NÃO INFORMAR NOVA SENHA, MANTÉM A ANTIGA
                if (usuario.getSenha() == null || usuario.getSenha().isBlank()) {
                    usuario.setSenha(usuarioBanco.getSenha());
                }

                // ✅ SE NÃO ALTERAR O PERFIL NA EDIÇÃO, MANTÉM O ANTIGO
                if (usuario.getPerfil() == null) {
                    usuario.setPerfil(usuarioBanco.getPerfil());
                }
            }

            usuarioService.salvar(usuario);

            addMensagem("Usuário salvo com sucesso!");
            usuario = new UsuarioEntity();
            listar();

        } catch (Exception e) {
            addMensagemErro("Erro ao salvar o usuário.");
            e.printStackTrace();
        }
    }


    public void excluir(Long id) {
        try {
            usuarioService.excluir(id);
            addMensagem("Usuário excluído com sucesso!");
            listar();
        } catch (Exception e) {
            addMensagemErro("Erro ao excluir o usuário.");
            e.printStackTrace();
        }
    }

    public void editar(UsuarioEntity u) {
        this.usuario = u;
    }

    public void listar() {
        usuarios = usuarioService.listarTodos();
    }

    private void addMensagem(String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
    }

    private void addMensagemErro(String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    public List<UsuarioEntity> getUsuarios() {
        return usuarios;
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
    
    public boolean isAdmin() {
        return SessaoUtil.isAdmin();
    }


}
