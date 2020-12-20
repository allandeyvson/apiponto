package br.com.pontoeltronico.apiponto.controller;

import java.time.LocalDate;
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

	private HttpHeaders hearder;

	@BeforeEach
	public void setUp() {
		hearder = new HttpHeaders();
		hearder.setBasicAuth(userName, password);		
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
		ParameterizedTypeReference<UsuarioDto> tipoRetorno = new ParameterizedTypeReference<UsuarioDto>() {};
		Integer idUsuarioBuscado = 100;
		
		ResponseEntity<UsuarioDto> resposta = testRestTemplate.exchange("/api/usuario/{id}", HttpMethod.GET, request,
				tipoRetorno, idUsuarioBuscado);
		
		Assertions.assertNull(resposta.getBody());
		Assertions.assertEquals(HttpStatus.NOT_FOUND, resposta.getStatusCode());
	}
	
	@Test
	public void criarUsuarioDeveRetornarStatus201() {
		UsuarioDto usuarioDto = new UsuarioDto("Usuario 01", "123456789");
		HttpEntity<UsuarioDto> httpEntity = new HttpEntity<UsuarioDto>(usuarioDto, hearder);
		
		ResponseEntity<UsuarioDto> resposta = testRestTemplate.postForEntity("/api/usuario/", httpEntity, UsuarioDto.class);
		UsuarioDto respostaUsuario = resposta.getBody();
		
		Assertions.assertNotNull(respostaUsuario.getId());
		Assertions.assertEquals(usuarioDto.getNome(), respostaUsuario.getNome());
		Assertions.assertEquals(usuarioDto.getPis(), respostaUsuario.getPis());
		Assertions.assertTrue(respostaUsuario.getDataCadastro().equals(LocalDate.now()));
		Assertions.assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
	}
	
	@Test
	public void criarUsuarioDeveRetornarMsgDeErroAndStatus201() {
		UsuarioDto usuarioDto = new UsuarioDto(null, null);
		HttpEntity<UsuarioDto> httpEntity = new HttpEntity<UsuarioDto>(usuarioDto, hearder);
		
		ResponseEntity<List<String>> resposta = testRestTemplate.exchange("/api/usuario/", HttpMethod.POST, httpEntity, 
				new ParameterizedTypeReference<List<String>>(){});	
		
		Assertions.assertTrue(resposta.getBody().contains("O campo Nome não pode ser vazio."));
		Assertions.assertTrue(resposta.getBody().contains("O campo PIS não pode ser vazio."));		
		Assertions.assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
	}
}
