package br.gov.ba.sesab.service;

import java.util.List;

import br.gov.ba.sesab.controller.LoginController;
import br.gov.ba.sesab.entity.UnidadeSaudeEntity;
import br.gov.ba.sesab.entity.UsuarioEntity;
import br.gov.ba.sesab.enums.PerfilUsuario;
import br.gov.ba.sesab.repository.ReservaRepository;
import br.gov.ba.sesab.repository.UnidadeSaudeRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UnidadeSaudeService extends AbstractService {

	@Inject
	private UnidadeSaudeRepository unidadeRepository;

	@Inject
	private ReservaRepository reservaRepository;

	private UsuarioEntity getLogin() {
		  FacesContext context = FacesContext.getCurrentInstance();

		    if (context == null)
		        return null;

		    HttpServletRequest request = (HttpServletRequest)
		            context.getExternalContext().getRequest();

		    HttpSession session = request.getSession(false);

		    if (session == null)
		        return null;

		    return (UsuarioEntity) session.getAttribute("usuarioLogado");
		}

	public void salvar(UnidadeSaudeEntity unidade) {

		UsuarioEntity usuario = getLogin();

		if (usuario == null || usuario.getPerfil() != PerfilUsuario.ADMIN) {
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

	@Transactional
	public void excluir(Long id) {

	    validarAdmin(); 

	    if (reservaRepository.existeReservaPorUnidade(id)) {
	        throw new RuntimeException(
	            "Não é possível excluir a unidade pois existem reservas vinculadas."
	        );
	    }

	    UnidadeSaudeEntity unidade = unidadeRepository.findById(id);

	    if (unidade == null) {
	        throw new RuntimeException("Unidade não encontrada.");
	    }

	    unidadeRepository.excluir(id);
	}


	public List<UnidadeSaudeEntity> listarTodos() {
		return unidadeRepository.listarTodos();
	}

	
}
