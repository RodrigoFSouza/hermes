package br.com.cronos.hermes.controllers;

import br.com.cronos.hermes.core.BaseIntegrationTest;
import br.com.cronos.hermes.dto.LancamentoDto;
import br.com.cronos.hermes.entities.Usuario;
import br.com.cronos.hermes.repositories.LancamentoRepository;
import br.com.cronos.hermes.repositories.UsuarioRepository;
import br.com.cronos.hermes.utils.DatabaseCleaner;
import br.com.cronos.hermes.utils.LancamentoConstants;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.UUID;

import static br.com.cronos.hermes.utils.LancamentoData.*;
import static br.com.cronos.hermes.utils.UsuarioData.newUsuarioBuilder;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


class LancamentoIntegrationTest extends BaseIntegrationTest {

    @Autowired
    DatabaseCleaner cleaner;
    @Autowired
    LancamentoRepository repository;

    @Autowired
    UsuarioRepository usuarioRepository;
    
    private UUID idLancamentoDtoCriado;
    private Usuario usuarioCriado;
    
    @Override
    protected void setupEach() {
        RestAssured.basePath = LancamentoConstants.BASE_URL;
        cleaner.clean("lancamentos");
        cleaner.clean("usuarios");
        
        prepareData();
    }
    
    void prepareData() {
        usuarioCriado =  usuarioRepository.save(newUsuarioBuilder().build());
        var lancamentoDto = newLancamentoBuilder().usuario(usuarioCriado).dataCadastro(LocalDateTime.now()).build();
        var lancamentoDtoSalvo = repository.save(lancamentoDto);
        idLancamentoDtoCriado = lancamentoDtoSalvo.getId();
    }

    @Test
    void deveRetornarUmLancamento_QuandoBuscarTodosOsLancamentos() {
        var dtoEsperado = newLancamentoDtoBuilder().build();
        
        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
        .when()
            .get()
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("totalPages", is(equalTo(1)))
            .body("totalElements", is(equalTo(1)))
            .body("content", hasSize(1))
            .body("content[0].mes", is(equalTo(dtoEsperado.getMes())))
            .body("content[0].ano", is(equalTo(dtoEsperado.getAno())))
            .body("content[0].usuario", notNullValue())
            .body("content[0].valor", is(equalTo(dtoEsperado.getValor().floatValue())));
    }
    
    
    @Test
    void deveRetornarStatusOk_QuandoBuscarUmLancamentoQueExiste() {
        var dtoEsperado = newLancamentoDtoBuilder().build();
        
        given()
            .pathParam("id", idLancamentoDtoCriado.toString())
            .contentType(ContentType.JSON)
        .when()
            .get("/{id}")
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("mes", is(equalTo(dtoEsperado.getMes())))
            .body("ano", is(equalTo(dtoEsperado.getAno())))
            .body("usuario", notNullValue())
            .body("valor", is(equalTo(dtoEsperado.getValor().floatValue())));
    }
    
    @Test
    void deveResponderStatus404_QuandoBuscarUmLancamentoDtoQueNaoExiste() {
        given()
            .pathParam("id", LancamentoConstants.ID_QUE_NAO_EXISTE)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
        .when()
            .get("/{id}")
        .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }
    
    @Test
    void deveRetornarStatus201_QuandoCriarUmNovoLancamento() {
        var dtoEsperado = newLancamentoDtoBuilder().usuario(usuarioCriado).build();

        var lancamentoRetornado = given()
            .body(dtoEsperado)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract().body().as(LancamentoDto.class);

        assertThat(lancamentoRetornado.getId(), notNullValue());
        assertThat(lancamentoRetornado.getMes(), is(equalTo(dtoEsperado.getMes())));
        assertThat(lancamentoRetornado.getAno(), is(equalTo(dtoEsperado.getAno())));
        assertThat(lancamentoRetornado.getUsuario(), is(equalTo(dtoEsperado.getUsuario())));
        assertThat(lancamentoRetornado.getValor(), is(equalTo(dtoEsperado.getValor())));
    }
    
    @Test
    void deveRetornarStatus400_QuandoNaoForPreenchidoAlgumCampoQueEObrigatorio() {
        var lancamentoDtoEsperado = newLancamentoDtoBuilder().build();
        
        given()
            .body(lancamentoDtoEsperado)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
    }
    
    @Test
    void deveRetornarStatusBadRequestQuandoNaoPassarUmLancamentoDtoVazio() {
        var lancamentoDto = LancamentoDto.builder().build();
        given()
            .pathParam("id", idLancamentoDtoCriado.toString())
            .body(lancamentoDto)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
        .when()
            .put("/{id}")
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }
    
    @Test
    void deveRetornar200_QuantoAtualizarUmLancamentoDto() {
        var updateLancamentoDto = updateLancamentoDtoBuilder().usuario(usuarioCriado).build();

        var lancamentoRetornado = given()
            .pathParam("id", idLancamentoDtoCriado.toString())
            .body(updateLancamentoDto)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
        .when()
            .put("/{id}")
        .then()
            .statusCode(HttpStatus.OK.value())
            .extract().body().as(LancamentoDto.class);

        assertThat(lancamentoRetornado.getId(), notNullValue());
        assertThat(lancamentoRetornado.getMes(), is(equalTo(updateLancamentoDto.getMes())));
        assertThat(lancamentoRetornado.getAno(), is(equalTo(updateLancamentoDto.getAno())));
        assertThat(lancamentoRetornado.getUsuario(), is(equalTo(updateLancamentoDto.getUsuario())));
        assertThat(lancamentoRetornado.getValor(), is(equalTo(updateLancamentoDto.getValor())));
    }

    @Test
    void deveRetornarStatus404_QuandoTentarAtualizarUmLancamentoDtoComIdQueNaoExiste() {
        var lancamentoDto = updateLancamentoDtoBuilder().build();

        given()
            .pathParam("id", LancamentoConstants.ID_QUE_NAO_EXISTE)
            .body(lancamentoDto)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
        .when()
            .put("/{id}")
        .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }
    
    @Test
    void deveRetornarStatus204_QuandoExcluirUmLancamentoDto() {
        given()
            .pathParam("id", idLancamentoDtoCriado.toString())
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
        .when()
            .delete("/{id}")
        .then()
            .statusCode(HttpStatus.NO_CONTENT.value());
    }
    
    @Test
    void deveRetornarStatus404_QuandoTentarDeletarUmIdQueNaoExiste() {
        given()
            .pathParam("id", LancamentoConstants.ID_QUE_NAO_EXISTE)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
        .when()
            .get("/{id}")
        .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }
}