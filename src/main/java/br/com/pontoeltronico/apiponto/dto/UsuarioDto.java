package br.com.pontoeltronico.apiponto.dto;

import java.util.Date;

import br.com.pontoeltronico.apiponto.models.Usuario;

/**
 * Classe que representa um dto para a entidade {@link Usuario}
 * @author allan
 *
 */
public class UsuarioDto {
	
	private long id;
	
	private String nome;
	
	private String pis;
	
	private Date dataCdastro;

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

	public Date getDataCdastro() {
		return dataCdastro;
	}

	public void setDataCdastro(Date dataCdastro) {
		this.dataCdastro = dataCdastro;
	}
}
