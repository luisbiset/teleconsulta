package br.gov.ba.sesab.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.primefaces.PrimeFaces;

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
public class UsuarioController extends AbstractController implements Serializable {

    private static final long serialVersionUID = 1L;

    private UsuarioEntity usuario;
    private List<UsuarioEntity> usuarios;
    private String senhaAtual;
    private String novaSenha;
    private String confirmarSenha;
    private List<UsuarioEntity> usuariosFiltrados;
    private String filtroUsuario;

    
    @Inject
    private UsuarioService usuarioService;

    @PostConstruct
    public void init() {
        usuario = new UsuarioEntity();
        listar();
       
    }
    
    public void novo() {
        this.usuario = new UsuarioEntity();
        this.usuario.setPerfil(null);
        this.novaSenha = null;
        this.confirmarSenha = null;
        this.senhaAtual = null;
        
    }


    public void salvar() {
        try {
        	
        	 if (usuario.getId() == null) {
        	        usuario.setSenha(novaSenha);
        	    } else {
        	        if (novaSenha != null && !novaSenha.isBlank()) {
        	            usuario.setSenha(novaSenha);
        	        } else {
        	            usuario.setSenha(null); 
        	        }
        	    }
        	 
        	 UsuarioEntity existente = usuarioService.buscarUsuarioPorCpf(usuario.getCpf());

        	    if (existente != null && !existente.getId().equals(usuario.getId())) {
        	        throw new IllegalArgumentException("Já existe um usuário cadastrado com este CPF.");
        	    }
            usuarioService.salvar(usuario);
            PrimeFaces.current().ajax().addCallbackParam("sucesso", true);
            novaSenha =null;

            addMensagem("Usuário salvo com sucesso!");
            usuario = new UsuarioEntity();
            listar();

        } catch (IllegalArgumentException e) {
            addMensagemErro(e.getMessage()); 
        } catch (Exception e) {
            addMensagemErro("Erro inesperado ao salvar o usuário.");
            e.printStackTrace();
        }
    }



    public void excluir(Long id) {
        try {

            usuarioService.excluir(id);
            listar();

            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(
                    FacesMessage.SEVERITY_INFO,
                    "Usuário excluído com sucesso!",
                    null
                )
            );

        } catch (RuntimeException e) {  

            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(
                    FacesMessage.SEVERITY_WARN,
                    e.getMessage(),   
                    null
                )
            );

        } catch (Exception e) {

            e.printStackTrace();

            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Erro inesperado ao excluir usuário.",
                    null
                )
            );
        }
    }

    public void editar(UsuarioEntity u) {
        this.usuario = new UsuarioEntity(u);  
        this.novaSenha = null;
        this.confirmarSenha = null;
    }



    public void listar() {
        usuarios = usuarioService.listarTodos();
        usuariosFiltrados = new ArrayList<>(usuarios);
        
    }
    
    public void filtrar() {

        if (filtroUsuario == null || filtroUsuario.trim().isEmpty()) {
            usuariosFiltrados = new ArrayList<>(usuarios);
            return;
        }

        String filtro = filtroUsuario.toLowerCase();

        usuariosFiltrados = usuarios.stream()
            .filter(u ->
                (u.getNome() != null && u.getNome().toLowerCase().contains(filtro)) ||
                (u.getCpf() != null && u.getCpf().contains(filtro))
            )
            .toList();
    }
    
    public void limparFiltro() {
        filtroUsuario = null;
        usuariosFiltrados = new ArrayList<>(usuarios);
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
    
   

	public List<UsuarioEntity> getUsuariosFiltrados() {
		return usuariosFiltrados;
	}

	public void setUsuariosFiltrados(List<UsuarioEntity> usuariosFiltrados) {
		this.usuariosFiltrados = usuariosFiltrados;
	}

	public String getFiltroUsuario() {
		return filtroUsuario;
	}

	public void setFiltroUsuario(String filtroUsuario) {
		this.filtroUsuario = filtroUsuario;
	}

	public void setUsuarios(List<UsuarioEntity> usuarios) {
		this.usuarios = usuarios;
	}
    
    


}
