package br.gov.ba.sesab.controller;

import java.io.Serializable;
import java.util.List;

import br.gov.ba.sesab.entity.PacienteEntity;
import br.gov.ba.sesab.service.PacienteService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class PacienteController implements Serializable {

    private static final long serialVersionUID = 1L;

    private PacienteEntity paciente;
    private List<PacienteEntity> pacientes;

    @Inject
    private PacienteService pacienteService;

    @PostConstruct
    public void init() {
        novo();
        listar();
    }

    // ==========================
    // NOVO (LIMPA FORMULÁRIO)
    // ==========================
    public void novo() {
        paciente = new PacienteEntity();
    }

    // ==========================
    // SALVAR (CADASTRO + EDIÇÃO)
    // ==========================
    public void salvar() {
        try {
            pacienteService.salvar(paciente);

            addMensagem("Paciente salvo com sucesso!");
            novo();      // limpa formulário
            listar();    // recarrega tabela

        } catch (Exception e) {
            addMensagemErro("Erro ao salvar paciente.");
            e.printStackTrace();
        }
    }

    // ==========================
    // EXCLUIR
    // ==========================
    public void excluir(Long id) {
        try {
            pacienteService.excluir(id);
            addMensagem("Paciente excluído com sucesso!");
            listar();

        } catch (Exception e) {
            addMensagemErro("Erro ao excluir paciente.");
            e.printStackTrace();
        }
    }

    // ==========================
    // EDITAR (CARREGA O OBJETO)
    // ==========================
    public void editar(PacienteEntity p) {
        this.paciente = p;
    }

    // ==========================
    // LISTAR
    // ==========================
    public void listar() {
        pacientes = pacienteService.listarTodos();
    }

    // ==========================
    // MENSAGENS
    // ==========================
    private void addMensagem(String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
    }

    private void addMensagemErro(String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
    }

    // ==========================
    // GETTERS
    // ==========================
    public PacienteEntity getPaciente() {
        return paciente;
    }

    public List<PacienteEntity> getPacientes() {
        return pacientes;
    }
}

