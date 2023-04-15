package br.com.cronos.hermes.controllers;

import br.com.cronos.hermes.dto.LancamentoDto;
import br.com.cronos.hermes.services.LancamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/lancamentos")
@Tag(name = "Lancamentos", description = "Api para a manipulação dos lancamentos do sistema")
public class LancamentoController {

    private final Logger logger = LoggerFactory.getLogger(LancamentoController.class);
    private final LancamentoService lancamentoService;

    public LancamentoController(LancamentoService lancamentoService) {
        this.lancamentoService = lancamentoService;
    }

    @Operation(summary = "API para listar todos os lancamentos existentes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Retorna OK com a listagem de lancamentos"),
            @ApiResponse(responseCode = "401", description = "Erro de autenticação desta api"),
            @ApiResponse(responseCode = "403", description = "Erro de autorização desta api"),
            @ApiResponse(responseCode = "404", description = "Recurso não foi encontrado")
    })
    @GetMapping
    public ResponseEntity<Page<LancamentoDto>> findAll(Pageable pageable) {
        logger.info("CONTROLLER - Listar todos os lancamentos ");
        return ResponseEntity.ok(lancamentoService.listarTodos(pageable));
    }

    @Operation(summary = "API para buscar um lancamento pelo id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Retorna OK com o lancamento com o id informado"),
        @ApiResponse(responseCode = "401", description = "Erro de autenticação desta api"),
        @ApiResponse(responseCode = "403", description = "Erro de autorização desta api"),
        @ApiResponse(responseCode = "404", description = "Recurso não foi encontrado")
    })
    @Parameters(value = {@Parameter(name = "id", in = ParameterIn.PATH)})
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LancamentoDto> findById(@PathVariable(name = "id") final UUID id) {
        logger.info("CONTROLLER - Buscar um lancamento pelo id {} ", id);
        return ResponseEntity.ok(lancamentoService.buscarPorId(id));
    }

    @Operation(summary = "API para criar um novo lancamento")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Retorna OK com o lancamento criado"),
        @ApiResponse(responseCode = "401", description = "Erro de autenticação desta api"),
        @ApiResponse(responseCode = "403", description = "Erro de autorização desta api"),
        @ApiResponse(responseCode = "404", description = "Recurso não foi encontrado")
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LancamentoDto> created(@Valid @RequestBody LancamentoDto lancamentoDto) {
        logger.info("CONTROLLER - Criar um novo lancamento  {} ", lancamentoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoService.criarNovo(lancamentoDto));
    }

    @Operation(summary = "API para alterar um lancamento pelo id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna OK caso o lancamento tenha sido alterado"),
        @ApiResponse(responseCode = "401", description = "Erro de autenticação desta api"),
        @ApiResponse(responseCode = "403", description = "Erro de autorização desta api"),
        @ApiResponse(responseCode = "404", description = "Recurso não foi encontrado")
    })
    @Parameters(value = {@Parameter(name = "id", in = ParameterIn.PATH)})
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LancamentoDto> updated(@PathVariable(name = "id") UUID id, @Valid @RequestBody LancamentoDto lancamentoDto) {
        logger.info("CONTROLLER - Atualizando um lancamento  de id {} com as informacoes {} ", id, lancamentoDto);
        return ResponseEntity.ok(lancamentoService.atualizar(id, lancamentoDto));
    }

    @Operation(summary = "API para excluir um lancamento pelo id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Retorna OK para a remoção do lancamento"),
        @ApiResponse(responseCode = "401", description = "Erro de autenticação desta api"),
        @ApiResponse(responseCode = "403", description = "Erro de autorização desta api"),
        @ApiResponse(responseCode = "404", description = "Recurso não foi encontrado")
    })
    @Parameters(value = {@Parameter(name = "id", in = ParameterIn.PATH)})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleted(@PathVariable(name = "id") UUID id) {
        logger.info("CONTROLLER - Excluindo um lancamento com id {} ", id);
        lancamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}