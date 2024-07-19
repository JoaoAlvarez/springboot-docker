package com.projeto.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    protected static final String SEQUENCIAL = "usuario_id_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCIAL)
    @SequenceGenerator(name = SEQUENCIAL, allocationSize = 1)
    private Long id;
    private String username;
    private String password;

    @ManyToMany
    @NotEmpty(message = "Inseira ao menos uma role para o usuário")
    private List<Role> roles;
}
