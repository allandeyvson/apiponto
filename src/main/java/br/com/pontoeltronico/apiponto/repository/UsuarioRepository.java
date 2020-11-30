package br.com.pontoeltronico.apiponto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pontoeltronico.apiponto.models.Usuario;
/**
 * Repositorio de dados para a entidade {@link Usuario}
 * @author allan
 *
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

}
