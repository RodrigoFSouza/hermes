package br.com.cronos.hermes.services;

import br.com.cronos.hermes.core.BaseUnitTest;
import br.com.cronos.hermes.exceptions.BusinessNotFoundException;
import br.com.cronos.hermes.repositories.LancamentoRepository;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static br.com.cronos.hermes.utils.LancamentoData.newLancamentoDtoBuilder;
import static br.com.cronos.hermes.utils.LancamentoData.updateLancamentoDtoBuilder;
import static java.util.UUID.fromString;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class LancamentoServiceTest extends BaseUnitTest {

    @Mock
    private LancamentoRepository lancamentoDtoRepository;

    @InjectMocks
    private LancamentoService lancamentoDtoService;

    @Test
    void quandoNovoLancamentoDtoEhInformadoEntaoDeveSerCriado() {
        var lancamentoDtoCriadoDtoEsperado = newLancamentoDtoBuilder().build();
        var lancamentoDtoCriadoEsperado = lancamentoDtoCriadoDtoEsperado.toEntity();

        when(lancamentoDtoRepository.save(lancamentoDtoCriadoEsperado)).thenReturn(lancamentoDtoCriadoEsperado);

        var lancamentoDtoCriadoDto = lancamentoDtoService.criarNovo(lancamentoDtoCriadoDtoEsperado);
        assertThat(lancamentoDtoCriadoDto, is(equalTo(lancamentoDtoCriadoDtoEsperado)));
    }

    @Test
    void quandoUmIdValidoForInformadoUmLancamentoDtoDeveSerRetornado() {
        var lancamentoDtoEsperadoDto = newLancamentoDtoBuilder().build();
        var lancamentoDtoEncontradoEsperado = lancamentoDtoEsperadoDto.toEntity();

        when(lancamentoDtoRepository.findById(lancamentoDtoEsperadoDto.getId())).thenReturn(Optional.of(lancamentoDtoEncontradoEsperado));

        var lancamentoDtoEncontradoDto = lancamentoDtoService.buscarPorId(lancamentoDtoEsperadoDto.getId());

        assertThat(lancamentoDtoEncontradoDto, is(equalTo(lancamentoDtoEsperadoDto)));
    }

    @Test
    void quandoUmIdInvalidoForInformadoDeveRetornarUmaExcecao() {
        var lancamentoDtoEsperadoDto = newLancamentoDtoBuilder().build();

        when(lancamentoDtoRepository.findById(lancamentoDtoEsperadoDto.getId())).thenReturn(Optional.empty());

        assertThrows(BusinessNotFoundException.class, () -> lancamentoDtoService.buscarPorId(lancamentoDtoEsperadoDto.getId()));
    }

    @Test
    void quandoBuscarTodosLancamentoDtoUmListagemPaginadaDeveSerDevolvida() {
        var lancamentoDtoEsperadoDto = newLancamentoDtoBuilder().build();
        var lancamentoDtoEncontradoEsperado = lancamentoDtoEsperadoDto.toEntity();

        when(lancamentoDtoRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl(Arrays.asList(lancamentoDtoEncontradoEsperado), Pageable.ofSize(10), 1L));

        var foundLancamentoDtosDto = lancamentoDtoService.listarTodos(Pageable.ofSize(10));
        assertThat(foundLancamentoDtosDto.getTotalElements(), is(1L));
        assertThat(foundLancamentoDtosDto.getContent().get(0), is(equalTo(lancamentoDtoEsperadoDto)));
    }

    @Test
    void quandoBuscarTodosLancamentoDtoUmaListagemPaginadaVaziaDeveSerDevolvida() {
        when(lancamentoDtoRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl(Collections.emptyList(), Pageable.ofSize(10), 1L));

        var listagemLancamentosDto = lancamentoDtoService.listarTodos(Pageable.ofSize(10));

        assertThat(listagemLancamentosDto.getTotalElements(), is(0L));
    }

    @Test
    void quandoInformarUmLancamentoDtoValidoEntaoDeveSerAtualizado() {
        var lancamentoDtoEsperadoUpdateDto = updateLancamentoDtoBuilder().build();

        var lancamentoDtoEsperadoUpdate = lancamentoDtoEsperadoUpdateDto.toEntity();

        when(lancamentoDtoRepository.save(lancamentoDtoEsperadoUpdate)).thenReturn(lancamentoDtoEsperadoUpdate);
        when(lancamentoDtoRepository.findById(lancamentoDtoEsperadoUpdateDto.getId())).thenReturn(Optional.of(lancamentoDtoEsperadoUpdate));

        var lancamentoDtoAtualizado = lancamentoDtoService.atualizar(lancamentoDtoEsperadoUpdateDto.getId(), lancamentoDtoEsperadoUpdateDto);

        assertThat(lancamentoDtoAtualizado, is(equalTo(lancamentoDtoEsperadoUpdateDto)));
    }

    @Test
    void quandoForTentarAtualizarUmLancamentoDtoQueNaoExisteUmaExceptionDeveSerDisparada() {
        var lancamentoDtoEsperadoUpdateDto = updateLancamentoDtoBuilder().build();

        when(lancamentoDtoRepository.findById(lancamentoDtoEsperadoUpdateDto.getId())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> lancamentoDtoService.atualizar(lancamentoDtoEsperadoUpdateDto.getId(), lancamentoDtoEsperadoUpdateDto));
    }

    @Test
    void quandoInformarUmLancamentoDtoValidoComIdEntaoDeveSerDeletado() {
        var lancamentoDtoDeletadoEsperadoDto = newLancamentoDtoBuilder().build();
        var lancamentoDtoEsperadoDeletado = lancamentoDtoDeletadoEsperadoDto.toEntity();

        var lancamentoDtoEsperadoDeletadoId = lancamentoDtoDeletadoEsperadoDto.getId();

        doNothing().when(lancamentoDtoRepository).delete(lancamentoDtoEsperadoDeletado);
        when(lancamentoDtoRepository.findById(lancamentoDtoEsperadoDeletadoId)).thenReturn(Optional.of(lancamentoDtoEsperadoDeletado));

        lancamentoDtoService.deletar(lancamentoDtoEsperadoDeletadoId);

        verify(lancamentoDtoRepository, times(1)).delete(lancamentoDtoEsperadoDeletado);
        verify(lancamentoDtoRepository, times(1)).findById(lancamentoDtoEsperadoDeletadoId);
    }

    @Test
    void quandoForInformadoUmIdDeLancamentoDtoQueNaoExisteEntaoDeveLancarExcessao() {
        var lancamentoDtoInvalidoEsperado = fromString("8099ad37-6bcd-475d-8dc4-f9a4ca985f66");

        when(lancamentoDtoRepository.findById(lancamentoDtoInvalidoEsperado)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> lancamentoDtoService.deletar(lancamentoDtoInvalidoEsperado));
    }
}
