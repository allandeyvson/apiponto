package br.com.pontoeltronico.apiponto.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import br.com.pontoeltronico.apiponto.models.Usuario;

@DataJpaTest
class UsuarioRepositoryTest {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private TestEntityManager testEntityMan;

	private Usuario usuario;

	@BeforeEach
	public void start() {
		usuario = new Usuario(0, "Allan", "123456789");
	}

	@Test
	public void saveComNomeNuloDeveLancarException() {
		usuario.setNome(null);
		ConstraintViolationException exception = Assertions.assertThrows(ConstraintViolationException.class, () -> {
			usuarioRepository.save(usuario);
			testEntityMan.flush();
		});
		
		assertEquals("O campo Nome não pode ser vazio.", exception.getConstraintViolations().iterator().next().getMessage());
	}
	
	@Test
	public void saveComPisNuloDeveLancarException() {
		usuario.setPis(null);
		ConstraintViolationException exception = Assertions.assertThrows(ConstraintViolationException.class, () -> {
			usuarioRepository.save(usuario);
			testEntityMan.flush();
		});
		
		assertEquals("O campo PIS não pode ser vazio.", exception.getConstraintViolations().iterator().next().getMessage());
	}
	
	@Test
	public void saveComPisDuplicadoDeveLancarException() {
		PersistenceException excepetion = Assertions.assertThrows(PersistenceException.class, () -> {
			usuarioRepository.save(new Usuario(0, "Usuario 01", "123456789"));
			usuarioRepository.save(new Usuario(0, "Usuario 02", "123456789"));
			testEntityMan.flush();
		});
				
		assertTrue(excepetion.getCause() instanceof org.hibernate.exception.ConstraintViolationException);
	}
}
