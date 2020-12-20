package br.com.pontoeltronico.apiponto.strategy;

import br.com.pontoeltronico.apiponto.dto.PontoDiarioDto;

/**
 * Interface que define o comportamento das estrategias de contabilizacao de horas de um dia de trabalho.
 * 
 * @author allan
 *
 */
public interface EstrategiaContabilizaHoras {
	
	public long calculaTempoTrabalhado(PontoDiarioDto pontoDiario);
	
	public String formataTempoTrabalhado(long tempoTrabalhado);

}
