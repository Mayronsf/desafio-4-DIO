package br.com.dio.padroes.web;

import br.com.dio.padroes.exception.RecursoNaoEncontradoException;
import br.com.dio.padroes.exception.RegraNegocioException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

@RestControllerAdvice
public class TratadorDeErros {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    ProblemDetail naoEncontrado(RecursoNaoEncontradoException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(RegraNegocioException.class)
    ProblemDetail regraNegocio(RegraNegocioException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ProblemDetail conflito() {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "O registro está em uso e não pode ser alterado");
    }

    @ExceptionHandler(RestClientException.class)
    ProblemDetail viaCepIndisponivel() {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_GATEWAY, "Não foi possível consultar o ViaCEP");
    }
}
