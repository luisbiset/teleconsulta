package br.gov.ba.sesab.controller;

import br.gov.ba.sesab.entity.UsuarioEntity;
import br.gov.ba.sesab.enums.PerfilUsuario;
import br.gov.ba.sesab.util.SessaoUtil;

public abstract class AbstractController {
	
	  public boolean isAdmin() {
	        UsuarioEntity u = SessaoUtil.getUsuarioLogado();
	        return u != null && u.getPerfil() == PerfilUsuario.ADMIN;
	    }

}
