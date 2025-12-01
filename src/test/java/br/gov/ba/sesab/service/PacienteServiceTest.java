package br.gov.ba.sesab.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import br.gov.ba.sesab.entity.PacienteEntity;
import br.gov.ba.sesab.entity.UsuarioEntity;
import br.gov.ba.sesab.repository.PacienteRepository;
import br.gov.ba.sesab.repository.ReservaRepository;
import br.gov.ba.sesab.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Spy
    @InjectMocks
    private PacienteService pacienteService;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ReservaRepository reservaRepository;

    private PacienteEntity paciente;
    private UsuarioEntity usuario;

    @BeforeEach
    void setup() {
        usuario = new UsuarioEntity();
        usuario.setId(1L);
        usuario.setCpf("12345678900");
        usuario.setNome("Teste");
        usuario.setEmail("teste@email.com");

        paciente = new PacienteEntity();
        paciente.setUsuario(usuario);
        paciente.setRg("1234567890");
        paciente.setTelefone("71999999999");
        paciente.setDataNascimento(new Date());
        paciente.setSexo("M");
        paciente.setNomeMae("Mae");
        paciente.setNomePai("Pai");
        paciente.setEndereco("Endereco");
        paciente.setCns("123456789012345");
    }


    @Test
    void deveSalvarNovoPacienteComUsuario() {

        when(usuarioRepository.buscarPorCpf(anyString())).thenReturn(null);
        when(usuarioRepository.buscarPorLogin(anyString())).thenReturn(null);

        pacienteService.salvarComUsuario(paciente);

        verify(usuarioRepository).salvar(any());
        verify(pacienteRepository).salvar(any());
    }


    @Test
    void naoDeveExcluirPacienteComReserva() {

        doNothing().when(pacienteService).validarAdmin();

        when(pacienteRepository.findById(1L)).thenReturn(paciente);
        when(reservaRepository.existeReservaPorPaciente(1L)).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> pacienteService.excluir(1L));

        assertEquals(
            "Não é possível excluir o paciente pois existem reservas vinculadas.",
            ex.getMessage()
        );
    }

    @Test
    void deveExcluirPacienteSemReservas() {

        doNothing().when(pacienteService).validarAdmin();

        when(pacienteRepository.findById(1L)).thenReturn(paciente);
        when(reservaRepository.existeReservaPorPaciente(1L)).thenReturn(false);

        pacienteService.excluir(1L);

        verify(pacienteRepository).excluir(1L);
        verify(usuarioRepository).excluir(usuario.getId());
    }

    @Test
    void naoDeveExcluirPacienteInexistente() {

        doNothing().when(pacienteService).validarAdmin();

        when(pacienteRepository.findById(1L)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> pacienteService.excluir(1L));

        assertEquals("Paciente não encontrado.", ex.getMessage());
    }
}
