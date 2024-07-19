package com.projeto.api.repository;

import com.projeto.api.domain.Conta;
import com.projeto.api.domain.enumeration.ContaSituacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ContaRepository extends JpaRepository<Conta, Long> {

    @Query("SELECT c FROM Conta c " +
            "WHERE " +
            "c.situacao = :situacao " +
            "AND (:descricao IS NULL OR c.descricao LIKE %:descricao%)" +
            "AND (:dateVencimento IS NULL OR c.dataVencimento = CAST(:dateVencimento AS java.time.LocalDate))"
    )
    Page<Conta> findByFilters(
            @Param("situacao") ContaSituacao situacao,
            @Param("dateVencimento") String dateVencimento,
            @Param("descricao") String descricao,
            Pageable pageable);

    @Query("SELECT c FROM Conta c " +
            "WHERE " +
            "c.situacao = :situacao " +
            "AND (:descricao IS NULL OR c.descricao LIKE %:descricao%)"
    )
    Page<Conta> findByFilters(
            @Param("situacao") ContaSituacao situacao,
            @Param("descricao") String descricao,
            Pageable pageable);

    @Query("SELECT SUM(c.valor) FROM Conta c " +
            "WHERE " +
            "c.situacao = :situacao " +
            "AND c.dataPagamento BETWEEN :startDate AND :endDate")
    BigDecimal findTotalValorBySituacaoAndPeriodo(
            @Param("situacao") ContaSituacao situacao,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
