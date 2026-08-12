package com.example.gestaoconvenios.infrastructure.convenios.persistence.mapper;

import com.example.gestaoconvenios.domain.entity.convenios.Contato;
import com.example.gestaoconvenios.domain.entity.convenios.EmpresaConveniada;
import com.example.gestaoconvenios.infrastructure.convenios.persistence.entity.ContatoEntity;
import com.example.gestaoconvenios.infrastructure.convenios.persistence.entity.EmpresaConveniadaEntity;

public class ContatoMapper {
    public ContatoEntity toEntity(Contato domain, EmpresaConveniadaEntity empresaConveniadaEntity) {
        return new ContatoEntity(
            domain.getId(), 
            domain.getNome(), 
            domain.getEmail(), 
            domain.getCargo(), 
            domain.getTelefone(), 
            empresaConveniadaEntity, 
            domain.getCriadoEm(), 
            domain.getAtualizadoEm()
        );
    }

    public Contato toDomain(ContatoEntity entity, EmpresaConveniada empresaConveniada) {
        return new Contato(
            entity.getId(), 
            entity.getNome(), 
            entity.getEmail(), 
            entity.getCargo(), 
            entity.getTelefone(), 
            empresaConveniada, 
            entity.getCriadoEm(), 
            entity.getAtualizadoEm()
        );
    }
}
