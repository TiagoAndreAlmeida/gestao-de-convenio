package com.example.gestaoconvenios.infrastructure.convenios.persistence.adapter;

import org.springframework.stereotype.Repository;

import com.example.gestaoconvenios.domain.entity.convenios.EmpresaConveniada;
import com.example.gestaoconvenios.domain.repository.EmpresaConveniadaRepository;
import com.example.gestaoconvenios.infrastructure.convenios.persistence.entity.EmpresaConveniadaEntity;
import com.example.gestaoconvenios.infrastructure.convenios.persistence.mapper.EmpresaConveniadaMapper;
import com.example.gestaoconvenios.infrastructure.convenios.persistence.repository.EmpresaConveniadaJpaRepository;

@Repository
public class EmpresaConveniadaRepositoryAdapter implements EmpresaConveniadaRepository {
    private EmpresaConveniadaJpaRepository empresaConveniadaJpaRepository;
    private EmpresaConveniadaMapper empresaConveniadaMapper;

    public EmpresaConveniadaRepositoryAdapter(EmpresaConveniadaJpaRepository empresaConveniadaJpaRepository, EmpresaConveniadaMapper empresaConveniadaMapper) {
        this.empresaConveniadaJpaRepository = empresaConveniadaJpaRepository;
        this.empresaConveniadaMapper = empresaConveniadaMapper;
    }

    @Override
    public boolean existsByCnpj(String cnpj) {
        return this.empresaConveniadaJpaRepository.existsByCnpj(cnpj);
    }

    @Override
    public EmpresaConveniada save(EmpresaConveniada empresaConveniada) {
        EmpresaConveniadaEntity entity = this.empresaConveniadaMapper.toEntity(empresaConveniada);
        EmpresaConveniadaEntity savedEntity = this.empresaConveniadaJpaRepository.save(entity);
        return this.empresaConveniadaMapper.toDomain(savedEntity);
    }

}
