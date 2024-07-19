package com.projeto.api.web.rest;

import com.projeto.api.domain.Conta;
import com.projeto.api.repository.ContaRepository;
import com.projeto.api.service.ContaService;
import com.projeto.api.web.rest.dto.ManterContaDTO;
import com.projeto.api.web.rest.dto.ViewValorTotalPeriodoDTO;
import com.projeto.api.web.rest.dto.mapper.ContaMapper;
import com.projeto.api.web.rest.exceptions.ImportCsvException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ContaResourceTest {

    @InjectMocks
    private ContaResource contaResource;

    @Mock
    private ContaService contaService;

    @Mock
    private ContaRepository contaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Criar nova conta com sucesso")
    void cadastrarConta_success() throws URISyntaxException {
        // Dados de entrada
        ManterContaDTO dto = new ManterContaDTO();
        dto.setDescricao("Conta de água");
        dto.setValor(new BigDecimal("150.00"));

        Conta conta = ContaMapper.fromDto(dto);
        conta.setId(1L); // Simula o ID gerado pelo serviço

        // Mock do serviço
        when(contaService.cadastrarConta(any())).thenReturn(conta);

        // Execução do endpoint
        ResponseEntity<Conta> response = contaResource.cadastrarConta(dto);

        // Verificações
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(conta);
        assertThat(response.getHeaders().getLocation()).isEqualTo(new URI("/api/v1/conta/" + conta.getId()));
    }

    @Test
    @DisplayName("Falha ao criar nova conta (ID já existente)")
    void cadastrarConta_failure_idExists() throws URISyntaxException {
        // Dados de entrada
        ManterContaDTO dto = new ManterContaDTO();
        dto.setId(1L); // ID já existente

        // Execução do endpoint
        ResponseEntity<Conta> response = contaResource.cadastrarConta(dto);

        // Verificações
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Buscar conta por ID existente")
    void buscarPorId_success() {
        // Dados de entrada
        Long id = 1L;
        Conta conta = new Conta();
        conta.setId(id);

        // Mock do serviço
        when(contaService.buscarPorId(id)).thenReturn(Optional.of(conta));

        // Execução do endpoint
        ResponseEntity<Conta> response = contaResource.buscarPorId(id);

        // Verificações
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(conta);
    }

    @Test
    @DisplayName("Falha ao buscar conta por ID inexistente")
    void buscarPorId_failure_notFound() {
        // Dados de entrada
        Long id = 1L;

        // Mock do serviço
        when(contaService.buscarPorId(id)).thenReturn(Optional.empty());

        // Execução do endpoint
        try {
            contaResource.buscarPorId(id);
        } catch (ResponseStatusException e) {
            // Verificações
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Test
    @DisplayName("Buscar total valor pago por período")
    void getTotalValorPago() {
        // Dados de entrada
        LocalDate dataInicial = LocalDate.now().minusMonths(1);
        LocalDate dataFinal = LocalDate.now();

        BigDecimal totalValor = new BigDecimal("500.00");

        // Mock do serviço
        when(contaService.getTotalValorPagoPorPeriodo(dataInicial, dataFinal)).thenReturn(totalValor);

        // Execução do endpoint
        ResponseEntity<ViewValorTotalPeriodoDTO> response = contaResource.getTotalValorPago(dataInicial, dataFinal);

        // Verificações
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getDataInicial()).isEqualTo(dataInicial);
        assertThat(response.getBody().getDataFinal()).isEqualTo(dataFinal);
        assertThat(response.getBody().getValorTotal()).isEqualTo(totalValor);
    }

    @Test
    @DisplayName("Atualizar conta com sucesso")
    void atualizarConta_success() {
        // Dados de entrada
        Long id = 1L;
        ManterContaDTO dto = new ManterContaDTO();
        dto.setId(id);

        Conta conta = ContaMapper.fromDto(dto);

        // Mock do repositório
        when(contaRepository.existsById(id)).thenReturn(true);

        // Mock do serviço
        when(contaService.atualizarConta(any())).thenReturn(Optional.of(conta));

        // Execução do endpoint
        ResponseEntity<Conta> response = contaResource.atualizarConta(id, dto);

        // Verificações
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(conta);
    }

    @Test
    @DisplayName("Falha ao atualizar conta (ID não encontrado)")
    void atualizarConta_failure_idNotFound() {
        // Dados de entrada
        Long id = 1L;
        ManterContaDTO dto = new ManterContaDTO();
        dto.setId(id);

        // Mock do repositório
        when(contaRepository.existsById(id)).thenReturn(false);

        // Execução do endpoint

        ResponseEntity response = contaResource.atualizarConta(id, dto);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    @DisplayName("Importar contas via CSV com sucesso")
    void importByCsv_success() throws IOException, ImportCsvException {
        // Dados de entrada
        MultipartFile file = mock(MultipartFile.class);

        // Mock do serviço
        when(contaService.importarContas(file)).thenReturn(10); // Simula 10 contas importadas com sucesso

        // Execução do endpoint
        ResponseEntity<Integer> response = contaResource.importByCsv(file);

        // Verificações
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(10);
    }

    @Test
    @DisplayName("Falha ao importar contas via CSV (erro de importação)")
    void importByCsv_failure_importError() throws IOException, ImportCsvException {
        // Dados de entrada
        MultipartFile file = mock(MultipartFile.class);

        // Mock do serviço lançando exceção
        when(contaService.importarContas(file)).thenThrow(new ImportCsvException("Erro ao importar CSV"));

        // Execução do endpoint
        ResponseEntity<Integer> response = contaResource.importByCsv(file);

        // Verificações
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    // Métodos auxiliares não testados diretamente via endpoints

}
