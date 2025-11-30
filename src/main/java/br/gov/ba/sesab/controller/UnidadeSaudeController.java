package br.gov.ba.sesab.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.gov.ba.sesab.entity.UnidadeSaudeEntity;
import br.gov.ba.sesab.service.UnidadeSaudeService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class UnidadeSaudeController implements Serializable {

    private static final long serialVersionUID = 1L;

    private UnidadeSaudeEntity unidade;
    private List<UnidadeSaudeEntity> unidades;
    private List<UnidadeSaudeEntity> unidadesFiltradas;
    private String filtroUnidade;


    @Inject
    private UnidadeSaudeService unidadeSaudeService;

    @PostConstruct
    public void init() {
        novo();
        listar();
    }

    public void novo() {
        unidade = new UnidadeSaudeEntity();
    }
    public void salvar() {
        try {
        	unidadeSaudeService.salvar(unidade);

            addMensagem("Unidade salva com sucesso!");
            novo();
            listar();

        } catch (RuntimeException e) {
            addMensagemErro(e.getMessage()); 
        } catch (Exception e) {
            addMensagemErro("Erro ao salvar unidade.");
            e.printStackTrace();
        }
    }
    public void excluir(Long id) {
        try {
        	unidadeSaudeService.excluir(id);
            addMensagem("Unidade excluída com sucesso!");
            listar();

        } catch (RuntimeException e) {
            addMensagemErro(e.getMessage());
        } catch (Exception e) {
            addMensagemErro("Erro inesperado ao excluir unidade.");
            e.printStackTrace();
        }
    }

    public void editar(UnidadeSaudeEntity u) {
        this.unidade = u;
    }

    public void listar() {
        unidades = unidadeSaudeService.listarTodos();
        unidadesFiltradas = new ArrayList<>(unidades);
    }
    
    public void filtrar() {

        if (filtroUnidade == null || filtroUnidade.isBlank()) {
        	unidadesFiltradas = new ArrayList<>(unidades);
            return;
        }

        String f = filtroUnidade.toLowerCase();

        unidadesFiltradas = unidades.stream()
            .filter(u ->
                u.getNome().toLowerCase().contains(f) ||
                u.getCnpj().contains(f) ||
                u.getCnes().contains(f) 
            )
            .toList();
    }
    
    public void limparFiltro() {
        filtroUnidade = null;
        unidadesFiltradas = new ArrayList<>(unidades);
    }
    
    
    
    

    public void setUnidade(UnidadeSaudeEntity unidade) {
		this.unidade = unidade;
	}

	public void setUnidades(List<UnidadeSaudeEntity> unidades) {
		this.unidades = unidades;
	}

	public List<UnidadeSaudeEntity> getUnidadesFiltradas() {
		return unidadesFiltradas;
	}

	public void setUnidadesFiltradas(List<UnidadeSaudeEntity> unidadesFiltradas) {
		this.unidadesFiltradas = unidadesFiltradas;
	}

	public String getFiltroUnidade() {
		return filtroUnidade;
	}

	public void setFiltroUnidade(String filtroUnidade) {
		this.filtroUnidade = filtroUnidade;
	}

	private void addMensagem(String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
    }

    private void addMensagemErro(String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
    }

    public UnidadeSaudeEntity getUnidade() {
        return unidade;
    }

    public List<UnidadeSaudeEntity> getUnidades() {
        return unidades;
    }
    
    
}
