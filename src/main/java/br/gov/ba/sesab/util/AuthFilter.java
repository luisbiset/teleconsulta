package br.gov.ba.sesab.util;

import java.io.IOException;

import br.gov.ba.sesab.controller.LoginController;
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

        String uri = req.getRequestURI();

        // ✅ LIBERA LOGIN E RECURSOS
        if (uri.endsWith("login.xhtml")
                || uri.contains("jakarta.faces.resource")
                || uri.contains("/resources/")
                || uri.endsWith(".css")
                || uri.endsWith(".js")) {

            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);

        LoginController login =
            (session != null) ? (LoginController) session.getAttribute("loginController") : null;

        // ✅ BLOQUEIA SE NÃO ESTIVER LOGADO
        if (login == null || !login.isLogado()) {
            res.sendRedirect(req.getContextPath() + "/login.xhtml");
            return;
        }

        // 🔒 BLOQUEIO ESPECÍFICO: UNIDADE SÓ PARA ADMIN
        if (uri.contains("/unidade/") && !login.isAdmin()) {
            res.sendRedirect(req.getContextPath() + "/menu/menu.xhtml");
            return;
        }

        // ✅ SEGUE O FLUXO NORMAL
        chain.doFilter(request, response);
    }
}
