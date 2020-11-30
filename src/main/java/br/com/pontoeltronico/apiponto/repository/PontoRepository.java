package br.com.pontoeltronico.apiponto.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.pontoeltronico.apiponto.models.Ponto;
/**
 * Repositorio de dados para a entidade {@link Ponto}
 * @author allan
 *
 */
public interface PontoRepository extends JpaRepository<Ponto, Long>{
	
	@Query
	Optional<List<Ponto>> findByDataAndUsuarioId(LocalDate data, Long id);
	
	List<Ponto> findByUsuarioId(Long id);
}
