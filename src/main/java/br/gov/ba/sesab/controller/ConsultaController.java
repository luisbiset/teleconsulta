package br.gov.ba.sesab.controller;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import br.gov.ba.sesab.entity.SalaEntity;
import br.gov.ba.sesab.entity.UnidadeSaudeEntity;
import br.gov.ba.sesab.service.ReservaService;
import br.gov.ba.sesab.service.UnidadeSaudeService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@ViewScoped
public class ConsultaController implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idUnidade;
    private String dataInicio;
    private String horaInicio;
    private String dataFim;
    private String horaFim;



    private List<SalaEntity> salasDisponiveis;
    private List<UnidadeSaudeEntity> unidades;

    @Inject
    private ReservaService reservaService;

    @Inject
    private UnidadeSaudeService unidadeService;

    @PostConstruct
    public void init() {
        unidades = unidadeService.listarTodos();
    }

    public void consultarDisponibilidade() {

        try {


            if (idUnidade == null) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Unidade é obrigatória.", null));
                return;
            }

            if (horaInicio == null || horaInicio.isBlank()
             || horaFim == null || horaFim.isBlank()
             || dataInicio == null || dataInicio.isBlank()
             || dataFim == null || dataFim.isBlank()) {

                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Todos os campos de data e hora são obrigatórios.", null));
                return;
            }


            DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm");

            LocalDate dataIni = LocalDate.parse(dataInicio, dataFormatter);
            LocalDate dataFimL = LocalDate.parse(dataFim, dataFormatter);

            LocalTime horaIni = LocalTime.parse(horaInicio, horaFormatter);
            LocalTime horaFimL = LocalTime.parse(horaFim, horaFormatter);

            LocalDateTime inicio = LocalDateTime.of(dataIni, horaIni);
            LocalDateTime fim = LocalDateTime.of(dataFimL, horaFimL);


            if (fim.isBefore(inicio) || fim.isEqual(inicio)) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "A data/hora final deve ser maior que a inicial.", null));
                return;
            }


            salasDisponiveis = reservaService.consultarDisponibilidade(
                idUnidade, inicio, fim
            );

            if (salasDisponiveis == null || salasDisponiveis.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                        "Nenhuma sala disponível para o período informado.", null));
            }

        } catch (DateTimeParseException e) {

            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Formato inválido de data ou hora.", null));

        } catch (Exception e) {

            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "Erro ao consultar: " + e.getMessage(), null));
        }
    }





    public Long getIdUnidade() {
        return idUnidade;
    }

    public void setIdUnidade(Long idUnidade) {
        this.idUnidade = idUnidade;
    }

   

    public String getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
	}

	public String getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(String horaInicio) {
		this.horaInicio = horaInicio;
	}

	public String getDataFim() {
		return dataFim;
	}

	public void setDataFim(String dataFim) {
		this.dataFim = dataFim;
	}

	public String getHoraFim() {
		return horaFim;
	}

	public void setHoraFim(String horaFim) {
		this.horaFim = horaFim;
	}

	public void setUnidades(List<UnidadeSaudeEntity> unidades) {
		this.unidades = unidades;
	}

	public List<SalaEntity> getSalasDisponiveis() {
        return salasDisponiveis;
    }

    public List<UnidadeSaudeEntity> getUnidades() {
        return unidades;
    }
}
