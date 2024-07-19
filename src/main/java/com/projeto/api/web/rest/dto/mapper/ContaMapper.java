package com.projeto.api.web.rest.dto.mapper;

import com.projeto.api.domain.Conta;
import com.projeto.api.web.rest.dto.ManterContaDTO;
import com.projeto.api.web.rest.dto.ViewContaDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ContaMapper {

    public static ViewContaDTO toDto(Conta obj){
        ViewContaDTO dto = new ViewContaDTO();
        dto.setId(obj.getId());
        dto.setDataPagamento(obj.getDataPagamento());
        dto.setDataVencimento(obj.getDataVencimento());
        dto.setDescricao(obj.getDescricao());
        dto.setSituacao(obj.getSituacao());
        dto.setValor(obj.getValor());
        return dto;
    }

    public static List<ViewContaDTO> toDto(List<Conta> l) {
        if (l == null) {
            return new ArrayList<>();
        }
        return l.stream().map(el -> toDto(el)).collect(Collectors.toList());
    }

    public static Conta fromDto(ManterContaDTO dto){
        Conta obj = new Conta();
        obj.setId(dto.getId());
        obj.setDataPagamento(dto.getDataPagamento());
        obj.setDataVencimento(dto.getDataVencimento());
        obj.setDescricao(dto.getDescricao());
        obj.setSituacao(dto.getSituacao());
        obj.setValor(dto.getValor());
        return obj;
    }



}
