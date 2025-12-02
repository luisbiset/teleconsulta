package br.gov.ba.sesab.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.gov.ba.sesab.entity.ReservaEntity;
import br.gov.ba.sesab.entity.SalaEntity;
import br.gov.ba.sesab.repository.ReservaRepository;

@ExtendWith(MockitoExtension.class)
class ReservaServiceSalvarTest {

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private ReservaService reservaService;

  
    @Test
    @DisplayName("salvar - deve lançar exceção quando existir conflito de reserva")
    void salvar_deveLancarExcecao_quandoExisteConflito() {

        ReservaEntity reserva = new ReservaEntity();
        setId(reserva,1L);

        SalaEntity sala = new SalaEntity();
        sala.setId(10L);

        reserva.setSala(sala);
        reserva.setDataInicio(LocalDateTime.now().plusHours(1));
        reserva.setDataFim(LocalDateTime.now().plusHours(2));

        when(reservaRepository.existeConflito(
                reserva.getId(),
                sala.getId(),
                reserva.getDataInicio(),
                reserva.getDataFim()
        )).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> reservaService.salvar(reserva));

        assertEquals(
                "Já existe uma reserva neste dia e horário para esta sala.",
                ex.getMessage()
        );

        verify(reservaRepository, never()).salvar(any());
    }
    @Test
    @DisplayName("salvar - deve salvar quando não existir conflito de reserva")
    void salvar_deveSalvar_quandoNaoExisteConflito() {

        ReservaEntity reserva = new ReservaEntity();
        setId(reserva,1L);

        SalaEntity sala = new SalaEntity();
        sala.setId(10L);

        reserva.setSala(sala);
        reserva.setDataInicio(LocalDateTime.now().plusHours(1));
        reserva.setDataFim(LocalDateTime.now().plusHours(2));

        when(reservaRepository.existeConflito(
                reserva.getId(),
                sala.getId(),
                reserva.getDataInicio(),
                reserva.getDataFim()
        )).thenReturn(false);

        reservaService.salvar(reserva);

        verify(reservaRepository).salvar(reserva);
    }
    
    private void setId(Object entidade, Long id) {
        try {
            var campo = entidade.getClass().getDeclaredField("id");
            campo.setAccessible(true);
            campo.set(entidade, id);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao setar ID via reflection no teste", e);
        }
    }
}
