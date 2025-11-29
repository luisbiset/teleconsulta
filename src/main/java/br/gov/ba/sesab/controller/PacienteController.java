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

    public void novo() {
        paciente = new PacienteEntity();
    }
    public void salvar() {
        try {

            pacienteService.salvar(paciente);

            addMensagem("Paciente salvo com sucesso!");
            novo();      
            listar();    

        } 
        catch (jakarta.validation.ConstraintViolationException e) {

            e.getConstraintViolations().forEach(v -> {
                FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, v.getMessage(), null)
                );
            });

        } 
        catch (Exception e) {
            addMensagemErro("Erro inesperado ao salvar paciente.");
            e.printStackTrace();
        }
    }


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

    public void editar(PacienteEntity p) {
        this.paciente = p;
    }

    public void listar() {
        pacientes = pacienteService.listarTodos();
    }

    private void addMensagem(String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, msg, null));
    }

    private void addMensagemErro(String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
    }

    public PacienteEntity getPaciente() {
        return paciente;
    }

    public List<PacienteEntity> getPacientes() {
        return pacientes;
    }
}

