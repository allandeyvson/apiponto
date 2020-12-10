package br.com.pontoeltronico.apiponto.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pontoeltronico.apiponto.dto.PontoDiarioDto;
import br.com.pontoeltronico.apiponto.dto.PontoDto;
import br.com.pontoeltronico.apiponto.dto.response.Response;
import br.com.pontoeltronico.apiponto.exception.ApiRNException;
import br.com.pontoeltronico.apiponto.service.PontoService;
import br.com.pontoeltronico.apiponto.strategy.EstrategiaContablizaHorasLife;
/**
 * Controlador responsavel por prover o acesso a registro de ponto e suas funcionalidades.
 * @author allan
 *
 */
@RestController
@RequestMapping("/api/ponto")
public class PontoController {
	
	@Autowired
	private PontoService pontoService;
	
	@GetMapping
	public ResponseEntity<List<PontoDiarioDto>> listar(@RequestHeader Long idUsuario){
		pontoService.setEstrategia(new EstrategiaContablizaHorasLife());
		List<PontoDiarioDto> pontosDiarios = pontoService.buscarPontos(idUsuario);		
		return ResponseEntity.ok().body(pontosDiarios);
	}
	
	@PostMapping
	public ResponseEntity<Response<PontoDto>> registrar(@RequestHeader Long idUsuario) throws ApiRNException{
		Response<PontoDto> response = new Response<PontoDto>();
		
		PontoDto dto = pontoService.registrarPonto(idUsuario);
		response.setData(dto);
		
		return ResponseEntity.ok().body(response);
	}
}
