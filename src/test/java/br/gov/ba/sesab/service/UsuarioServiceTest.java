package br.gov.ba.sesab.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import br.gov.ba.sesab.entity.UsuarioEntity;
import br.gov.ba.sesab.enums.PerfilUsuario;
import br.gov.ba.sesab.repository.UsuarioRepository;
import br.gov.ba.sesab.util.SessaoUtil;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void deveSalvarUsuarioQuandoLogadoForAdmin() {
        UsuarioEntity admin = new UsuarioEntity();
        admin.setPerfil(PerfilUsuario.ADMIN);

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setNome("Teste");
        usuario.setCpf("123");
        usuario.setSenha("123");

        try (MockedStatic<SessaoUtil> mocked = mockStatic(SessaoUtil.class)) {
            mocked.when(SessaoUtil::getUsuarioLogado).thenReturn(admin);

            assertDoesNotThrow(() -> usuarioService.salvar(usuario));

            verify(usuarioRepository).salvar(usuario);
            assertNotNull(usuario.getDataCadastro());
        }
    }

    @Test
    void naoDevePermitirSalvarUsuarioQuandoNaoForAdmin() {
        UsuarioEntity comum = new UsuarioEntity();
        comum.setPerfil(PerfilUsuario.OPERADOR);

        UsuarioEntity usuario = new UsuarioEntity();

        try (MockedStatic<SessaoUtil> mocked = mockStatic(SessaoUtil.class)) {
            mocked.when(SessaoUtil::getUsuarioLogado).thenReturn(comum);

            RuntimeException ex = assertThrows(RuntimeException.class, () -> {
                usuarioService.salvar(usuario);
            });

            assertEquals("Apenas ADMIN pode salvar usuários.", ex.getMessage());
            verify(usuarioRepository, never()).salvar(any());
        }
    }

    @Test
    void deveAutenticarUsuarioComSenhaValida() {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setCpf("123");
        usuario.setSenha("123");

        when(usuarioRepository.buscarPorCpf("123")).thenReturn(usuario);

        UsuarioEntity autenticado = usuarioService.autenticar("123", "123");

        assertNotNull(autenticado);
        assertEquals("123", autenticado.getCpf());
    }

    @Test
    void naoDeveAutenticarUsuarioComSenhaInvalida() {
        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setCpf("123");
        usuario.setSenha("123");

        when(usuarioRepository.buscarPorCpf("123")).thenReturn(usuario);

        UsuarioEntity autenticado = usuarioService.autenticar("123", "errada");

        assertNull(autenticado);
    }

    @Test
    void deveCriarUsuarioInicialQuandoNaoExistir() {
        when(usuarioRepository.buscarPorCpf("12345678900")).thenReturn(null);

        usuarioService.criarUsuarioInicial();

        verify(usuarioRepository).salvar(any(UsuarioEntity.class));
    }

    @Test
    void naoDeveCriarUsuarioInicialQuandoJaExistir() {
        when(usuarioRepository.buscarPorCpf("12345678900"))
            .thenReturn(new UsuarioEntity());

        usuarioService.criarUsuarioInicial();

        verify(usuarioRepository, never()).salvar(any());
    }
}
