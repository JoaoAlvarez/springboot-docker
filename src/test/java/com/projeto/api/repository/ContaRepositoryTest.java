package com.projeto.api.repository;

import com.projeto.api.domain.Conta;
import com.projeto.api.domain.enumeration.ContaSituacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class ContaRepositoryTest {

    @Autowired
    private ContaRepository contaRepository;

    private Conta conta1;
    private Conta conta2;
    private Conta conta3;
    private static final BigDecimal BIG_DECIMAL_100 = BigDecimal.valueOf(100.00);
    private static final String CONTA_NOME = "a cool bill";
    private static final LocalDate DATA = LocalDate.of(2024, 6,10);


    @BeforeEach
    public void setup() {
        // Criar contas de exemplo para testes
        conta1 = new Conta();
        conta1.setSituacao(ContaSituacao.PENDENTE);
        conta1.setDescricao("Conta de água");
        conta1.setDataVencimento(LocalDate.now().plusDays(1));
        conta1.setValor(BigDecimal.valueOf(100.00));
        contaRepository.save(conta1);

        conta2 = new Conta();
        conta2.setSituacao(ContaSituacao.PAGO);
        conta2.setDescricao("Conta de luz");
        conta2.setDataVencimento(LocalDate.now().minusDays(1));
        conta2.setValor(BigDecimal.valueOf(150.00));
        contaRepository.save(conta2);

        conta3 = new Conta();
        conta3.setSituacao(ContaSituacao.PENDENTE);
        conta3.setDescricao("Conta de telefone");
        conta3.setDataVencimento(LocalDate.now().plusDays(2));
        conta3.setValor(BigDecimal.valueOf(200.00));
        contaRepository.save(conta3);
    }



    @Test
    public void testPersistence() {
        //given
        Conta conta = new Conta();
        conta.setSituacao(ContaSituacao.PENDENTE);
        conta.setValor(BIG_DECIMAL_100);
        conta.setDataVencimento(DATA);
        conta.setDescricao(CONTA_NOME);

        //when
        contaRepository.save(conta);

        //then
        assertThat(conta.getId()).isNotNull();
        Conta newConta = contaRepository.findById(conta.getId()).orElse(null);
        assertThat(newConta.getId()).isEqualTo((Long) 4L);
        assertThat(newConta.getDescricao()).isEqualTo(CONTA_NOME);
        assertThat(BIG_DECIMAL_100.compareTo(newConta.getValor())).isEqualTo(0);
        assertThat(newConta.getDataVencimento()).isEqualTo(DATA);
    }

    @Test
    @DisplayName("Should find accounts by situacao, dateVencimento and descricao")
    public void findByFilters_withDateVencimento() {
        // given
        ContaSituacao situacao = ContaSituacao.PENDENTE;
        LocalDate dateVencimento = LocalDate.now().plusDays(1);
        String descricao = "água";

        // when
        Page<Conta> result = contaRepository.findByFilters(situacao, dateVencimento.toString(), descricao, PageRequest.of(0, 10));

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getDescricao()).isEqualTo(conta1.getDescricao());
    }

    @Test
    @DisplayName("Should find accounts by situacao and descricao")
    public void findByFilters_withoutDateVencimento() {
        // given
        ContaSituacao situacao = ContaSituacao.PAGO;
        String descricao = "luz";

        // when
        Page<Conta> result = contaRepository.findByFilters(situacao, descricao, PageRequest.of(0, 10));

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getDescricao()).isEqualTo(conta2.getDescricao());
    }

    @Test
    @DisplayName("Should return empty list when no accounts found")
    public void findByFilters_noResults() {
        // given
        ContaSituacao situacao = ContaSituacao.PAGO;
        String descricao = "inexistente";

        // when
        Page<Conta> result = contaRepository.findByFilters(situacao, descricao, PageRequest.of(0, 10));

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
    }

    @Test
    @DisplayName("Should find total valor by situacao and periodo")
    public void findTotalValorBySituacaoAndPeriodo() {
        // given
        ContaSituacao situacao = ContaSituacao.PAGO;
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(1);
        conta1.setDataPagamento(startDate);
        conta1.setSituacao(situacao);
        conta2.setDataPagamento(endDate);
        conta2.setSituacao(situacao);
        contaRepository.saveAll(Arrays.asList(conta1, conta2));
        // when
        BigDecimal total = contaRepository.findTotalValorBySituacaoAndPeriodo(situacao, startDate, endDate);

        // then
        assertThat(total).isNotNull();
        assertThat(total).isEqualByComparingTo(BigDecimal.valueOf(250.00)); // conta1 + conta3
    }
}
