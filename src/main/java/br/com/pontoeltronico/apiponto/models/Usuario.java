package br.com.pontoeltronico.apiponto.models;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
/**
 * Classe que representa um usuario da api.
 * 
 * @author allan
 *
 */
@Entity
@Table(name = "usuario")
public class Usuario implements Serializable{
	
	private static final long serialVersionUID = -5967093217067584954L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotEmpty(message = "O campo Nome não pode ser vazio.")
	private String nome;
	
	@Column(nullable = false, unique = true)
	@NotEmpty(message = "O campo PIS não pode ser vazio.")
	private String pis;
	
	@Column(columnDefinition = "DATE")
	private LocalDate dataCadastro;
	
	public Usuario() {
		
	}

	public Usuario(long id, String nome, String pis) {
		super();
		this.id = id;
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

	public void setDataCadastro(LocalDate dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	
	@PrePersist
	protected void onCreate() {
		dataCadastro = LocalDate.now();
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nome=" + nome + ", pis=" + pis + ", dataCadastro=" + dataCadastro + "]";
	}
}
