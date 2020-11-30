package br.com.pontoeltronico.apiponto.exception;

import java.lang.reflect.InvocationTargetException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.pontoeltronico.apiponto.dto.response.Response;

/**
 * Handle utilizado para capturar e formatar as exceções da api.
 * @author allan
 *
 */
@ControllerAdvice
public class ApiExceptionHandler<T> {
	
	/**
	 * Captura e trata as exceções referentes as RN da api.
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(value = {ApiRNException.class})
	protected ResponseEntity<Response<T>> handleApiRNException(ApiRNException exception) {
		Response<T> response = new Response<T>();
		
		response.addError(exception.getLocalizedMessage());
		
		return ResponseEntity.badRequest().body(response);
	}
}
