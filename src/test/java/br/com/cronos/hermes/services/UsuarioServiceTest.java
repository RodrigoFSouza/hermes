package br.com.cronos.hermes.services;

import br.com.cronos.hermes.core.BaseUnitTest;
import br.com.cronos.hermes.exceptions.BusinessNotFoundException;
import br.com.cronos.hermes.repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static br.com.cronos.hermes.utils.UsuarioData.newDtoBuilder;
import static br.com.cronos.hermes.utils.UsuarioData.updateDtoBuilder;
import static java.util.UUID.fromString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UsuarioServiceTest  extends BaseUnitTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void quandoNovoUsuarioEhInformadoEntaoDeveSerCriado() {
        var usuarioCriadoDtoEsperado = newDtoBuilder().build();
        var usuarioCriadoEsperado = usuarioCriadoDtoEsperado.toEntity();

        when(usuarioRepository.save(usuarioCriadoEsperado)).thenReturn(usuarioCriadoEsperado);

        var usuarioCriadoDto = usuarioService.criarNovo(usuarioCriadoDtoEsperado);
        assertThat(usuarioCriadoDto, is(equalTo(usuarioCriadoDtoEsperado)));
    }

    @Test
    void quandoUmIdValidoForInformadoUmUsuarioDeveSerRetornado() {
        var usuarioEsperadoDto = newDtoBuilder().build();
        var usuarioEncontradoEsperado = usuarioEsperadoDto.toEntity();

        when(usuarioRepository.findById(usuarioEsperadoDto.getId())).thenReturn(Optional.of(usuarioEncontradoEsperado));

        var usuarioEncontradoDto = usuarioService.buscarPorId(usuarioEsperadoDto.getId());

        assertThat(usuarioEncontradoDto, is(equalTo(usuarioEsperadoDto)));
    }

    @Test
    void quandoUmIdInvalidoForInformadoDeveRetornarUmaExcecao() {
        var usuarioEsperadoDto = newDtoBuilder().build();

        when(usuarioRepository.findById(usuarioEsperadoDto.getId())).thenReturn(Optional.empty());

        assertThrows(BusinessNotFoundException.class, () -> usuarioService.buscarPorId(usuarioEsperadoDto.getId()));
    }

    @Test
    void quandoBuscarTodosUsuarioUmListagemPaginadaDeveSerDevolvida() {
        var usuarioEsperadoDto = newDtoBuilder().build();
        var usuarioEncontradoEsperado = usuarioEsperadoDto.toEntity();

        when(usuarioRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl(Arrays.asList(usuarioEncontradoEsperado), Pageable.ofSize(10), 1L));

        var foundUsuariosDto = usuarioService.listarTodos(Pageable.ofSize(10));
        assertThat(foundUsuariosDto.getTotalElements(), is(1L));
        assertThat(foundUsuariosDto.getContent().get(0), is(equalTo(usuarioEsperadoDto)));
    }

    @Test
    void quandoBuscarTodosUsuarioUmaListagemPaginadaVaziaDeveSerDevolvida() {
        when(usuarioRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl(Collections.emptyList(), Pageable.ofSize(10), 1L));

        var listagemUsuariosDto = usuarioService.listarTodos(Pageable.ofSize(10));

        assertThat(listagemUsuariosDto.getTotalElements(), is(0L));
    }

    @Test
    void quandoInformarUmUsuarioValidoEntaoDeveSerAtualizado() {
        var usuarioEsperadoUpdateDto = updateDtoBuilder().build();

        var usuarioEsperadoUpdate = usuarioEsperadoUpdateDto.toEntity();

        when(usuarioRepository.save(usuarioEsperadoUpdate)).thenReturn(usuarioEsperadoUpdate);
        when(usuarioRepository.findById(usuarioEsperadoUpdateDto.getId())).thenReturn(Optional.of(usuarioEsperadoUpdate));

        var usuarioAtualizado = usuarioService.atualizar(usuarioEsperadoUpdateDto.getId(), usuarioEsperadoUpdateDto);

        assertThat(usuarioAtualizado, is(equalTo(usuarioEsperadoUpdateDto)));
    }

    @Test
    void quandoForTentarAtualizarUmUsuarioQueNaoExisteUmaExceptionDeveSerDisparada() {
        var usuarioEsperadoUpdateDto = updateDtoBuilder().build();

        when(usuarioRepository.findById(usuarioEsperadoUpdateDto.getId())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usuarioService.atualizar(usuarioEsperadoUpdateDto.getId(), usuarioEsperadoUpdateDto));
    }

    @Test
    void quandoInformarUmUsuarioValidoComIdEntaoDeveSerDeletado() {
        var usuarioDeletadoEsperadoDto = newDtoBuilder().build();
        var usuarioEsperadoDeletado = usuarioDeletadoEsperadoDto.toEntity();

        var usuarioEsperadoDeletadoId = usuarioDeletadoEsperadoDto.getId();

        doNothing().when(usuarioRepository).delete(usuarioEsperadoDeletado);
        when(usuarioRepository.findById(usuarioEsperadoDeletadoId)).thenReturn(Optional.of(usuarioEsperadoDeletado));

        usuarioService.deletar(usuarioEsperadoDeletadoId);

        verify(usuarioRepository, times(1)).delete(usuarioEsperadoDeletado);
        verify(usuarioRepository, times(1)).findById(usuarioEsperadoDeletadoId);
    }

    @Test
    void quandoForInformadoUmIdDeUsuarioQueNaoExisteEntaoDeveLancarExcessao() {
        var usuarioInvalidoEsperado = fromString("8099ad37-6bcd-475d-8dc4-f9a4ca985f66");

        when(usuarioRepository.findById(usuarioInvalidoEsperado)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> usuarioService.deletar(usuarioInvalidoEsperado));
    }
}
