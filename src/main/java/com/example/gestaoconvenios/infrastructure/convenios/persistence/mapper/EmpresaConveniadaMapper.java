package com.example.gestaoconvenios.infrastructure.convenios.persistence.mapper;

import org.springframework.stereotype.Component;

import com.example.gestaoconvenios.domain.entity.convenios.EmpresaConveniada;
import com.example.gestaoconvenios.infrastructure.convenios.persistence.entity.EmpresaConveniadaEntity;

@Component
public class EmpresaConveniadaMapper {

    public EmpresaConveniada toDomain(EmpresaConveniadaEntity entity) {
        return new EmpresaConveniada(
            entity.getId(), 
            entity.getRazaoSocial(), 
            entity.getCnpj(), 
            entity.getCnpj(), 
            entity.getAtiva(), 
            entity.getExcluida(), 
            entity.getCriadoEm(), 
            entity.getAtualizadoEm()
        );
    }

    public EmpresaConveniadaEntity toEntity(EmpresaConveniada domain) {
        return new EmpresaConveniadaEntity(
            domain.getId(), 
            domain.getRazaoSocial(), 
            domain.getCnpj(), 
            domain.getEndereco(), 
            domain.getAtiva(), 
            domain.getExcluida(), 
            domain.getCriadaEm(), 
            domain.getAtualizadaEm()
        );

    }

}
