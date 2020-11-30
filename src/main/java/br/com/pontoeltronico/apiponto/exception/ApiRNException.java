package br.com.pontoeltronico.apiponto.exception;

/**
 * Classe que representa as exceções negociais tratadas pela api.
 * @author allan
 *
 */
public class ApiRNException extends Exception{

	private static final long serialVersionUID = 1947299462675264420L;
	
	public ApiRNException() {
		super();
	}
	
	public ApiRNException(String msg) {
		super(msg);	
	}
	
	public ApiRNException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
