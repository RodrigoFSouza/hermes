package br.com.cronos.hermes.controllers;

import br.com.cronos.hermes.core.BaseIntegrationTest;
import br.com.cronos.hermes.entities.Usuario;
import br.com.cronos.hermes.dto.UsuarioDto;
import br.com.cronos.hermes.repositories.UsuarioRepository;
import br.com.cronos.hermes.utils.DatabaseCleaner;
import br.com.cronos.hermes.utils.UsuarioData;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static br.com.cronos.hermes.utils.UsuarioConstants.BASE_URL;
import static br.com.cronos.hermes.utils.UsuarioConstants.ID_QUE_NAO_EXISTE;
import static br.com.cronos.hermes.utils.UsuarioData.newBuilder;
import static br.com.cronos.hermes.utils.UsuarioData.updateDtoBuilder;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class UsuarioIntegrationTest extends BaseIntegrationTest {

    @Autowired
    DatabaseCleaner cleaner;
    @Autowired
    UsuarioRepository repository;
    
    private UUID idUsuarioCriado;
    
    @Override
    protected void setupEach() {
        RestAssured.basePath = BASE_URL;
        cleaner.clean("lancamentos");
        cleaner.clean("usuarios");
        
        prepareData();
    }
    
    void prepareData() {
        var usuario = newBuilder().build();
        var usuarioSalvo = repository.save(usuario);
        idUsuarioCriado = usuarioSalvo.getId();
    }
    
    @Test
    void deveRetornarStatus200_QuandobuscarTodosOsUsuarios() {
        given()
            .accept(ContentType.JSON)
        .when()
            .get()
        .then()
            .statusCode(HttpStatus.OK.value());
    }
    
    @Test
    void deveRetornarUmUsuario_QuandoBuscarTodosOsUsuarios() {
        var usuarioEsperado = UsuarioData.newDtoBuilder().build();
        
        given()
            .accept(ContentType.JSON)
        .when()
            .get()
        .then()
            .body("totalPages", is(equalTo(1)))
            .body("totalElements", is(equalTo(1)))
            .body("content", hasSize(1))
            .body("content[0].nome", is(equalTo(usuarioEsperado.getNome())))
            .body("content[0].email", is(equalTo(usuarioEsperado.getEmail())))
            .body("content[0].senha", is(equalTo(usuarioEsperado.getSenha())));
    }
    
    
    @Test
    void deveRetornarStatusOk_QuandoBuscarUmUsuarioQueExiste() {
        var usuarioEsperado = UsuarioData.newDtoBuilder().build();
        
        given()
            .pathParam("id", idUsuarioCriado.toString())
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
        .when()
            .get("/{id}")
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("nome", is(equalTo(usuarioEsperado.getNome())))
            .body("email", is(equalTo(usuarioEsperado.getEmail())))
            .body("senha", is(equalTo(usuarioEsperado.getSenha())));
    }
    
    @Test
    void deveResponderStatus404_QuandoBuscarUmUsuarioQueNaoExiste() {
        given()
            .pathParam("id", ID_QUE_NAO_EXISTE)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
        .when()
            .get("/{id}")
        .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }
    
    @Test
    void deveRetornarStatus201_QuandoCriarUmNovoUsuario() {
        var usuarioEsperado = UsuarioData.newDtoBuilder().build();

        var usuarioRetornado = given()
            .body(usuarioEsperado)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.CREATED.value())
            .extract().body().as(Usuario.class);

        assertThat(usuarioRetornado.getId(), notNullValue());
            assertThat(usuarioRetornado.getNome(), is(equalTo(usuarioEsperado.getNome())));
            assertThat(usuarioRetornado.getEmail(), is(equalTo(usuarioEsperado.getEmail())));
            assertThat(usuarioRetornado.getSenha(), is(equalTo(usuarioEsperado.getSenha())));
    }
    
    @Test
    void deveRetornarStatus400_QuandoNaoForPreenchidoAlgumCampoQueEObrigatorio() {
        var usuarioEsperado = UsuarioData.newDtoBuilder().build();
        
        given()
            .body(usuarioEsperado)
        .when()
            .post()
        .then()
            .statusCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
    }
    
    @Test
    void deveRetornarStatusBadRequestQuandoNaoPassarUmUsuarioVazio() {
        var usuarioDto = UsuarioDto.builder().build();
        given()
            .pathParam("id", idUsuarioCriado.toString())
            .body(usuarioDto)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
        .when()
            .put("/{id}")
        .then()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }
    
    @Test
    void deveRetornar200_QuantoAtualizarUmUsuario() {
        var updateUsuario = UsuarioData.updateDtoBuilder().build();

        var usuarioRetornado = given()
            .pathParam("id", idUsuarioCriado.toString())
            .body(updateUsuario)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
        .when()
            .put("/{id}")
        .then()
            .statusCode(HttpStatus.OK.value())
            .extract().body().as(Usuario.class);

        assertThat(usuarioRetornado.getId(), notNullValue());
            assertThat(usuarioRetornado.getNome(), is(equalTo(updateUsuario.getNome())));
            assertThat(usuarioRetornado.getEmail(), is(equalTo(updateUsuario.getEmail())));
            assertThat(usuarioRetornado.getSenha(), is(equalTo(updateUsuario.getSenha())));
    }

    @Test
    void deveRetornarStatus404_QuandoTentarAtualizarUmUsuarioComIdQueNaoExiste() {
        var updateUsuario = updateDtoBuilder().build();

        given()
            .pathParam("id", ID_QUE_NAO_EXISTE)
            .body(updateUsuario)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
        .when()
            .put("/{id}")
        .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }
    
    @Test
    void deveRetornarStatus204_QuandoExcluirUmUsuario() {
        given()
            .pathParam("id", idUsuarioCriado.toString())
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
            .pathParam("id", ID_QUE_NAO_EXISTE)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
        .when()
            .get("/{id}")
        .then()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }
}