package com.projeto.api.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projeto.api.domain.enumeration.ContaSituacao;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ViewContaDTO {
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dataVencimento;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dataPagamento;
    private BigDecimal valor;
    private String descricao;
    private ContaSituacao situacao;
}
