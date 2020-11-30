package br.com.pontoeltronico.apiponto.dto.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que implementa um response generico para a api.
 * 
 * @author allan
 *
 */
public class Response<T> {

	private T data;

	private List<String> errors;

	public void addError(String error) {
		if (errors == null)
			errors = new ArrayList<String>();
		errors.add(error);
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
	
	
}
