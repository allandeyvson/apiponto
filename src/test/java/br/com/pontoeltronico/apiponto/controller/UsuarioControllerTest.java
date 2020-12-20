package br.com.pontoeltronico.apiponto.controller;

import java.util.Base64;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.pontoeltronico.apiponto.dto.UsuarioDto;
import br.com.pontoeltronico.apiponto.models.Usuario;
import br.com.pontoeltronico.apiponto.repository.UsuarioRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UsuarioControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Value(value = "${spring.security.user.name}")
	private String userName;

	@Value(value = "${spring.security.user.password}")
	private String password;

	private HttpEntity<String> request;

	@BeforeEach
	public void setUp() {
		HttpHeaders hearder = new HttpHeaders();
		String auth = new String(userName.concat(":").concat(password));
		byte[] authBytes = auth.getBytes();
		String authBase64 = Base64.getEncoder().encodeToString(authBytes);
		hearder.add("Authorization", "Basic ".concat(authBase64));
		request = new HttpEntity<String>(hearder);

	}

	@AfterEach
	public void end() {
		usuarioRepository.deleteAll();
	}

	@Test
	public void buscaTodosUsuariosDeveRetornar02UsuariosAndStatus200() {
		usuarioRepository.save(new Usuario(0, "Usuario 01", "123456789"));
		usuarioRepository.save(new Usuario(0, "Usuario 02", "987654321"));

		ParameterizedTypeReference<List<UsuarioDto>> tipoRetorno = new ParameterizedTypeReference<List<UsuarioDto>>() {
		};

		ResponseEntity<List<UsuarioDto>> resposta = testRestTemplate.exchange("/api/usuario/", HttpMethod.GET, request,
				tipoRetorno);

		Assertions.assertEquals(2, resposta.getBody().size());
		Assertions.assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}

	@Test
	public void buscaUsuariodeveRetornar01UsuarioAndStatus200() {
		Usuario usuario = usuarioRepository.save(new Usuario(0, "Usuario 03", "1234567890"));
		ParameterizedTypeReference<UsuarioDto> tipoRetorno = new ParameterizedTypeReference<UsuarioDto>() {
		};

		ResponseEntity<UsuarioDto> resposta = testRestTemplate.exchange("/api/usuario/{id}", HttpMethod.GET, request,
				tipoRetorno, usuario.getId());
		
		Assertions.assertEquals(resposta.getBody().getId(), usuario.getId());
		Assertions.assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}
	
	@Test
	public void buscaUsuarioDeveRetornarStatus404() {
		ParameterizedTypeReference<UsuarioDto> tipoRetorno = new ParameterizedTypeReference<UsuarioDto>() {
		};

		ResponseEntity<UsuarioDto> resposta = testRestTemplate.exchange("/api/usuario/{id}", HttpMethod.GET, request,
				tipoRetorno, 100);
		
		Assertions.assertNull(resposta.getBody());
		Assertions.assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
	}
}
