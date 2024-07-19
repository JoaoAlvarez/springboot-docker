package com.projeto.api.web.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projeto.api.domain.Role;
import com.projeto.api.domain.Usuario;
import com.projeto.api.repository.UsuarioRepository;
import com.projeto.api.service.UsuarioService;
import com.projeto.api.web.rest.UsuarioController;
import com.projeto.api.web.rest.dto.NovoUsuarioDTO;
import com.projeto.api.web.rest.validation.InserirUsuarioValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Collections;
import java.util.List;

@WebMvcTest(controllers = UsuarioController.class)
@Import({InserirUsuarioValidator.class})
public class UsuarioControllerTest {

    private static final Role ROLE = new Role(1L, "teste");
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @Autowired
    private LocalValidatorFactoryBean validator;

    @InjectMocks
    private UsuarioController usuarioController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).setValidator(validator).build();

        Usuario existentUsuario = new Usuario();
        existentUsuario.setUsername("existinguser");
        when(usuarioRepository.findByUsername("existinguser")).thenReturn(existentUsuario);
    }

    @Test
    public void testCreateSuccess() throws Exception {
        NovoUsuarioDTO novoUsuarioDTO = new NovoUsuarioDTO();
        novoUsuarioDTO.setUsername("testuser");
        novoUsuarioDTO.setPassword("password");
        novoUsuarioDTO.setRoles(List.of(ROLE));

        // Simulate a successful user creation
        mockMvc.perform(post("/api/v1/usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"testuser\", \"password\": \"password\", \"roles\": [{\"id\": 1}]}"))
                .andExpect(status().isNoContent());

        verify(usuarioService).create(any(Usuario.class));
    }

    @Test
    public void testCreateUserAlreadyExists() throws Exception {
        NovoUsuarioDTO novoUsuarioDTO = new NovoUsuarioDTO();
        novoUsuarioDTO.setUsername("existinguser");
        novoUsuarioDTO.setPassword("password");
        novoUsuarioDTO.setRoles(List.of(ROLE));

        mockMvc.perform(post("/api/v1/usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"existinguser\", \"password\": \"password\", \"roles\": [{\"id\": 1}]}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateValidationError() throws Exception {
        // Create a NovoUsuarioDTO with an existing username to trigger validation error
        NovoUsuarioDTO novoUsuarioDTO = new NovoUsuarioDTO();
        novoUsuarioDTO.setUsername("existinguser");
        novoUsuarioDTO.setPassword("password");
        novoUsuarioDTO.setRoles(Collections.singletonList(ROLE));

        ObjectMapper objectMapper = new ObjectMapper();

        // Simulate a validation error
        mockMvc.perform(post("/api/v1/usuario")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoUsuarioDTO)))
                .andExpect(status().isBadRequest());
    }
}
