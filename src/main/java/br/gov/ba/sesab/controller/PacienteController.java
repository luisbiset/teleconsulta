package br.gov.ba.sesab.controller;

import java.io.Serializable;
import java.util.ArrayList;
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
    private List<PacienteEntity> pacientesFiltrados;
    private String filtroPaciente;


    


    @Inject
    private PacienteService pacienteService;

    @PostConstruct
    public void init() {
        novo();
        listar();
        pacientesFiltrados = new ArrayList<>(pacientes); 
    }

    public void novo() {
        paciente = new PacienteEntity();
    }
    public void salvar() {
        try {
        	
        	if (paciente.getId() != null) {
        	    PacienteEntity pacienteBanco = pacienteService.buscarPorId(paciente.getId());

        	    paciente.setCpf(pacienteBanco.getCpf());
        	}

            pacienteService.salvar(paciente);

            addMensagem("Paciente salvo com sucesso!");

            pacientes = pacienteService.listarTodos();

            pacientesFiltrados = new ArrayList<>(pacientes);

            novo();

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

            // ✅ Recarrega lista completa
            pacientes = pacienteService.listarTodos();

            // ✅ Atualiza a lista usada pela tabela (IMPORTANTE!)
            pacientesFiltrados = new ArrayList<>(pacientes);

        } catch (Exception e) {
            addMensagemErro("Erro ao excluir paciente.");
            e.printStackTrace();
        }
    }
    
    public String getFiltroPaciente() {
        return filtroPaciente;
    }

    public void setFiltroPaciente(String filtroPaciente) {
        this.filtroPaciente = filtroPaciente;
    }
    
    public void filtrar() {

        if (filtroPaciente == null || filtroPaciente.trim().isEmpty()) {
            pacientesFiltrados = new ArrayList<>(pacientes);
            return;
        }

        String filtro = filtroPaciente.toLowerCase();

        pacientesFiltrados = pacientes.stream()
            .filter(p ->
                   (p.getNome() != null && p.getNome().toLowerCase().contains(filtro))
                || (p.getCpf() != null && p.getCpf().toLowerCase().contains(filtro))
                || (p.getRg() != null && p.getRg().toLowerCase().contains(filtro))
                || (p.getTelefone() != null && p.getTelefone().toLowerCase().contains(filtro))
            )
            .toList();
    }
    
    public List<PacienteEntity> getPacientesFiltrados() {
		return pacientesFiltrados;
	}

	public void setPacientesFiltrados(List<PacienteEntity> pacientesFiltrados) {
		this.pacientesFiltrados = pacientesFiltrados;
	}

	public void limparFiltro() {
        filtroPaciente = null;
        pacientesFiltrados = new ArrayList<>(pacientes);
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

