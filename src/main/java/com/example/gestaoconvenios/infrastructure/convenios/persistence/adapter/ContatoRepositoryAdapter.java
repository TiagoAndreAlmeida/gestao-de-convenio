package com.example.gestaoconvenios.infrastructure.convenios.persistence.adapter;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.example.gestaoconvenios.application.shared.pagination.PaginatedResult;
import com.example.gestaoconvenios.domain.entity.convenios.Contato;
import com.example.gestaoconvenios.domain.entity.convenios.EmpresaConveniada;
import com.example.gestaoconvenios.domain.repository.ContatoRepository;
import com.example.gestaoconvenios.infrastructure.convenios.persistence.entity.ContatoEntity;
import com.example.gestaoconvenios.infrastructure.convenios.persistence.entity.EmpresaConveniadaEntity;
import com.example.gestaoconvenios.infrastructure.convenios.persistence.mapper.ContatoMapper;
import com.example.gestaoconvenios.infrastructure.convenios.persistence.mapper.EmpresaConveniadaMapper;
import com.example.gestaoconvenios.infrastructure.convenios.persistence.repository.ContatoJpaRepository;
import com.example.gestaoconvenios.infrastructure.convenios.persistence.repository.EmpresaConveniadaJpaRepository;

@Component
public class ContatoRepositoryAdapter implements ContatoRepository {
    private EmpresaConveniadaJpaRepository empresaConveniadaJpaRepository;
    private ContatoJpaRepository contatoJpaRepository;
    private ContatoMapper contatoMapper;
    private EmpresaConveniadaMapper empresaConveniadaMapper;

    public ContatoRepositoryAdapter(
            ContatoJpaRepository contatoJpaRepository,
            ContatoMapper contatoMapper,
            EmpresaConveniadaJpaRepository empresaConveniadaJpaRepository,
            EmpresaConveniadaMapper empresaConveniadaMapper) {
        this.contatoJpaRepository = contatoJpaRepository;
        this.contatoMapper = contatoMapper;
        this.empresaConveniadaJpaRepository = empresaConveniadaJpaRepository;
        this.empresaConveniadaMapper = empresaConveniadaMapper;
    }

    @Override
    public boolean existsByEmail(String email) {
        return this.contatoJpaRepository.existsByEmail(email);
    }

    @Override
    public void saveAll(List<Contato> contatos) {

        List<ContatoEntity> entities = contatos.stream()
                .map(contato -> {
                    EmpresaConveniadaEntity empresa = this.empresaConveniadaJpaRepository
                            .getReferenceById(contato.getEmpresaConveniada().getId());
                    return this.contatoMapper.toEntity(contato, empresa);
                })
                .toList();

        contatoJpaRepository.saveAll(entities);
    }

    @Override
    public Contato save(Contato contato) {
        EmpresaConveniadaEntity empresa = this.empresaConveniadaJpaRepository
                .getReferenceById(contato.getEmpresaConveniada().getId());
        ContatoEntity entity = this.contatoMapper.toEntity(contato, empresa);
        ContatoEntity savedEntity = contatoJpaRepository.save(entity);
        return this.contatoMapper.toDomain(savedEntity, contato.getEmpresaConveniada());
    }

    @Override
    public PaginatedResult<Contato> findByEmpresaConveniada(Long empresaConveniadaId, String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<ContatoEntity> pagedContatoEntities = contatoJpaRepository
            .findByEmpresaConveniada(empresaConveniadaId, search, pageable);

        EmpresaConveniada empresa = this.empresaConveniadaMapper
            .toDomain(this.empresaConveniadaJpaRepository.getReferenceById(empresaConveniadaId));

        List<Contato> contatos = pagedContatoEntities.getContent()
            .stream()
            .map(entity -> contatoMapper.toDomain(entity, empresa))
            .toList();

        return new PaginatedResult<Contato>(
            contatos, 
            pagedContatoEntities.getNumber(), 
            pagedContatoEntities.getSize(), 
            pagedContatoEntities.getTotalElements(), 
            pagedContatoEntities.getTotalPages()
        );
    }

}
