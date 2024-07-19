package com.projeto.api.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.projeto.api.domain.Conta;
import com.projeto.api.domain.enumeration.ContaSituacao;
import com.projeto.api.repository.ContaRepository;
import com.projeto.api.service.dto.ContaCsv;
import com.projeto.api.web.rest.exceptions.ImportCsvException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class ContaService {

    private ContaRepository contaRepository;

    public Optional<Conta> buscarPorId(Long id){
        return contaRepository.findById(id);
    }

    public Conta cadastrarConta(Conta dto){
        log.info("Nova conta para ser cadastrada: {}",dto);
        dto.setSituacao(ContaSituacao.PENDENTE);
        return contaRepository.save(dto);
    }


    public BigDecimal getTotalValorPagoPorPeriodo(LocalDate dataInicial, LocalDate dataFinal) {
        return contaRepository.findTotalValorBySituacaoAndPeriodo(ContaSituacao.PAGO, dataInicial, dataFinal);
    }

    public Optional<Conta> atualizarConta(Conta dto){
        log.info("Atualizar conta para : {}",dto);
        return contaRepository.findById(dto.getId())
                .map( fromDataBase -> {
                    if(dto.getDescricao() != null){
                        fromDataBase.setDescricao(dto.getDescricao());
                    }
                    if(dto.getValor()!= null){
                        fromDataBase.setValor(dto.getValor());
                    }
                    if(dto.getDataPagamento() != null){
                        fromDataBase.setDataPagamento(dto.getDataPagamento());
                    }
                    if(dto.getDataVencimento()!= null){
                        fromDataBase.setDataVencimento(dto.getDataVencimento());
                    }
                    if(dto.getSituacao() != null){
                        fromDataBase.setSituacao(dto.getSituacao());
                    }
                    return fromDataBase;
                }).map(contaRepository::save);
    }

    public Page<Conta> buscaPaginada(LocalDate dateVencimento, String descricao, ContaSituacao status, Pageable pageable) {
        if(status == null){
            status = ContaSituacao.PENDENTE;
        }
        log.info("Listar contas com os filtros: Data={}, descricao={}, status={}",dateVencimento,descricao,status);
        if(dateVencimento!= null){
            return contaRepository.findByFilters(status, dateToString(dateVencimento), descricao,  pageable);
        }
        return contaRepository.findByFilters(status, descricao,  pageable);
    }

    private String dateToString(LocalDate date){
        if(date == null){
            return null;
        }
        return date != null ? date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null;
    }

    public Integer importarContas(MultipartFile file) throws IOException, ImportCsvException {
        Set<Conta> list = parseCsv(file);
        return contaRepository.saveAll(list).size();
    }

    private Set<Conta> parseCsv(MultipartFile file) throws IOException, ImportCsvException, DateTimeParseException {
        try(Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))){
            HeaderColumnNameMappingStrategy<ContaCsv> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(ContaCsv.class);
            CsvToBean<ContaCsv> csvToBean = new CsvToBeanBuilder<ContaCsv>(reader)
                    .withMappingStrategy(strategy)
                    .withIgnoreEmptyLine(true)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            List<ContaCsv> lines = csvToBean.parse();
            Set<Conta> contas = new HashSet<>();

            for (int i = 0; i < lines.size(); i++) {
                ContaCsv line = lines.get(i);
                if (line.getValor() == null) {
                    throw new ImportCsvException("Valor deve ser preenchido na linha " + (i + 1));
                }
                try{
                    line.getDataPagamentoLocalDate();
                    line.getDataVencimentoLocalDate();
                } catch (DateTimeParseException dateTimeParseException){
                    throw new ImportCsvException("Formato de data Invalida na linha " + (i + 1));
                }
                contas.add(Conta.builder()
                        .dataPagamento(line.getDataPagamentoLocalDate())
                        .dataVencimento(line.getDataVencimentoLocalDate())
                        .descricao(line.getDescricao())
                        .valor(line.getValor())
                        .situacao(line.getSituacaoEnum())
                        .build());
            }
            return contas;
        }
    }
}
