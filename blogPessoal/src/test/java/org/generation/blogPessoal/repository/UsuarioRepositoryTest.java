package org.generation.blogPessoal.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.List;

import org.generation.blogPessoal.model.Usuario;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioRepositoryTest {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@BeforeAll
	void start() {
		
		usuarioRepository.deleteAll();
		
		usuarioRepository.save(new Usuario(0L, "João da Silva", "joao@email.com.br", "13465278", "https://i.imgur.com/FETvs20.jpg"));
		
		usuarioRepository.save(new Usuario(0L, "Leila da Silva", "leila@email.com.br", "13465278", "https://i.imgur.com/Ntygneo.jpg"));
		
		usuarioRepository.save(new Usuario(0L, "oi da terra", "oi@email.com.br", "13465278", "https://i.imgur.com/ksjajsa.jpg"));
		
		usuarioRepository.save(new Usuario(0L, "ele da silva", "ele@email.com.br", "13465278", "https://i.imgur.com/ksjaksjka.jpg"));

	}
	
	@Test
	@DisplayName("Retornar 1 usuario")
	public void deveRetornarUmUsuario() {
			
		Optional<Usuario> usuario = usuarioRepository.findByUsuario("joao@email.com.br");
		assertTrue(usuario.get().getUsuario().equals("joao@email.com.br"));
}
	
	@Test
	@DisplayName("Retornar 3 usuarios")
	public void deveRetornarTresUsuarios() {
		
		List <Usuario> listaDeUsuarios = usuarioRepository.findAllByNomeContainingIgnoreCase("Silva");
		assertEquals(3, listaDeUsuarios.size());
		assertTrue(listaDeUsuarios.get(0).getNome().equals("João da Silva"));
		assertTrue(listaDeUsuarios.get(1).getNome().equals("Leila da Silva"));
		assertTrue(listaDeUsuarios.get(2).getNome().equals("ele da Silva"));

	}
	
	public void end() {
		usuarioRepository.deleteAll();
	}
}
