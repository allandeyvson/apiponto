package br.com.pontoeltronico.apiponto.dto;

import java.time.LocalDate;
import java.util.Date;

import javax.validation.constraints.NotEmpty;

import br.com.pontoeltronico.apiponto.models.Usuario;

/**
 * Classe que representa um dto para a entidade {@link Usuario}
 * @author allan
 *
 */
public class UsuarioDto {
	
	private long id;
	
	@NotEmpty(message = "O campo Nome não pode ser vazio.")
	private String nome;
	
	@NotEmpty(message = "O campo PIS não pode ser vazio.")
	private String pis;
	
	private LocalDate dataCadastro;
	
	public UsuarioDto() {
		
	}

	public UsuarioDto(String nome, String pis) {
		super();
		this.nome = nome;
		this.pis = pis;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getPis() {
		return pis;
	}

	public void setPis(String pis) {
		this.pis = pis;
	}

	public LocalDate getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(LocalDate dataCdastro) {
		this.dataCadastro = dataCdastro;
	}
}
