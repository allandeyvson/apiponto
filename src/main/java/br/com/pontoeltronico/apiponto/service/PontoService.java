package br.com.pontoeltronico.apiponto.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

			Long horasTrabalhadas = calculaHorasTrabalhadas(pontoDiario);
			pontoDiario.setHorasTrabalhadas(LocalTime.ofSecondOfDay(horasTrabalhadas).toString());

			pontosDiarios.add(pontoDiario);
		});

		return pontosDiarios;
	}

	/**
	 * Método que calcula as horas trabalhadas em determinado período.
	 * 
	 * @param pontoDiario
	 * @return
	 */
	private Long calculaHorasTrabalhadas(PontoDiarioDto pontoDiario) {
		List<PontoDto> pontosDto = pontoDiario.getPontos();
		Long horas = 0L;

		for (int i = 0; i < pontosDto.size(); i++) {
			PontoDto pontoEntrada = pontosDto.get(i);
			if (pontoEntrada.getTipo().equals(TipoPonto.ENTRADA)) {
				try {
					PontoDto pontoSaida = pontosDto.get(i + 1);
					if (pontoSaida.getTipo().equals(TipoPonto.SAIDA)) {
						Duration duration = Duration.between(pontoEntrada.getHorario(),pontoSaida.getHorario());
						horas+=duration.getSeconds();
					}
				} catch (IndexOutOfBoundsException e) {
					// TODO: handle exception
				}
			}
		}
		return horas;
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

		if (LocalDateTime.now().minusSeconds(60).isBefore(ultimoPonto.getHorario())) {
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
