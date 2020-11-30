package br.com.pontoeltronico.apiponto.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pontoeltronico.apiponto.dto.UsuarioDto;
import br.com.pontoeltronico.apiponto.exception.ApiRNException;
import br.com.pontoeltronico.apiponto.service.UsuarioService;
/**
 * Controller responsavel por prover o endpoint para manipulacao dos dados de usuario
 * @author allan
 *
 */
@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping
	public ResponseEntity<List<UsuarioDto>> listarTodos(){		
		List<UsuarioDto> usuarios = usuarioService.findAll();
		return ResponseEntity.ok().body(usuarios);
	}
	
	@GetMapping(path = {"{id}"})
	public ResponseEntity<UsuarioDto> buscarPorId(@PathVariable("id") long id) throws ApiRNException{
		return ResponseEntity.ok().body(usuarioService.findById(id));
	}
	
	@PostMapping
	public ResponseEntity<UsuarioDto> criar(@RequestBody UsuarioDto dto){
		UsuarioDto usuario = usuarioService.save(dto);
		return ResponseEntity.ok().body(usuario);
	}

}
