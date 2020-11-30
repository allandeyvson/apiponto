package br.com.pontoeltronico.apiponto.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.pontoeltronico.apiponto.models.Ponto;
import br.com.pontoeltronico.apiponto.models.TipoPonto;

/**
 * Classe que representa um dto para a entidade {@link Ponto}
 * @author allan
 *
 */
public class PontoDto {
	
	@JsonFormat(pattern = "HH:mm:ss")
	private LocalDateTime horario;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate data;
	
	private TipoPonto tipo;

	public LocalDateTime getHorario() {
		return horario;
	}

	public void setHorario(LocalDateTime horario) {
		this.horario = horario;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public TipoPonto getTipo() {
		return tipo;
	}

	public void setTipo(TipoPonto tipo) {
		this.tipo = tipo;
	}
}
