package br.com.cronos.hermes.controllers;

import br.com.cronos.hermes.core.BaseUnitTest;
import br.com.cronos.hermes.entities.enums.StatusLancamento;
import br.com.cronos.hermes.entities.enums.TipoLancamento;
import br.com.cronos.hermes.services.LancamentoService;
import br.com.cronos.hermes.utils.JsonConvertion;
import br.com.cronos.hermes.utils.LancamentoConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Arrays;

import static br.com.cronos.hermes.utils.LancamentoConstants.BASE_URL;
import static br.com.cronos.hermes.utils.LancamentoData.newLancamentoDtoBuilder;
import static br.com.cronos.hermes.utils.LancamentoData.updateLancamentoDtoBuilder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LancamentoControllerTest extends BaseUnitTest {
    @Mock
    private LancamentoService lancamentoService;
    @InjectMocks
    private LancamentoController lancamentoController;
    private MockMvc mockMvc;
    
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(lancamentoController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .setViewResolvers((s,  locale) -> new MappingJackson2JsonView())
            .build();
    }
    
    @Test
    void quandoPOSTForChamadoDeveRetornarStatus201() throws Exception {
        var dto = newLancamentoDtoBuilder().build();
        
        when(lancamentoService.criarNovo(dto)).thenReturn(dto);
        
        mockMvc.perform(post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonConvertion.asJsonString(dto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.descricao", is(dto.getDescricao())))
            .andExpect(jsonPath("$.tipo", equalTo(TipoLancamento.DESPESA.toString())))
            .andExpect(jsonPath("$.status", equalTo(StatusLancamento.PENDENTE.toString())))
            .andExpect(jsonPath("$.mes", is(dto.getMes())))
            .andExpect(jsonPath("$.ano", is(dto.getAno())))
            .andExpect(jsonPath("$.usuario", not(nullValue())))
            .andExpect(jsonPath("$.valor", is(dto.getValor().doubleValue())));
    }
    
    @Test
    void quandoUmPOSTForChamadoSemOsCamposObrigatoriosUmStatus400DeveSerRetornado() throws Exception {
        var dto = newLancamentoDtoBuilder().build();
        dto.setUsuario(null);
        
        mockMvc.perform(post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonConvertion.asJsonString(dto)))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    void quandoBuscarPorIdInformandoUmIdValidoDeveRetornarUmStatus200() throws Exception {
        var dto = updateLancamentoDtoBuilder().build();
        
        when(lancamentoService.buscarPorId(dto.getId())).thenReturn(dto);

        mockMvc.perform(get(BASE_URL + "/" + dto.getId())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.descricao", is(dto.getDescricao())))
            .andExpect(jsonPath("$.tipo", equalTo(TipoLancamento.RECEITA.toString())))
            .andExpect(jsonPath("$.status", equalTo(StatusLancamento.EFETIVADO.toString())))
            .andExpect(jsonPath("$.mes", is(dto.getMes())))
            .andExpect(jsonPath("$.ano", is(dto.getAno())))
            .andExpect(jsonPath("$.usuario", not(nullValue())))
            .andExpect(jsonPath("$.valor", is(dto.getValor().doubleValue())));
    }
    
    @Test
    void quandoBuscarTodosForChamadoDeveRetornarUmaListaDeLancamentoDtosEStatus200() throws Exception {
        var dtoEsperado = updateLancamentoDtoBuilder().build();

        when(lancamentoService.listarTodos(any(Pageable.class))).thenReturn(new PageImpl(Arrays.asList(dtoEsperado), Pageable.ofSize(10), 1L));
        
        mockMvc.perform(get(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id", is(dtoEsperado.getId().toString())))
            .andExpect(jsonPath("$.content[0].descricao", is(dtoEsperado.getDescricao())))
            .andExpect(jsonPath("$.content[0].tipo", equalTo(TipoLancamento.RECEITA.toString())))
            .andExpect(jsonPath("$.content[0].status", equalTo(StatusLancamento.EFETIVADO.toString())))
            .andExpect(jsonPath("$.content[0].mes", is(dtoEsperado.getMes())))
            .andExpect(jsonPath("$.content[0].ano", is(dtoEsperado.getAno())))
            .andExpect(jsonPath("$.content[0].usuario", not(nullValue())))
            .andExpect(jsonPath("$.content[0].valor", is(dtoEsperado.getValor().doubleValue())));
    }
    
    @Test
    void quandoForAtualizarUmRegistroDeveRetornarStatus200() throws Exception {
        var dtoAtualizadoEsperado = updateLancamentoDtoBuilder().build();
        
        when(lancamentoService.atualizar(dtoAtualizadoEsperado.getId(), dtoAtualizadoEsperado))
        .thenReturn(dtoAtualizadoEsperado);
        
        mockMvc.perform(put(BASE_URL + "/" + dtoAtualizadoEsperado.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonConvertion.asJsonString(dtoAtualizadoEsperado)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.descricao", is(dtoAtualizadoEsperado.getDescricao())))
            .andExpect(jsonPath("$.tipo", equalTo(TipoLancamento.RECEITA.toString())))
            .andExpect(jsonPath("$.status", equalTo(StatusLancamento.EFETIVADO.toString())))
            .andExpect(jsonPath("$.id", is(dtoAtualizadoEsperado.getId().toString())))
            .andExpect(jsonPath("$.mes", is(dtoAtualizadoEsperado.getMes())))
            .andExpect(jsonPath("$.ano", is(dtoAtualizadoEsperado.getAno())))
            .andExpect(jsonPath("$.usuario", not(nullValue())))
            .andExpect(jsonPath("$.valor", is(dtoAtualizadoEsperado.getValor().doubleValue())));
    }
    
    @Test
    void quandoForDeletarUmRegistroInformandoUmIdValidoDeveRetornarUmStatus204() throws Exception {
        var dtoEsperado = updateLancamentoDtoBuilder().build();
        
        doNothing().when(lancamentoService).deletar(dtoEsperado.getId());
        
        mockMvc.perform(delete(BASE_URL + "/" + dtoEsperado.getId())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }
}
