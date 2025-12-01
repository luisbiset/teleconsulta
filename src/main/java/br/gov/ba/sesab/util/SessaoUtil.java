package br.gov.ba.sesab.util;

import br.gov.ba.sesab.entity.UsuarioEntity;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessaoUtil {

    public static UsuarioEntity getUsuarioLogado() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context == null) return null;

        HttpServletRequest request = (HttpServletRequest)
                context.getExternalContext().getRequest();

        HttpSession session = request.getSession(false);

        if (session == null) return null;

        return (UsuarioEntity) session.getAttribute("usuarioLogado");
    }

    public static boolean isAdmin() {
        UsuarioEntity u = getUsuarioLogado();
        return u != null && u.getPerfil().name().equals("ADMIN");
    }
}
