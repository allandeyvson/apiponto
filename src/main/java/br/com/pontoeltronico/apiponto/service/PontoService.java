package br.com.pontoeltronico.apiponto.service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.pontoeltronico.apiponto.dto.PontoDiarioDto;
import br.com.pontoeltronico.apiponto.dto.PontoDto;
import br.com.pontoeltronico.apiponto.exception.ApiRNException;
import br.com.pontoeltronico.apiponto.models.Ponto;
import br.com.pontoeltronico.apiponto.models.TipoPonto;
import br.com.pontoeltronico.apiponto.models.Usuario;
import br.com.pontoeltronico.apiponto.repository.PontoRepository;
import br.com.pontoeltronico.apiponto.repository.UsuarioRepository;

/**
 * Servico para prover a metodos de manipulacao de dados referentes aos
 * registros de pontos.
 * 
 * @author allan
 *
 */
@Service
public class PontoService {

	@Autowired
	private PontoRepository pontoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	private static Logger logger = LoggerFactory.getLogger(PontoService.class);
	
	/**
	 * Limite de intervalo para registro de ponto (dada em segundos)
	 */
	private static final int LIMITE_DE_TEMPO_REGISTRO = 60;

	/**
	 * Retorna uma lista de pontos contendo informacoes sobre a quantidade de horas
	 * trabalhadas.
	 * 
	 * @param idU
	 * @return
	 */
	public List<PontoDiarioDto> buscarPontos(Long id) {
		List<PontoDiarioDto> pontosDiarios = new ArrayList<PontoDiarioDto>();

		Map<LocalDate, List<Ponto>> mapaDiaPonto = pontoRepository.findByUsuarioId(id).stream()
				.collect(Collectors.groupingBy(Ponto::getData));

		agrupaMontaPontosDiarios(pontosDiarios, mapaDiaPonto);

		return pontosDiarios;
	}
	
	/**
	 * Método responsavel pela estruturacao dos dados exibidos ao usuario.
	 * @param pontosDiarios
	 * @param mapaDiaPonto
	 */
	private void agrupaMontaPontosDiarios(List<PontoDiarioDto> pontosDiarios,
			Map<LocalDate, List<Ponto>> mapaDiaPonto) {
		mapaDiaPonto.entrySet().forEach(item -> {
			PontoDiarioDto pontoDiario = new PontoDiarioDto();

			pontoDiario.setData(item.getKey());
			pontoDiario.setPontos(item.getValue().stream().map(itemPonto -> {
				PontoDto itemDto = new PontoDto();
				itemDto.setHorario(itemPonto.getHorario());
				itemDto.setData(itemPonto.getData());
				itemDto.setTipo(itemPonto.getTipo());
				return itemDto;
			}).collect(Collectors.toList()));

			Long tempoTrabalhado = calculaTempoTrabalhado(pontoDiario);
			pontoDiario.setHorasTrabalhadas(LocalTime.ofSecondOfDay(tempoTrabalhado).toString());

			pontosDiarios.add(pontoDiario);
		});
	}

	/**
	 * Método que calcula as horas trabalhadas em determinado período.
	 * 
	 * @param pontoDiario
	 * @return
	 */
	private Long calculaTempoTrabalhado(PontoDiarioDto pontoDiario) {
		List<PontoDto> pontosDto = pontoDiario.getPontos();
		Long horas = 0L;

		for (int i = 0; i < pontosDto.size(); i++) {
			PontoDto pontoEntrada = pontosDto.get(i);
			if (pontoEntrada.getTipo().equals(TipoPonto.ENTRADA)) {
				try {
					PontoDto pontoSaida = pontosDto.get(i + 1);
					if (pontoSaida.getTipo().equals(TipoPonto.SAIDA)) {
						horas += calcularTempoProporcinal(pontoEntrada, pontoSaida);
					}
				} catch (IndexOutOfBoundsException e) {
					// TODO: handle exception
				}
			}
		}
		return horas;
	}
	
	/**
	 * Calcula a quantidade de horas trabalhadas de acordo com regra:
	  	1) De segunda a sexta feira a cada 60 minutos trabalhados são contabilizados 60 minutos.
		2) Aos sábados a cada 60 minutos trabalhados são contabilizados 90 minutos.
		3) Aos domingos a cada 60 minutos trabalhados são contabilizados 120 minutos.
		4) Para trabalho realizado entre as 22:00 e 06:00 a cada 60 minutos trabalhados são contabilizados 72 minutos.
	 * @param pontoEntrada
	 * @param pontoSaida
	 * @return
	 */
	private Long calcularTempoProporcinal(PontoDto pontoEntrada, PontoDto pontoSaida) {
		Duration duration = Duration.between(pontoEntrada.getHorario(),pontoSaida.getHorario());
		Long segundosTrabalhados = duration.getSeconds();		
		switch (pontoEntrada.getHorario().getDayOfWeek()) {
		case SATURDAY:
			//horas += 0;
			break;
		case MONDAY:
			//horas +=0;
			break;
		default:			
			break;
		}
		return segundosTrabalhados;
	}

	/**
	 * Registra o ponto do usuario repassado.
	 * 
	 * @param id id do usuario
	 * @throws ApiRNException
	 */
	public PontoDto registrarPonto(Long id) throws ApiRNException {
		Optional<Usuario> usuario = usuarioRepository.findById(id);
		if (usuario.isEmpty()) {
			throw new ApiRNException("Usuario não cadastrado na api.");
		}

		Ponto ponto = new Ponto();
		ponto.setUsuario(usuario.get());

		Optional<List<Ponto>> pontosHoje = pontoRepository.findByDataAndUsuarioId(LocalDate.now(), id);
		if (pontosHoje.isPresent()) {
			Ponto ultimoPonto = pontosHoje.get().get(pontosHoje.get().size() - 1);

			verficaTempoMinimoRegistro(ultimoPonto);

			if (ultimoPonto.isEntrada())
				ponto.setTipo(TipoPonto.SAIDA);
			else
				ponto.setTipo(TipoPonto.ENTRADA);
		} else {
			ponto.setTipo(TipoPonto.ENTRADA);
		}

		ponto = pontoRepository.save(ponto);
		return modelToDto(ponto);
	}

	/**
	 * Método que verifica se a tentativa de batida ocorre com menos de 1min de
	 * intervalo.
	 * 
	 * @param ultimoPonto
	 * @throws ApiRNException
	 */
	private void verficaTempoMinimoRegistro(Ponto ultimoPonto) throws ApiRNException {

		if (LocalDateTime.now().minusSeconds(LIMITE_DE_TEMPO_REGISTRO).isBefore(ultimoPonto.getHorario())) {
			throw new ApiRNException("Nao é permitido registrar batida de ponto com menos de 1min de intervalo.");
		}

	}

	private PontoDto modelToDto(Ponto model) {
		PontoDto dto = new PontoDto();
		dto.setData(model.getData());
		dto.setHorario(model.getHorario());
		dto.setTipo(model.getTipo());
		return dto;
	}

}
