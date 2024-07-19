package com.projeto.api.service.dto;

import com.opencsv.bean.CsvBindByName;
import com.projeto.api.domain.enumeration.ContaSituacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContaCsv {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    @CsvBindByName(column = "descricao")
    private String descricao;

    @CsvBindByName(column = "dataVencimento")
    private String dataVencimento;

    @CsvBindByName(column = "dataPagamento")
    private String dataPagamento;

    @CsvBindByName(column = "valor")
    private BigDecimal valor;

    @CsvBindByName(column = "situacao")
    private String situacao;

    public LocalDate getDataPagamentoLocalDate(){
        if(dataPagamento != null && !dataPagamento.isBlank()) {
            return LocalDate.parse(dataPagamento, FORMATTER);
        }
        return null;
    }

    public LocalDate getDataVencimentoLocalDate(){
        if(dataVencimento != null && !dataVencimento.isBlank()) {
            return LocalDate.parse(dataVencimento, FORMATTER);
        }
        return null;
    }

    public ContaSituacao getSituacaoEnum(){
        if(situacao != null && !situacao.isBlank()) {
            return ContaSituacao.valueOf(situacao.toUpperCase());
        }
        return ContaSituacao.PENDENTE;
    }
}
