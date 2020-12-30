package br.com.pontoeltronico.apiponto.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import br.com.pontoeltronico.apiponto.dto.PontoDiarioDto;
import br.com.pontoeltronico.apiponto.dto.PontoDto;
import br.com.pontoeltronico.apiponto.models.TipoPonto;
/**
 * 
 * @author allan
 *
 */
class EstrategiaContablizaHorasLifeTest {

	private static EstrategiaContablizaHorasLife estrategia;
	
	private static PontoDto pontoEntrada;
	
	private static PontoDto pontoSaida;
	

	@BeforeAll
	public static void setUp() {
		estrategia = new EstrategiaContablizaHorasLife();
		
		pontoEntrada = new PontoDto();
		LocalDateTime entrada = LocalDateTime.of(2020, 12, 10, 8, 0);
		pontoEntrada.setHorario(entrada);
		pontoEntrada.setTipo(TipoPonto.ENTRADA);

		pontoSaida = new PontoDto();
		LocalDateTime saida = LocalDateTime.of(2020, 12, 10, 12, 0);
		pontoSaida.setHorario(saida);
		pontoSaida.setTipo(TipoPonto.SAIDA);
	}

	@Test
	public void deveCalcular240MinutosEntreDuasData() {
		long minutosTrabalhados = estrategia.calculaTempoTrabalhado(pontoEntrada, pontoSaida);
		assertEquals(240, minutosTrabalhados);
	}
	
	@Test
	public void deveExibir4HorasNoFormatoHHMM() {
		long tempoTrabalhado = estrategia.calculaTempoTrabalhado(pontoEntrada, pontoSaida);
		String tempoFormatado = estrategia.formataTempoTrabalhado(tempoTrabalhado);
		
		assertEquals("04:00", tempoFormatado);
	}
	
	@Test
	public void deveCalcular360MinutosTrabalhadosNoSabado() {
		PontoDto pontoEntradaSabado = new PontoDto();
		LocalDateTime entradaSabado = LocalDateTime.of(2020, 12, 5, 8, 0);
		pontoEntradaSabado.setHorario(entradaSabado);
		
		PontoDto pontoSaidaSabado = new PontoDto();
		LocalDateTime saidaSabado = LocalDateTime.of(2020, 12, 5, 12, 0);
		pontoSaidaSabado.setHorario(saidaSabado);
		
		long minutosTrabalhados = estrategia.calculaTempoTrabalhado(pontoEntradaSabado, pontoSaidaSabado);
		assertEquals(360, minutosTrabalhados);
	}
	
	
	@Test
	public void deveCalcular480MinutosTrabalhadosNoDomingo() {
		PontoDto pontoEntradaDomingo= new PontoDto();
		LocalDateTime entradaDomingo = LocalDateTime.of(2020, 12, 6, 8, 0);
		pontoEntradaDomingo.setHorario(entradaDomingo);
		
		PontoDto pontoSaidaDomingo = new PontoDto();
		LocalDateTime saidaDomingo = LocalDateTime.of(2020, 12, 6, 12, 0);
		pontoSaidaDomingo.setHorario(saidaDomingo);
		
		long minutosTrabalhados = estrategia.calculaTempoTrabalhado(pontoEntradaDomingo, pontoSaidaDomingo);
		assertEquals(480, minutosTrabalhados);
	}
	
	@Test
	public void deveCalcular480MinutosTrabalhadosEmPontoComEntradaAndSaida() {
		PontoDiarioDto pontoDiario = new PontoDiarioDto();
		pontoDiario.setPontos(new ArrayList<PontoDto>());
		pontoDiario.getPontos().add(pontoEntrada);
		pontoDiario.getPontos().add(pontoSaida);
		
		PontoDto pontoEntrada2 = new PontoDto();
		LocalDateTime entrada2 = LocalDateTime.of(2020, 12, 10, 14, 0);
		pontoEntrada2.setHorario(entrada2);
		pontoEntrada2.setTipo(TipoPonto.ENTRADA);
		
		PontoDto pontoSaida2 = new PontoDto();
		LocalDateTime saida2 = LocalDateTime.of(2020, 12, 10, 18, 0);
		pontoSaida2.setHorario(saida2);
		pontoSaida2.setTipo(TipoPonto.SAIDA);
		
		pontoDiario.getPontos().add(pontoEntrada2);
		pontoDiario.getPontos().add(pontoSaida2);
		
		long minutosTrabalhados = estrategia.calculaTempoTrabalhado(pontoDiario);
		assertEquals(480, minutosTrabalhados);
	}
	
	@Test
	public void deveCalcular240MinutosTrabalhadosEmPontoEmAberto() {
		PontoDiarioDto pontoDiario = new PontoDiarioDto();
		pontoDiario.setPontos(new ArrayList<PontoDto>());
		pontoDiario.getPontos().add(pontoEntrada);
		pontoDiario.getPontos().add(pontoSaida);
		
		PontoDto pontoEntrada2 = new PontoDto();
		LocalDateTime entrada2 = LocalDateTime.of(2020, 12, 10, 14, 0);
		pontoEntrada2.setHorario(entrada2);
		pontoEntrada2.setTipo(TipoPonto.ENTRADA);
		
		pontoDiario.getPontos().add(pontoEntrada2);
		
		long minutosTrabalhados = estrategia.calculaTempoTrabalhado(pontoDiario);
		assertEquals(240, minutosTrabalhados);
	}

}
