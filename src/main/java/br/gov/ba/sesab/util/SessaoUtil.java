package br.gov.ba.sesab.util;

import br.gov.ba.sesab.entity.UsuarioEntity;
import jakarta.faces.context.FacesContext;

public class SessaoUtil {

    public static UsuarioEntity getUsuarioLogado() {
        return (UsuarioEntity) FacesContext.getCurrentInstance()
            .getExternalContext()
            .getSessionMap()
            .get("usuarioLogado");
    }

    public static boolean isAdmin() {
        UsuarioEntity u = getUsuarioLogado();
        return u != null && u.getPerfil().name().equals("ADMIN");
    }
}
