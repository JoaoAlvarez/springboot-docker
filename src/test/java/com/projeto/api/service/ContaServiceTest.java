package com.projeto.api.service;

import com.projeto.api.domain.Conta;
import com.projeto.api.domain.enumeration.ContaSituacao;
import com.projeto.api.repository.ContaRepository;
import com.projeto.api.web.rest.exceptions.ImportCsvException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private ContaService contaService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should find Conta by Id")
    void buscarPorId() {
        // Given
        Long id = 1L;
        Conta conta = new Conta();
        conta.setId(id);

        Mockito.when(contaRepository.findById(id)).thenReturn(Optional.of(conta));

        // When
        Optional<Conta> result = contaService.buscarPorId(id);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("Should register Conta successfully")
    void cadastrarConta() {
        // Given
        Conta conta = new Conta();
        conta.setDescricao("Conta de Teste");
        conta.setValor(BigDecimal.TEN);

        Mockito.when(contaRepository.save(conta)).thenReturn(conta);

        // When
        Conta createdConta = contaService.cadastrarConta(conta);

        // Then
        assertThat(createdConta).isNotNull();
        assertThat(createdConta.getSituacao()).isEqualTo(ContaSituacao.PENDENTE);
    }

    @Test
    @DisplayName("Should get total value paid by period")
    void getTotalValorPagoPorPeriodo() {
        // Given
        LocalDate dataInicial = LocalDate.of(2023, 1, 1);
        LocalDate dataFinal = LocalDate.of(2023, 12, 31);
        BigDecimal expectedTotal = BigDecimal.valueOf(1000);

        Mockito.when(contaRepository.findTotalValorBySituacaoAndPeriodo(ContaSituacao.PAGO, dataInicial, dataFinal))
                .thenReturn(expectedTotal);

        // When
        BigDecimal totalPago = contaService.getTotalValorPagoPorPeriodo(dataInicial, dataFinal);

        // Then
        assertThat(totalPago).isEqualTo(expectedTotal);
    }

    @Test
    @DisplayName("Should update Conta successfully")
    void atualizarConta() {
        // Given
        Long id = 1L;
        Conta contaExistente = new Conta();
        contaExistente.setId(id);
        contaExistente.setDescricao("Conta Original");

        Conta contaAtualizada = new Conta();
        contaAtualizada.setId(id);
        contaAtualizada.setDescricao("Conta Atualizada");

        Mockito.when(contaRepository.findById(id)).thenReturn(Optional.of(contaExistente));
        Mockito.when(contaRepository.save(contaExistente)).thenReturn(contaExistente);

        // When
        Optional<Conta> updatedConta = contaService.atualizarConta(contaAtualizada);

        // Then
        assertThat(updatedConta).isPresent();
        assertThat(updatedConta.get().getDescricao()).isEqualTo(contaAtualizada.getDescricao());
    }

    @Test
    @DisplayName("Should paginate Contas with filters")
    void buscaPaginada() {
        // Given
        LocalDate dataVencimento = LocalDate.of(2023, 6, 1);
        String descricao = "Conta";
        ContaSituacao status = ContaSituacao.PENDENTE;
        Pageable pageable = PageRequest.of(0, 10);
        Page<Conta> pageMock = Mockito.mock(Page.class);

        Mockito.when(contaRepository.findByFilters(status, dataVencimento.toString(), descricao, pageable))
                .thenReturn(pageMock);

        // When
        Page<Conta> result = contaService.buscaPaginada(dataVencimento, descricao, status, pageable);

        // Then
        assertThat(result).isNotNull();
        Mockito.verify(contaRepository, Mockito.times(1))
                .findByFilters(status, dataVencimento.toString(), descricao, pageable);
    }

    @Test
    @DisplayName("Should import Contas from CSV file")
    void importarContas() throws IOException, ImportCsvException {
        // Given
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv",
                "descricao,valor,dataVencimento,dataPagamento,situacao\nConta 1,100,01/06/2023,,PENDENTE\n".getBytes());

        Set<Conta> contasImportadas = new HashSet<>();
        contasImportadas.add(new Conta(null, LocalDate.of(2023, 6, 1), null, BigDecimal.valueOf(100), "Conta 1", ContaSituacao.PENDENTE));

        Mockito.when(contaRepository.saveAll(contasImportadas)).thenReturn(new ArrayList<>(contasImportadas));

        // When
        Integer numContasImportadas = contaService.importarContas(file);

        // Then
        assertThat(numContasImportadas).isEqualTo(contasImportadas.size());
    }

    @Test
    @DisplayName("Should throw exception when CSV is invalid")
    void importarContas_invalidDateCSV() {
        // Given
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv",
                ("descricao,valor,dataVencimento,dataPagamento,situacao\n" +
                        "Conta 1,100,2023-06-01,,PENDENTE\n").getBytes());

        // When/Then
        assertThatThrownBy(() -> contaService.importarContas(file))
                .isInstanceOf(ImportCsvException.class)
                .hasMessageContaining("Formato de data Invalida na linha");
    }

    @Test
    @DisplayName("Should throw exception when CSV is invalid")
    void importarContas_invalidCSV() {
        // Given
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/csv",
                ("descricao,valor,dataVencimento,dataPagamento,situacao\n" +
                        "Conta 1,,01/06/2023,,PENDENTE\n").getBytes());

        // When/Then
        assertThatThrownBy(() -> contaService.importarContas(file))
                .isInstanceOf(ImportCsvException.class)
                .hasMessageContaining("Valor deve ser preenchido");
    }
}
