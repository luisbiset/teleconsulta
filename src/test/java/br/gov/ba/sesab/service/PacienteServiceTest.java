package br.gov.ba.sesab.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.gov.ba.sesab.entity.PacienteEntity;
import br.gov.ba.sesab.entity.UsuarioEntity;
import br.gov.ba.sesab.enums.PerfilUsuario;
import br.gov.ba.sesab.repository.PacienteRepository;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private PacienteService pacienteService;
    @Test
    void deveSalvarPaciente() {
        PacienteEntity paciente = new PacienteEntity();

        assertDoesNotThrow(() -> pacienteService.salvar(paciente));

        verify(pacienteRepository, times(1)).salvar(paciente);
    }
    @Test
    void deveLancarErroQuandoUsuarioForNulo() {
        PacienteEntity paciente = new PacienteEntity();
        paciente.setUsuario(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> pacienteService.salvarComUsuario(paciente));

        assertEquals("Usuário é obrigatório.", ex.getMessage());
        verify(pacienteRepository, never()).salvar(any());
    }

    @Test
    void deveLancarErroQuandoCpfForVazio() {
        PacienteEntity paciente = new PacienteEntity();
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setCpf("");
        paciente.setUsuario(usuario);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> pacienteService.salvarComUsuario(paciente));

        assertEquals("CPF é obrigatório.", ex.getMessage());
        verify(pacienteRepository, never()).salvar(any());
    }

    @Test
    void deveUsarUsuarioExistenteQuandoCpfJaCadastrado() {
        PacienteEntity paciente = new PacienteEntity();

        UsuarioEntity usuarioTela = new UsuarioEntity();
        usuarioTela.setCpf("123.456.789-00");
        usuarioTela.setNome("Novo Nome");
        usuarioTela.setEmail("novo@email.com");
        usuarioTela.setAtivo(true);

        paciente.setUsuario(usuarioTela);

        UsuarioEntity usuarioBanco = new UsuarioEntity();
        usuarioBanco.setCpf("12345678900");
        usuarioBanco.setNome("Antigo Nome");
        usuarioBanco.setEmail("antigo@email.com");
        usuarioBanco.setAtivo(false);

        when(usuarioService.buscarUsuarioPorCpf("12345678900"))
            .thenReturn(usuarioBanco);

        pacienteService.salvarComUsuario(paciente);

        assertEquals("Novo Nome", usuarioBanco.getNome());
        assertEquals("novo@email.com", usuarioBanco.getEmail());
        assertTrue(usuarioBanco.isAtivo());

        assertEquals(usuarioBanco, paciente.getUsuario());

        verify(pacienteRepository, times(1)).salvar(paciente);
    }
    @Test
    void deveCriarNovoUsuarioQuandoCpfNaoExistir() {
        PacienteEntity paciente = new PacienteEntity();

        UsuarioEntity usuarioTela = new UsuarioEntity();
        usuarioTela.setCpf("123.456.789-00");
        usuarioTela.setNome("Paciente Novo");

        paciente.setUsuario(usuarioTela);

        when(usuarioService.buscarUsuarioPorCpf("12345678900"))
            .thenReturn(null);

        pacienteService.salvarComUsuario(paciente);

        UsuarioEntity usuarioFinal = paciente.getUsuario();

        assertNotNull(usuarioFinal.getSenha());
        assertEquals(PerfilUsuario.PACIENTE, usuarioFinal.getPerfil());
        assertFalse(usuarioFinal.isAtivo());
        assertNotNull(usuarioFinal.getDataCadastro());

        verify(pacienteRepository, times(1)).salvar(paciente);
    }

    @Test
    void deveExcluirPaciente() {
        pacienteService.excluir(1L);

        verify(pacienteRepository, times(1)).excluir(1L);
    }
    @Test
    void deveListarPacientes() {
        when(pacienteRepository.listarTodos()).thenReturn(List.of());

        List<PacienteEntity> lista = pacienteService.listarTodos();

        assertNotNull(lista);
        verify(pacienteRepository, times(1)).listarTodos();
    }
}
