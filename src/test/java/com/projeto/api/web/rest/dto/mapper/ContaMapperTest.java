package com.projeto.api.web.rest.dto.mapper;

import com.projeto.api.domain.Conta;
import com.projeto.api.domain.enumeration.ContaSituacao;
import com.projeto.api.web.rest.dto.ManterContaDTO;
import com.projeto.api.web.rest.dto.ViewContaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ContaMapperTest {

    private Conta conta;
    private ManterContaDTO dto;

    @BeforeEach
    void setUp() {
        // Configuração inicial para cada teste
        conta = new Conta();
        conta.setId(1L);
        conta.setDataPagamento(LocalDate.now());
        conta.setDataVencimento(LocalDate.now().plusDays(5));
        conta.setDescricao("Conta de água");
        conta.setSituacao(ContaSituacao.PENDENTE);
        conta.setValor(new BigDecimal("150.00"));

        dto = new ManterContaDTO();
        dto.setId(1L);
        dto.setDataPagamento(LocalDate.now());
        dto.setDataVencimento(LocalDate.now().plusDays(5));
        dto.setDescricao("Conta de água");
        dto.setSituacao(ContaSituacao.PENDENTE);
        dto.setValor(new BigDecimal("150.00"));
    }

    @Test
    @DisplayName("Converter Conta para ViewContaDTO")
    void testToDto() {
        // Execução do método a ser testado
        ViewContaDTO dto = ContaMapper.toDto(conta);

        // Verificação do resultado
        assertThat(dto.getId()).isEqualTo(conta.getId());
        assertThat(dto.getDataPagamento()).isEqualTo(conta.getDataPagamento());
        assertThat(dto.getDataVencimento()).isEqualTo(conta.getDataVencimento());
        assertThat(dto.getDescricao()).isEqualTo(conta.getDescricao());
        assertThat(dto.getSituacao()).isEqualTo(conta.getSituacao());
        assertThat(dto.getValor()).isEqualTo(conta.getValor());
    }

    @Test
    @DisplayName("Converter Lista de Contas para Lista de ViewContaDTO")
    void testToDtoList() {
        // Lista de Contas
        List<Conta> listaContas = new ArrayList<>();
        listaContas.add(conta);

        // Execução do método a ser testado
        List<ViewContaDTO> listaDto = ContaMapper.toDto(listaContas);

        // Verificação do resultado
        assertThat(listaDto).hasSize(1);
        ViewContaDTO dto = listaDto.get(0);
        assertThat(dto.getId()).isEqualTo(conta.getId());
        assertThat(dto.getDataPagamento()).isEqualTo(conta.getDataPagamento());
        assertThat(dto.getDataVencimento()).isEqualTo(conta.getDataVencimento());
        assertThat(dto.getDescricao()).isEqualTo(conta.getDescricao());
        assertThat(dto.getSituacao()).isEqualTo(conta.getSituacao());
        assertThat(dto.getValor()).isEqualTo(conta.getValor());
    }

    @Test
    @DisplayName("Converter ManterContaDTO para Conta")
    void testFromDto() {
        // Execução do método a ser testado
        Conta obj = ContaMapper.fromDto(dto);

        // Verificação do resultado
        assertThat(obj.getId()).isEqualTo(dto.getId());
        assertThat(obj.getDataPagamento()).isEqualTo(dto.getDataPagamento());
        assertThat(obj.getDataVencimento()).isEqualTo(dto.getDataVencimento());
        assertThat(obj.getDescricao()).isEqualTo(dto.getDescricao());
        assertThat(obj.getSituacao()).isEqualTo(dto.getSituacao());
        assertThat(obj.getValor()).isEqualTo(dto.getValor());
    }
}
