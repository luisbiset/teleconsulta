package br.gov.ba.sesab.controller;

import java.io.IOException;
import java.io.Serializable;

import br.gov.ba.sesab.entity.UsuarioEntity;
import br.gov.ba.sesab.enums.PerfilUsuario;
import br.gov.ba.sesab.service.UsuarioService;
import br.gov.ba.sesab.util.SessaoUtil;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Named
@SessionScoped
public class LoginController extends AbstractController implements Serializable {

    private static final long serialVersionUID = 1L;
	private String cpf;
    private String senha;

    private UsuarioEntity usuarioLogado;

    @Inject
    private UsuarioService usuarioService;

    public void entrar() {

        String cpfLimpo = cpf.replace(".", "").replace("-", "").trim();
        String senhaLimpa = senha.trim();

        UsuarioEntity usuario = usuarioService.autenticar(cpfLimpo, senhaLimpa);

        if (usuario != null) {
        	
        	  if (usuario.getPerfil() == PerfilUsuario.PACIENTE) {
                  FacesContext.getCurrentInstance().addMessage(null,
                      new FacesMessage(FacesMessage.SEVERITY_ERROR,
                              "Acesso negado: pacientes não possuem acesso ao sistema.", null));
                  return;
              }
            this.usuarioLogado = usuario;

            HttpServletRequest request = (HttpServletRequest)
                    FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .getRequest();

            HttpSession session = request.getSession(true);
            session.setAttribute("usuarioLogado", usuario);
            

            try {

                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect(request.getContextPath() + "/menu/menu.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }

            return;
        }

        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "CPF ou senha inválidos", null));
    }

    public void sair() {

        FacesContext context = FacesContext.getCurrentInstance();

        HttpServletRequest request = (HttpServletRequest)
                context.getExternalContext().getRequest();

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        this.usuarioLogado = null;
        this.cpf = null;
        this.senha = null;

        try {
            context.getExternalContext()
                   .redirect(request.getContextPath() + "/login.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    public boolean isLogado() {
        return SessaoUtil.getUsuarioLogado() != null;
    }


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

	public UsuarioService getUsuarioService() {
		return usuarioService;
	}

	public void setUsuarioService(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
    
    

}

