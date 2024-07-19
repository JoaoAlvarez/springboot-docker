package com.projeto.api.web.rest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Builder
public class ViewValorTotalPeriodoDTO {
    @Getter
    private LocalDate dataInicial;
    @Getter
    private LocalDate dataFinal;
    private BigDecimal valorTotal;

    public BigDecimal getValorTotal() {
        return valorTotal != null ? valorTotal : BigDecimal.ZERO;
    }
}

