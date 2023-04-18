package br.com.cronos.hermes.controllers;

import br.com.cronos.hermes.core.BaseUnitTest;
import br.com.cronos.hermes.services.UsuarioService;
import br.com.cronos.hermes.controllers.UsuarioController;
import br.com.cronos.hermes.utils.JsonConvertion;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.Arrays;
import java.util.Collections;

import static br.com.cronos.hermes.utils.UsuarioConstants.BASE_URL;
import static br.com.cronos.hermes.utils.UsuarioData.newDtoBuilder;
import static br.com.cronos.hermes.utils.UsuarioData.updateDtoBuilder;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UsuarioControllerTest  extends BaseUnitTest {
    @Mock
    private UsuarioService usuarioService;
    @InjectMocks
    private UsuarioController usuarioController;
    private MockMvc mockMvc;
    
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .setViewResolvers((s,  locale) -> new MappingJackson2JsonView())
            .build();
    }
    
    @Test
    void quandoPOSTForChamadoDeveRetornarStatus201() throws Exception {
        var dtoEsperado = newDtoBuilder().build();
        
        when(usuarioService.criarNovo(dtoEsperado)).thenReturn(dtoEsperado);
        
        mockMvc.perform(post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonConvertion.asJsonString(dtoEsperado)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nome", is(dtoEsperado.getNome())))
            .andExpect(jsonPath("$.email", is(dtoEsperado.getEmail())))
            .andExpect(jsonPath("$.senha", is(dtoEsperado.getSenha())))
;
    }
    
    @Test
    void quandoUmPOSTForChamadoSemOsCamposObrigatoriosUmStatus400DeveSerRetornado() throws Exception {
        var dtoEsperado = newDtoBuilder().build();
        dtoEsperado.setNome(null);

        mockMvc.perform(post(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonConvertion.asJsonString(dtoEsperado)))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    void quandoBuscarPorIdInformandoUmIdValidoDeveRetornarUmStatus200() throws Exception {
        var dtoEsperado = updateDtoBuilder().build();
        
        when(usuarioService.buscarPorId(dtoEsperado.getId())).thenReturn(dtoEsperado);
        
        mockMvc.perform(get(BASE_URL + "/" + dtoEsperado.getId())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nome", is(dtoEsperado.getNome())))
            .andExpect(jsonPath("$.email", is(dtoEsperado.getEmail())))
            .andExpect(jsonPath("$.senha", is(dtoEsperado.getSenha())))
;
    }
    
    @Test
    void quandoBuscarTodosForChamadoDeveRetornarUmaListaDeUsuariosEStatus200() throws Exception {
        var dtoEsperado = updateDtoBuilder().build();


        when(usuarioService.listarTodos(any(Pageable.class))).thenReturn(new PageImpl(Arrays.asList(dtoEsperado), Pageable.ofSize(10), 1L));
        
        mockMvc.perform(get(BASE_URL)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].id", is(dtoEsperado.getId().toString())))
            .andExpect(jsonPath("$.content[0].nome", is(dtoEsperado.getNome())))
            .andExpect(jsonPath("$.content[0].email", is(dtoEsperado.getEmail())))
            .andExpect(jsonPath("$.content[0].senha", is(dtoEsperado.getSenha())))
;
    }
    
    @Test
    void quandoForAtualizarUmRegistroDeveRetornarStatus200() throws Exception {
        var dtoAtualizadoEsperado = updateDtoBuilder().build();
        
        when(usuarioService.atualizar(dtoAtualizadoEsperado.getId(), dtoAtualizadoEsperado))
        .thenReturn(dtoAtualizadoEsperado);
        
        mockMvc.perform(put(BASE_URL + "/" + dtoAtualizadoEsperado.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(JsonConvertion.asJsonString(dtoAtualizadoEsperado)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(dtoAtualizadoEsperado.getId().toString())))
            .andExpect(jsonPath("$.nome", is(dtoAtualizadoEsperado.getNome())))
            .andExpect(jsonPath("$.email", is(dtoAtualizadoEsperado.getEmail())))
            .andExpect(jsonPath("$.senha", is(dtoAtualizadoEsperado.getSenha())))
;
    }
    
    @Test
    void quandoForDeletarUmRegistroInformandoUmIdValidoDeveRetornarUmStatus204() throws Exception {
        var dtoEsperado = updateDtoBuilder().build();
        
        doNothing().when(usuarioService).deletar(dtoEsperado.getId());
        
        mockMvc.perform(delete(BASE_URL + "/" + dtoEsperado.getId())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }
}
