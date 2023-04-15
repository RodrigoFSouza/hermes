package br.com.cronos.hermes.controllers;

import br.com.cronos.hermes.dto.UsuarioDto;
import br.com.cronos.hermes.services.UsuarioService;
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
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios", description = "Api para a manipulação dos usuarios do sistema")
public class UsuarioController {

    private final Logger logger = LoggerFactory.getLogger(UsuarioController.class);
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "API para listar todos os usuarios existentes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Retorna OK com a listagem de usuarios"),
        @ApiResponse(responseCode = "401", description = "Erro de autenticação desta api"),
        @ApiResponse(responseCode = "403", description = "Erro de autorização desta api"),
        @ApiResponse(responseCode = "404", description = "Recurso não foi encontrado")
    })
    @GetMapping
    public ResponseEntity<Page<UsuarioDto>> findAll(Pageable pageable) {
        logger.info("CONTROLLER - Listar todos os usuarios ");
        return ResponseEntity.ok(usuarioService.listarTodos(pageable));
    }

    @Operation(summary = "API para buscar um usuario pelo id")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Retorna OK com o usuario com o id informado"),
    @ApiResponse(responseCode = "401", description = "Erro de autenticação desta api"),
    @ApiResponse(responseCode = "403", description = "Erro de autorização desta api"),
    @ApiResponse(responseCode = "404", description = "Recurso não foi encontrado")
    })
    @Parameters(value = {@Parameter(name = "id", in = ParameterIn.PATH)})
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioDto> findById(@PathVariable(name = "id") final UUID id) {
        logger.info("CONTROLLER - Buscar um usuario pelo id {} ", id);
        return ResponseEntity.ok(usuarioService.buscarPorId(id));
    }

    @Operation(summary = "API para criar um novo usuario")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Retorna OK com o usuario criado"),
    @ApiResponse(responseCode = "401", description = "Erro de autenticação desta api"),
    @ApiResponse(responseCode = "403", description = "Erro de autorização desta api"),
    @ApiResponse(responseCode = "404", description = "Recurso não foi encontrado")
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioDto> created(@Valid @RequestBody UsuarioDto usuarioDto) {
        logger.info("CONTROLLER - Criar um novo usuario  {} ", usuarioDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.criarNovo(usuarioDto));
    }

    @Operation(summary = "API para alterar um usuario pelo id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Retorna OK caso o usuario tenha sido alterado"),
    @ApiResponse(responseCode = "401", description = "Erro de autenticação desta api"),
    @ApiResponse(responseCode = "403", description = "Erro de autorização desta api"),
    @ApiResponse(responseCode = "404", description = "Recurso não foi encontrado")
    })
    @Parameters(value = {@Parameter(name = "id", in = ParameterIn.PATH)})
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioDto> updated(@PathVariable(name = "id") UUID id, @Valid @RequestBody UsuarioDto usuarioDto) {
        logger.info("CONTROLLER - Atualizando um usuario  de id {} com as informacoes {} ", id, usuarioDto);
        return ResponseEntity.ok(usuarioService.atualizar(id, usuarioDto));
    }

    @Operation(summary = "API para excluir um usuario pelo id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Retorna OK para a remoção do usuario"),
    @ApiResponse(responseCode = "401", description = "Erro de autenticação desta api"),
    @ApiResponse(responseCode = "403", description = "Erro de autorização desta api"),
    @ApiResponse(responseCode = "404", description = "Recurso não foi encontrado")
    })
    @Parameters(value = {@Parameter(name = "id", in = ParameterIn.PATH)})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleted(@PathVariable(name = "id") UUID id) {
        logger.info("CONTROLLER - Excluindo um usuario com id {} ", id);
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}