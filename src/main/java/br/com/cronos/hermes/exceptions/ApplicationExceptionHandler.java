package br.com.cronos.hermes.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Object> unauthorized(UnauthorizedException exception) {
        log.error("SEM AUTORIZACAO {}", exception.getMessage());
        return buildResponseEntity(HttpStatus.UNAUTHORIZED, exception.getMessage(), List.of("Sem Autorização"));
    }

    @ExceptionHandler(BusinessNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> notFound(BusinessNotFoundException exception) {
        log.error("Recurso não encontrado {}", exception.getMessage());
        return buildResponseEntity(HttpStatus.NOT_FOUND, exception.getMessage(), List.of("Recurso não foi encontrado"));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("Argumentos da requisição com erros {}", exception.getMessage());
        List<String> errors = new ArrayList<>();
        exception.getBindingResult().getFieldErrors().forEach(fieldError -> errors.add("Field " + fieldError.getField().toUpperCase() + " " + fieldError.getDefaultMessage()));
        exception.getBindingResult().getGlobalErrors().forEach(globalError -> errors.add("Object " + globalError.getObjectName() + " "  + globalError.getDefaultMessage()));
        return buildResponseEntity(HttpStatus.BAD_REQUEST, "Os argumento(s) informado(s) contém erros de validação", errors);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error("Malformed JSON body and/or field error {}", ex.getMessage());
        return buildResponseEntity(HttpStatus.BAD_REQUEST, "O JSON informado contém erros", List.of("JSON malformatado ou campos do body com erros"));
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected ResponseEntity<Object> handleInvalidJwtAuthenticationException(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("Sem permissao de acesso para este recurso  {}", ex.getMessage());
        return buildResponseEntity(HttpStatus.FORBIDDEN, "Sem permissao de acesso para este recurso", List.of("Sem permissao de acesso para este recurso"));
    }

    private ResponseEntity<Object> buildResponseEntity(HttpStatus httpStatus, String message, List<String> errors) {
        var errorResponse = ErrorResponse.builder()
                .code(httpStatus.value())
                .status(httpStatus.getReasonPhrase())
                .message(message)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}