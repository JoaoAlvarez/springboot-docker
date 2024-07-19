package com.projeto.api.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.projeto.api.domain.Role;
import com.projeto.api.web.rest.validation.InserirUsuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
@InserirUsuario
public class NovoUsuarioDTO {
    protected static final String SEQUENCIAL = "usuario_id_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCIAL)
    @SequenceGenerator(name = SEQUENCIAL, allocationSize = 1)
    private Long id;

    @NotNull(message="Preenchimento obrigatório")
    private String username;

    @NotNull(message="Preenchimento obrigatório")
    private String password;

    @NotEmpty(message = "Inseira ao menos uma role para o usuário")
    private List<Role> roles;
}
