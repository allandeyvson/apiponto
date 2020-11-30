package br.com.pontoeltronico.apiponto.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Classe que agrupa todas a batidas de um dia de trabalho.
 * @author allan
 *
 */
public class PontoDiarioDto {
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate data;
	
	private String horasTrabalhadas;
	
	private List<PontoDto> pontos;

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public String getHorasTrabalhadas() {
		return horasTrabalhadas;
	}

	public void setHorasTrabalhadas(String horasTrabalhadas) {
		this.horasTrabalhadas = horasTrabalhadas;
	}

	public List<PontoDto> getPontos() {
		return pontos;
	}

	public void setPontos(List<PontoDto> pontos) {
		this.pontos = pontos;
	}
}
