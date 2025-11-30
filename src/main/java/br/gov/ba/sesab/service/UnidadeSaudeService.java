package br.gov.ba.sesab.service;

import java.util.List;

import br.gov.ba.sesab.controller.LoginController;
import br.gov.ba.sesab.entity.UnidadeSaudeEntity;
import br.gov.ba.sesab.enums.PerfilUsuario;
import br.gov.ba.sesab.repository.UnidadeSaudeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;

@ApplicationScoped
public class UnidadeSaudeService {

    @Inject
    private UnidadeSaudeRepository unidadeRepository;

    private LoginController getLogin() {
        return (LoginController) FacesContext
                .getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("loginController");
    }

    public void salvar(UnidadeSaudeEntity unidade) {

        LoginController login = getLogin();

        if (login == null || 
            login.getUsuarioLogado().getPerfil() != PerfilUsuario.ADMIN) {
            throw new RuntimeException("Apenas ADMIN pode cadastrar ou editar unidades.");
        }

        try {
            unidadeRepository.salvar(unidade);

        } catch (Exception e) {

            String erro = e.getMessage().toLowerCase();

            if (erro.contains("unidade_saude.cnpj")) {
                throw new RuntimeException("Já existe uma unidade cadastrada com este CNPJ.");
            }

            if (erro.contains("unidade_saude.cnes")) {
                throw new RuntimeException("Já existe uma unidade cadastrada com este CNES.");
            }

            if (erro.contains("unique")) {
                throw new RuntimeException("Registro duplicado não permitido.");
            }

            throw new RuntimeException("Erro ao salvar unidade de saúde.");
        }
    }


    public void excluir(Long id) {

        LoginController login = getLogin();

        if (login == null || 
            login.getUsuarioLogado().getPerfil() != PerfilUsuario.ADMIN) {
            throw new RuntimeException("Apenas ADMIN pode excluir unidades.");
        }

        unidadeRepository.excluir(id);
    }

    public List<UnidadeSaudeEntity> listarTodos() {
        return unidadeRepository.listarTodos();
    }

    public UnidadeSaudeEntity buscarPorId(Long id) {
        return unidadeRepository.buscarPorId(id);
    }
}
