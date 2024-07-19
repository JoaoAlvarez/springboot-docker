package com.projeto.api.service;

import com.projeto.api.domain.Usuario;
import com.projeto.api.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UsuarioServiceImplTest {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "pass";

    @Mock
    private UsuarioRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should create User successfully")
    void create_success() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setUsername(USERNAME);
        usuario.setPassword(PASSWORD);

        // Mocking behavior of userRepository
        Mockito.when(passwordEncoder.encode(Mockito.any())).thenReturn("hashedPassword");
        Mockito.when(userRepository.save(usuario)).thenReturn(usuario);

        // When
        Usuario createdUsuario = usuarioService.create(usuario);

        // Then
        assertThat(createdUsuario).isNotNull();
        assertThat(createdUsuario.getUsername()).isEqualTo(USERNAME);
        Mockito.verify(userRepository, Mockito.times(1)).save(usuario);
    }

}
