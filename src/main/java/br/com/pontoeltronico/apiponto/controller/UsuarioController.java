package br.com.pontoeltronico.apiponto.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pontoeltronico.apiponto.dto.UsuarioDto;
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
		List<UsuarioDto> usuarios = usuarioService.buscaTodosUsuarios();
		return ResponseEntity.ok().body(usuarios);
	}
	
	@GetMapping(path = {"{id}"})
	public ResponseEntity<UsuarioDto> buscarPorId(@PathVariable("id") long id){		
		return usuarioService.buscaUsuario(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	public ResponseEntity<UsuarioDto> criar(@RequestBody @Valid UsuarioDto dto){
		UsuarioDto usuario = usuarioService.save(dto);
		return new ResponseEntity<UsuarioDto>(usuario, HttpStatus.CREATED);
	}

}
