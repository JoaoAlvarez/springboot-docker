package com.projeto.api.domain;

import com.projeto.api.domain.enumeration.ContaSituacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "contas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Conta {

    protected static final String SEQUENCIAL = "conta_id_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCIAL)
    @SequenceGenerator(name = SEQUENCIAL, allocationSize = 1)
    private Long id;

    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @Column(name = "valor")
    private BigDecimal valor;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "situacao")
    @Enumerated(EnumType.STRING)
    private ContaSituacao situacao = ContaSituacao.PENDENTE;

}
