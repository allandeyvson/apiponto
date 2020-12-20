package br.com.pontoeltronico.apiponto.strategy;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import br.com.pontoeltronico.apiponto.dto.PontoDiarioDto;
import br.com.pontoeltronico.apiponto.dto.PontoDto;
import br.com.pontoeltronico.apiponto.models.TipoPonto;

/**
 * Classe que contabiliza a quantidade de horas seguindo a seguinte estrategia:
 * <br>
 * 1) De segunda a sexta feira a cada 60 minutos trabalhados são contabilizados 60 minutos.
 * <br>
 * 2) Aos sábados a cada 60 minutos trabalhados são contabilizados 90 minutos.
 * <br>
 * 3) Aos domingos a cada 60 minutos trabalhados são contabilizados 120 minutos.
 * <br>
 * 4) Para trabalho realizado entre as 22:00 e 06:00 a cada 60 minutos trabalhados são contabilizados 72 minutos.
 * <br>
 * 
 * @author allan
 *
 */
public class EstrategiaContablizaHorasLife implements EstrategiaContabilizaHoras {

	@Override
	public long calculaTempoTrabalhado(PontoDiarioDto pontoDiario) {
		List<PontoDto> pontosDto = pontoDiario.getPontos();
		Long horas = 0L;

		for (int i = 0; i < pontosDto.size(); i++) {
			PontoDto pontoEntrada = pontosDto.get(i);
			if (pontoEntrada.getTipo().equals(TipoPonto.ENTRADA)) {
				try {
					PontoDto pontoSaida = pontosDto.get(i + 1);
					if (pontoSaida.getTipo().equals(TipoPonto.SAIDA)) {
						horas += calculaTempoTrabalhado(pontoEntrada, pontoSaida);
					}
				} catch (IndexOutOfBoundsException e) {
					// TODO: handle exception
				}
			}
		}
		return horas;
	}
	/**
	 * Retorna a quantidade de tempo trabalalhada em minutos.
	 * 
	 * @param pontoEntrada
	 * @param pontoSaida
	 * @return
	 */
	protected Long calculaTempoTrabalhado(PontoDto pontoEntrada, PontoDto pontoSaida) {
		Duration duration = Duration.between(pontoEntrada.getHorario(),pontoSaida.getHorario());
		Long minutosTrabalhados = TimeUnit.MINUTES.convert(duration);
		
		long minutosAux = minutosTrabalhados;		
		switch (pontoEntrada.getHorario().getDayOfWeek()) {
		case SATURDAY:
			while (minutosAux >= 60) {
				minutosTrabalhados += 30;
				minutosAux -=60;
			}		
			break;
		case SUNDAY:		
			while (minutosAux >= 60) {
				minutosTrabalhados += 60;
				minutosAux -=60;
			}			
			break;
		default:		
			break;
		}
		return minutosTrabalhados;
	}

	@Override
	public String formataTempoTrabalhado(long tempoTrabalhado) {
		return LocalTime.ofSecondOfDay(tempoTrabalhado*60).toString();		
	}

}
