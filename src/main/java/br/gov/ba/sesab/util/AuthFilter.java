package br.gov.ba.sesab.util;

import java.io.IOException;

import br.gov.ba.sesab.entity.UsuarioEntity;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req  = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        res.setHeader("Pragma", "no-cache");
        res.setDateHeader("Expires", 0);

        String uri = req.getRequestURI();
        String ctx = req.getContextPath();

        String path = uri.substring(ctx.length());

        if (path.startsWith("/login.xhtml")
                || path.startsWith("/jakarta.faces.resource")
                || path.startsWith("/resources")
                || path.endsWith(".css")
                || path.endsWith(".js")
                || path.endsWith(".png")
                || path.endsWith(".jpg")
                || path.contains("keepSessionAlive")
                || path.endsWith(".woff2")) {

            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);

        UsuarioEntity usuario =
            (session != null)
                ? (UsuarioEntity) session.getAttribute("usuarioLogado")
                : null;

        if (usuario == null) {
            System.out.println(">>> AUTHFILTER: USUARIO NULO, REDIRECIONANDO");
            res.sendRedirect(ctx + "/login.xhtml");
            return;
        }


        chain.doFilter(request, response);
    }
}


