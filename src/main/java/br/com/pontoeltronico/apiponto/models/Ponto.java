package br.com.pontoeltronico.apiponto.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entidade que representa um registro de ponto.
 * @author allan
 *
 */
@Entity
@Table(name = "ponto")
public class Ponto implements Serializable{
	
	private static final long serialVersionUID = 1904617206007734761L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@OrderBy
	@Column(columnDefinition = "DATE")
	private LocalDate data;
	
	@OrderBy
	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime horario;
	
	@Enumerated(EnumType.STRING)
	private TipoPonto tipo;
	
	@ManyToOne
	private Usuario usuario;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public LocalDateTime getHorario() {
		return horario;
	}

	public void setHorario(LocalDateTime horario) {
		this.horario = horario;
	}

	public TipoPonto getTipo() {
		return tipo;
	}

	public void setTipo(TipoPonto tipo) {
		this.tipo = tipo;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	@PrePersist
	protected void onCreate() {
		this.data = LocalDate.now();
		this.horario = LocalDateTime.now();
	}
	
	public boolean isEntrada() {
		return tipo.equals(TipoPonto.ENTRADA);
	}
	
	public boolean isSaida() {
		return tipo.equals(TipoPonto.SAIDA);
	}

	@Override
	public String toString() {
		return "Ponto [id=" + id + ", data=" + data + ", horario=" + horario + ", tipo=" + tipo + ", usuario=" + usuario
				+ "]";
	}
}
