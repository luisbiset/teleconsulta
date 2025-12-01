package br.gov.ba.sesab.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.gov.ba.sesab.entity.PacienteEntity;
import br.gov.ba.sesab.entity.UsuarioEntity;
import br.gov.ba.sesab.service.PacienteService;
import br.gov.ba.sesab.service.UsuarioService;
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

	@Inject
	UsuarioService usuarioService;

	@PostConstruct
	public void init() {
		paciente = new PacienteEntity();
		paciente.setUsuario(new UsuarioEntity());
		listar();
	}

	public void novo() {
		paciente = new PacienteEntity();
		paciente.setUsuario(new UsuarioEntity());
	}

	public void salvar() {
	    try {
	        pacienteService.salvarComUsuario(paciente);

	        listar();
	        novo();

	        FacesContext.getCurrentInstance().addMessage(null,
	            new FacesMessage(FacesMessage.SEVERITY_INFO,
	                "Paciente salvo com sucesso!", null));

	    } catch (RuntimeException e) { 
	        FacesContext.getCurrentInstance().addMessage(null,
	            new FacesMessage(
	                FacesMessage.SEVERITY_WARN,
	                e.getMessage(), 
	                null
	            )
	        );

	    } catch (Exception e) { 
	        e.printStackTrace();

	        FacesContext.getCurrentInstance().addMessage(null,
	            new FacesMessage(
	                FacesMessage.SEVERITY_ERROR,
	                "Erro inesperado ao salvar paciente.",
	                null
	            )
	        );
	    }
	}

	public void editar(PacienteEntity p) {
		this.paciente = p;
	}

	public void listar() {
		pacientes = pacienteService.listarTodos();
		pacientesFiltrados = new ArrayList<>(pacientes);
	}

	public void filtrar() {
		if (filtroPaciente == null || filtroPaciente.isBlank()) {
			pacientesFiltrados = new ArrayList<>(pacientes);
			return;
		}

		String f = filtroPaciente.toLowerCase();
		pacientesFiltrados = pacientes.stream()
				.filter(p -> p.getUsuario().getNome().toLowerCase().contains(f) || p.getUsuario().getCpf().contains(f))
				.toList();
	}

	public void limparFiltro() {
		filtroPaciente = null;
		pacientesFiltrados = new ArrayList<>(pacientes);
	}
	
	public void excluir(Long id) {
	    try {
	        pacienteService.excluir(id);
	        listar();

	        FacesContext.getCurrentInstance().addMessage(null,
	            new FacesMessage(
	                FacesMessage.SEVERITY_INFO,
	                "Paciente excluído com sucesso!",
	                null
	            )
	        );

	    } catch (RuntimeException e) { 
	        FacesContext.getCurrentInstance().addMessage(null,
	            new FacesMessage(
	                FacesMessage.SEVERITY_WARN,
	                e.getMessage(),
	                null
	            )
	        );

	    } catch (Exception e) {
	        e.printStackTrace();

	        FacesContext.getCurrentInstance().addMessage(null,
	            new FacesMessage(
	                FacesMessage.SEVERITY_ERROR,
	                "Erro inesperado ao excluir paciente.",
	                null
	            )
	        );
	    }
	}


	public PacienteEntity getPaciente() {
		if (paciente == null)
			novo();
		return paciente;
	}

	public List<PacienteEntity> getPacientes() {
		return pacientes;
	}

	public void setPacientes(List<PacienteEntity> pacientes) {
		this.pacientes = pacientes;
	}

	public List<PacienteEntity> getPacientesFiltrados() {
		return pacientesFiltrados;
	}

	public void setPacientesFiltrados(List<PacienteEntity> pacientesFiltrados) {
		this.pacientesFiltrados = pacientesFiltrados;
	}

	public PacienteService getPacienteService() {
		return pacienteService;
	}

	public void setPacienteService(PacienteService pacienteService) {
		this.pacienteService = pacienteService;
	}

	public void setPaciente(PacienteEntity paciente) {
		this.paciente = paciente;
	}

	public String getFiltroPaciente() {
		return filtroPaciente;
	}

	public void setFiltroPaciente(String filtroPaciente) {
		this.filtroPaciente = filtroPaciente;
	}

	
	
}
