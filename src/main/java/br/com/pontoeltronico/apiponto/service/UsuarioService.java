package br.com.pontoeltronico.apiponto.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.pontoeltronico.apiponto.dto.UsuarioDto;
import br.com.pontoeltronico.apiponto.exception.ApiRNException;
import br.com.pontoeltronico.apiponto.models.Usuario;
import br.com.pontoeltronico.apiponto.repository.UsuarioRepository;

/**
 * Classe de servico para prover metodos de manipulacao de dados referentes a usuarios.
 * @author allan
 *
 */
@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	/**
	 * Converte um objeto do tipo {@link UsuarioDto} em seu respectivo modelo.
	 * @param dto
	 * @return
	 */
	private Usuario dtoToModel(UsuarioDto dto) {
		Usuario usuario = new Usuario();
		usuario.setId(dto.getId());
		usuario.setNome(dto.getNome());
		usuario.setPis(dto.getPis());		
		return usuario;
	}
	
	/**
	 * Converte um objeto do tipo {@link Usuario} em seu respectivo DTO.
	 * @param model
	 * @return
	 */
	private UsuarioDto modelToDto(Usuario model) {
		UsuarioDto usuario = new UsuarioDto();
		usuario.setId(model.getId());
		usuario.setNome(model.getNome());
		usuario.setPis(model.getPis());
		usuario.setDataCdastro(model.getDataCadastro());
		return usuario;
	}
	
	public UsuarioDto save(UsuarioDto dto) {
		Usuario usuario = usuarioRepository.save(dtoToModel(dto));
		return modelToDto(usuario);
	}
	
	public UsuarioDto findById(long id) throws ApiRNException {
		Optional<Usuario> model = usuarioRepository.findById(id);
		if (model.isPresent()) {
			return modelToDto(model.get());
		} else {
			throw new ApiRNException("Usuario não cadastrado na api.");
		}
	}
	
	public List<UsuarioDto> findAll(){
		List<Usuario> usuarios = usuarioRepository.findAll();
		return usuarios.stream().map(u -> modelToDto(u)).collect(Collectors.toList());
	}
}
