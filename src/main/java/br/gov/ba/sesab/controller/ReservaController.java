package br.gov.ba.sesab.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.gov.ba.sesab.entity.ReservaEntity;
import br.gov.ba.sesab.entity.SalaEntity;
import br.gov.ba.sesab.entity.UnidadeSaudeEntity;
import br.gov.ba.sesab.entity.UsuarioEntity;
import br.gov.ba.sesab.service.ReservaService;
import br.gov.ba.sesab.service.SalaService;
import br.gov.ba.sesab.service.UnidadeSaudeService;
import br.gov.ba.sesab.service.UsuarioService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class ReservaController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ReservaService reservaService;

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private SalaService salaService;

	@Inject
	private UnidadeSaudeService unidadeService;

	private List<UnidadeSaudeEntity> unidades;
	private List<SalaEntity> salasDaUnidade;

	private Long idUnidadeSelecionada;

	private List<ReservaEntity> reservas;
	private List<SalaEntity> salas;

	private ReservaEntity reserva;

	private Long idSalaSelecionada;
	private String cpfSolicitante;
	private boolean modoEdicao;
	
	

	

	@PostConstruct
	public void init() {
		listarReservas();
		unidades = unidadeService.listarTodos();
		salasDaUnidade = new ArrayList<>();
		novo();
	}

	public void novo() {
		reserva = new ReservaEntity();

		idUnidadeSelecionada = null;
		idSalaSelecionada = null;

		salasDaUnidade.clear();
		cpfSolicitante = null;
		modoEdicao = false;
	}

	public void editar(ReservaEntity r) {

		modoEdicao = true;

		this.reserva = r;

		// Sala selecionada
		if (r.getSala() != null) {
			idSalaSelecionada = r.getSala().getId();

			// Unidade vem pela sala
			if (r.getSala().getUnidade() != null) {
				idUnidadeSelecionada = r.getSala().getUnidade().getId();

				// Carrega as salas da unidade para popular o combo
				salasDaUnidade = salaService.listarPorUnidade(idUnidadeSelecionada);
			} else {
				idUnidadeSelecionada = null;
				salasDaUnidade.clear();
			}

		} else {
			idSalaSelecionada = null;
			idUnidadeSelecionada = null;
			salasDaUnidade.clear();
		}

		// CPF vem do usuário solicitante
		if (r.getUsuarioSolicitante() != null) {
			cpfSolicitante = r.getUsuarioSolicitante().getCpf();
		} else {
			cpfSolicitante = null;
		}
	}

	public void salvar() {
		try {

			if (modoEdicao) {
				reserva.setUsuarioSolicitante(reservaService.buscarPorId(reserva.getId()).getUsuarioSolicitante());
				reserva.setSala(reservaService.buscarPorId(reserva.getId()).getSala());
			}

			if (reserva.getUsuarioSolicitante() == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuário solicitante é obrigatório.", null));
				return;
			}

			if (idUnidadeSelecionada == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Unidade é obrigatória.", null));
				return;
			}

			if (idSalaSelecionada == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sala é obrigatória.", null));
				return;
			}

			SalaEntity sala = salaService.findById(idSalaSelecionada);

			if (!sala.getUnidade().getId().equals(idUnidadeSelecionada)) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"A sala não pertence à unidade selecionada.", null));
				return;
			}

			reserva.setSala(sala);

			if (reserva.getDataInicio() == null || reserva.getDataFim() == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Data e horário são obrigatórios.", null));
				return;
			}

			if (reserva.getDataFim().isBefore(reserva.getDataInicio())) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"O horário final deve ser depois do inicial.", null));
				return;
			}

			reservaService.salvar(reserva);

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Reserva cadastrada com sucesso!", null));

			listarReservas();
			novo();

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
		}
	}

	public void excluir(Long id) {

		try {
			reservaService.excluir(id);

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Reserva removida com sucesso!", null));

			listarReservas();

		} catch (Exception e) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
		}
	}

	public void listarReservas() {
		reservas = reservaService.listarTodas();
	}

	public void carregarSalasDaUnidade() {

		if (idUnidadeSelecionada == null) {
			salasDaUnidade.clear();
			idSalaSelecionada = null;
			return;
		}

		salasDaUnidade = salaService.listarPorUnidade(idUnidadeSelecionada);
		idSalaSelecionada = null; // força nova escolha
	}

	public void buscarUsuarioPorCpf() {

		if (cpfSolicitante == null || cpfSolicitante.isBlank()) {
			reserva.setUsuarioSolicitante(null);
			return;
		}

		String cpfLimpo = cpfSolicitante.replaceAll("[^0-9]", "");

		UsuarioEntity usuario = usuarioService.buscarUsuarioPorCpf(cpfLimpo);

		if (usuario == null) {
			reserva.setUsuarioSolicitante(null);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Nenhum usuário encontrado com esse CPF.", null));
		} else {
			reserva.setUsuarioSolicitante(usuario);
		}
	}

	public List<ReservaEntity> getReservas() {
		return reservas;
	}

	public List<UnidadeSaudeEntity> getUnidades() {
		return unidades;
	}

	public void setUnidades(List<UnidadeSaudeEntity> unidades) {
		this.unidades = unidades;
	}

	public List<SalaEntity> getSalasDaUnidade() {
		return salasDaUnidade;
	}

	public void setSalasDaUnidade(List<SalaEntity> salasDaUnidade) {
		this.salasDaUnidade = salasDaUnidade;
	}

	public Long getIdUnidadeSelecionada() {
		return idUnidadeSelecionada;
	}

	public void setIdUnidadeSelecionada(Long idUnidadeSelecionada) {
		this.idUnidadeSelecionada = idUnidadeSelecionada;
	}

	public void setReservas(List<ReservaEntity> reservas) {
		this.reservas = reservas;
	}

	public void setSalas(List<SalaEntity> salas) {
		this.salas = salas;
	}

	public void setReserva(ReservaEntity reserva) {
		this.reserva = reserva;
	}

	public List<SalaEntity> getSalas() {
		return salas;
	}

	public ReservaEntity getReserva() {
		return reserva;
	}

	public Long getIdSalaSelecionada() {
		return idSalaSelecionada;
	}

	public void setIdSalaSelecionada(Long idSalaSelecionada) {
		this.idSalaSelecionada = idSalaSelecionada;
	}

	public String getCpfSolicitante() {
		return cpfSolicitante;
	}

	public void setCpfSolicitante(String cpfSolicitante) {
		this.cpfSolicitante = cpfSolicitante;
	}

	public boolean isModoEdicao() {
		return modoEdicao;
	}

}
