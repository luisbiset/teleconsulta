package br.gov.ba.sesab.service;

import br.gov.ba.sesab.entity.UsuarioEntity;
import br.gov.ba.sesab.enums.PerfilUsuario;
import br.gov.ba.sesab.util.SessaoUtil;

public abstract class AbstractService {

    protected void validarAdmin() {
        UsuarioEntity logado = SessaoUtil.getUsuarioLogado();

        if (logado == null || logado.getPerfil() != PerfilUsuario.ADMIN) {
            throw new RuntimeException("Ação permitida apenas para ADMIN.");
        }
    }
}
