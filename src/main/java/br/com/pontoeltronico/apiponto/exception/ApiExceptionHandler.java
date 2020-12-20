package br.com.pontoeltronico.apiponto.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
	protected ResponseEntity<List<String>> handleApiRNException(ApiRNException exception) {
		List<String> errors = new ArrayList<String>();
		
		errors.add(exception.getLocalizedMessage());
		
		return ResponseEntity.badRequest().body(errors);
	}
	
	/**
	 * Captura as execções de validação da api.
	 *  
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(value = {MethodArgumentNotValidException.class})
	protected ResponseEntity<List<String>> handleValidationException(MethodArgumentNotValidException exception) {
		List<String> errors = exception.getAllErrors().stream()
				.map(ObjectError::getDefaultMessage)
				.collect(Collectors.toList());
		
		return ResponseEntity.badRequest().body(errors);
	}
}
