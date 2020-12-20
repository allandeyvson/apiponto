package br.com.pontoeltronico.apiponto.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

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

import br.com.pontoeltronico.apiponto.models.Usuario;
import br.com.pontoeltronico.apiponto.repository.UsuarioRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class PontoControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Value(value = "${spring.security.user.name}")
	private String userName;

	@Value(value = "${spring.security.user.password}")
	private String password;

	private HttpEntity<String> requestEntity;

	private HttpHeaders hearder;

	private Usuario usuario;

	@BeforeEach
	public void setUp() {
		usuario = usuarioRepository.save(new Usuario(0, "Usuario 01", "123456789"));
		
		hearder = new HttpHeaders();
		hearder.setBasicAuth(userName, password);
		hearder.set("idUsuario", String.valueOf(usuario.getId()));		
		requestEntity = new HttpEntity<String>(hearder);
	}

	@Test
	public void naoDeveRegistrarPontoDeUsuarioNaoCadastradoAndDeveRetornarStatus400() {
		
		hearder.clearContentHeaders();
		hearder.setBasicAuth(userName, password);
		hearder.set("idUsuario", String.valueOf(usuario.getId()+1));		
		
		ResponseEntity<List<String>> resposta = testRestTemplate.exchange("/api/ponto/", HttpMethod.POST, requestEntity, 
				new ParameterizedTypeReference<List<String>>(){});
		
		assertTrue(resposta.getBody().contains("Usuario não cadastrado na api."));		
		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
	}
	
	public void naoDeveRegistrarDentroDoIntervaloMinimoAndDeveRetornarStatus400() {
		testRestTemplate.exchange("/api/ponto/", HttpMethod.POST, requestEntity, 
				new ParameterizedTypeReference<List<String>>(){});
		
		
		ResponseEntity<List<String>> resposta = testRestTemplate.exchange("/api/ponto/", HttpMethod.POST, requestEntity, 
				new ParameterizedTypeReference<List<String>>(){});
		
		assertTrue(resposta.getBody().contains("Nao é permitido registrar batida de ponto com menos de 1min de intervalo."));
		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
	}
}
