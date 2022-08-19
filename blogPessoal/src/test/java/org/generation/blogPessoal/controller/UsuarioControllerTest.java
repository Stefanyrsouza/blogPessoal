package org.generation.blogPessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.generation.blogPessoal.model.Usuario;
import org.generation.blogPessoal.repository.UsuarioRepository;
import org.generation.blogPessoal.service.UsuarioService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioControllerTest {
		
		@Autowired
		private TestRestTemplate testRestTemplate;
		
		@Autowired
		private UsuarioService usuarioService;
		
		@Autowired
		private UsuarioRepository usuarioRepository;
		
		@BeforeAll
		void start () {
			
			usuarioRepository.deleteAll();
			
			usuarioService.cadastrarUsuario(new Usuario(0L, "Root", "root@root.com", "rootroot", " "));
		}
		
		@Test
		@Order(1)
		@DisplayName("Cadastrar um usuario")
		public void deveCriarUmUsuario() {
			
			HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(0L, "Paulo Antunes", "paulo_antunes@email.com.br",
					"1234567", "http://i.imgur.com/hjhjhj"));
			
			ResponseEntity<Usuario> corpoResposta = testRestTemplate
					.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao, Usuario.class);
			
			assertEquals(HttpStatus.CREATED, corpoResposta.getStatusCode());
			assertEquals(corpoRequisicao.getBody().getNome(), corpoResposta.getBody().getNome());
			assertEquals(corpoRequisicao.getBody().getUsuario(), corpoResposta.getBody().getUsuario());
		}
		
		@Test
		@DisplayName("Não deve permitir duplicação do Usuário")
		public void naoDeveDuplicarUsuario() {
			
			usuarioService.cadastrarUsuario(new Usuario(0L, "Maria da Silva", "maria_silva@email.com", "12345678",
					"https://i.imgur.com/jdiajdias.jpg"));
			
			HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(new Usuario(0L, "Maria da Silva", "maria_silva@email.com", "12345678",
					"https://i.imgur.com/jdiajdias.jpg"));
			
			ResponseEntity<Usuario> corpoResposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, corpoRequisicao,
					Usuario.class);
			
			assertEquals(HttpStatus.BAD_REQUEST, corpoResposta.getStatusCode());
		}
		
		@Test
		@DisplayName("Atualizar um Usuário")
		public void deveAtualizarUmUsuario() {
			
			Optional<Usuario> usuarioCadastrado = usuarioService.cadastrarUsuario(new Usuario(0L, "Juliana Andrews Ramos",
					"juliana_ramos@email.com","juliana123", "https://i.imgur.com/hdaushdfoa.jpg"));
			
			Usuario usuarioUpdate = new Usuario(usuarioCadastrado.get().getId(), "Juliana Andrews Ramos", "juliana_ramos@email.com",
					"juliana123", "https://i.imgur.com/hdaushdfoa.jpg");
			
			HttpEntity<Usuario> corpoRequisicao = new HttpEntity<Usuario>(usuarioUpdate);
			
			ResponseEntity<Usuario> corpoResposta = testRestTemplate.withBasicAuth("root@root.com", "rootroot").exchange("/usuarios/atualizar",
					HttpMethod.PUT, corpoRequisicao, Usuario.class);
			
			assertEquals(HttpStatus.OK, corpoResposta.getStatusCode());
			assertEquals(corpoRequisicao.getBody().getNome(), corpoResposta.getBody().getNome());
			assertEquals(corpoRequisicao.getBody().getUsuario(), corpoResposta.getBody().getUsuario());
		}
		
		@Test
		@DisplayName("Listar todos os Usuários")
		public void deveMostrarTodosUsuarios() {
			
			usuarioService.cadastrarUsuario(new Usuario(0L, "Sabrina Sanches", "sabrina_sanches@email.com", "sabrina123",
					"https://i.imgur.com/jaksaks.jpg"));
			
			usuarioService.cadastrarUsuario(new Usuario(0L, "Ricardo Marques", "ricardo_marques@email.com", "ricardo123",
					"https://i.imgur.com/dshdkjashd.jpg"));
			
			ResponseEntity<String> resposta = testRestTemplate.withBasicAuth("root@root.com", "rootroot").exchange("/usuarios/all",
					HttpMethod.GET, null, String.class);
			
			assertEquals(HttpStatus.OK, resposta.getStatusCode());
					
		}
}
