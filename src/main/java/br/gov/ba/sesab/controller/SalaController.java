package br.gov.ba.sesab.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.gov.ba.sesab.entity.SalaEntity;
import br.gov.ba.sesab.entity.UnidadeSaudeEntity;
import br.gov.ba.sesab.service.SalaService;
import br.gov.ba.sesab.service.UnidadeSaudeService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class SalaController implements Serializable {

    private static final long serialVersionUID = 1L;

    private SalaEntity sala;
    private List<SalaEntity> salas;
    private List<SalaEntity> salasFiltradas; 
    private List<UnidadeSaudeEntity> unidades;
    private Long idUnidadeSelecionada;

    private String filtroSala;

    @Inject
    private SalaService salaService;

    @Inject
    private UnidadeSaudeService unidadeService;

    @PostConstruct
    public void init() {
        sala = new SalaEntity();
        listar();
        carregarUnidades();
    }
    
    public void novo() {
        sala = new SalaEntity();
        idUnidadeSelecionada = null;
    }

    public void salvar() {
        try {

            // EDIÇÃO: mantém a unidade original
            if (sala.getId() != null) {
                // garante que a unidade já está preenchida na entidade sala (via edição)
                // se quiser garantir:
                if (sala.getUnidade() == null || sala.getUnidade().getId() == null) {
                    addMensagemErro("Unidade da sala não encontrada.");
                    return;
                }

            } 
            // NOVO CADASTRO: exige unidade selecionada
            else {

                if (idUnidadeSelecionada == null) {
                    addMensagemErro("Selecione uma unidade.");
                    return;
                }

                // 👉 NÃO VAMOS MAIS NO BANCO
                UnidadeSaudeEntity unidadeRef = new UnidadeSaudeEntity();
                unidadeRef.setId(idUnidadeSelecionada);
                sala.setUnidade(unidadeRef);
            }

            salaService.salvar(sala);

            addMensagem("Sala salva com sucesso!");

            sala = new SalaEntity();
            idUnidadeSelecionada = null;

            listar();

        } catch (Exception e) {
            addMensagemErro("Erro ao salvar a sala.");
            e.printStackTrace();
        }
    }




    public void excluir(Long id) {
        try {
            salaService.excluir(id);
            addMensagem("Sala excluída com sucesso!");
            listar();
        } catch (Exception e) {
            addMensagemErro("Erro ao excluir a sala.");
            e.printStackTrace();
        }
    }

    public void editar(SalaEntity s) {
        this.sala = s;

        if (s.getUnidade() != null) {
            this.idUnidadeSelecionada = s.getUnidade().getId();
        }
    }

    public void listar() {
        salas = salaService.listarTodas();
        salasFiltradas = new ArrayList<>(salas); 
    }

    public void carregarUnidades() {
        unidades = unidadeService.listarTodos();
    }

    public void filtrar() {
        if (filtroSala == null || filtroSala.isBlank()) {
            salasFiltradas = new ArrayList<>(salas);
            return;
        }

        String f = filtroSala.toLowerCase();

        salasFiltradas = salas.stream()
            .filter(s ->
                (s.getNome() != null && s.getNome().toLowerCase().contains(f)) ||
                (s.getUnidade() != null &&
                 s.getUnidade().getNome() != null &&
                 s.getUnidade().getNome().toLowerCase().contains(f))
            )
            .toList();
    }
    
    public void limparFiltro() {
        filtroSala = null;
        salasFiltradas = new ArrayList<>(salas);
    }

    private void addMensagem(String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
    }

    private void addMensagemErro(String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
    }

  

    public SalaEntity getSala() {
        return sala;
    }

    public void setSala(SalaEntity sala) {
        this.sala = sala;
    }

    public List<SalaEntity> getSalas() {
        return salasFiltradas; 
    }

    public List<UnidadeSaudeEntity> getUnidades() {
        return unidades;
    }

    public Long getIdUnidadeSelecionada() {
        return idUnidadeSelecionada;
    }

    public void setIdUnidadeSelecionada(Long idUnidadeSelecionada) {
        this.idUnidadeSelecionada = idUnidadeSelecionada;
    }

    public String getFiltroSala() { 
        return filtroSala;
    }

    public void setFiltroSala(String filtroSala) { 
        this.filtroSala = filtroSala;
    }
}
