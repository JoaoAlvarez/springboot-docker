package com.projeto.api.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ROLE")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Role {

    protected static final String SEQUENCIAL = "role_id_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCIAL)
    @SequenceGenerator(name = SEQUENCIAL, allocationSize = 1)
    private Long id;

    private String name;
}
