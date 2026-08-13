package com.example.gestaoconvenios.infrastructure.convenios.persistence.adapter;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.gestaoconvenios.domain.entity.convenios.Contato;
import com.example.gestaoconvenios.domain.repository.ContatoRepository;
import com.example.gestaoconvenios.infrastructure.convenios.persistence.entity.ContatoEntity;
import com.example.gestaoconvenios.infrastructure.convenios.persistence.entity.EmpresaConveniadaEntity;
import com.example.gestaoconvenios.infrastructure.convenios.persistence.mapper.ContatoMapper;
import com.example.gestaoconvenios.infrastructure.convenios.persistence.repository.ContatoJpaRepository;
import com.example.gestaoconvenios.infrastructure.convenios.persistence.repository.EmpresaConveniadaJpaRepository;

@Component
public class ContatoRepositoryAdapter implements ContatoRepository {
    private EmpresaConveniadaJpaRepository empresaConveniadaJpaRepository;
    private ContatoJpaRepository contatoJpaRepository;
    private ContatoMapper contatoMapper;

    public ContatoRepositoryAdapter(
        ContatoJpaRepository contatoJpaRepository, 
        ContatoMapper contatoMapper, 
        EmpresaConveniadaJpaRepository empresaConveniadaJpaRepository
    ) {
        this.contatoJpaRepository = contatoJpaRepository;
        this.contatoMapper = contatoMapper;
        this.empresaConveniadaJpaRepository = empresaConveniadaJpaRepository;
    }

    @Override
    public boolean existsByEmail(String email) {
        return this.contatoJpaRepository.existsByEmail(email);
    }

    @Override
    public void saveAll(List<Contato> contatos) {
        
        List<ContatoEntity> entities = contatos.stream()
            .map(contato -> {
                EmpresaConveniadaEntity empresa = this.empresaConveniadaJpaRepository.getReferenceById(contato.getEmpresaConveniada().getId());
                return this.contatoMapper.toEntity(contato, empresa);
            })
            .toList();

        contatoJpaRepository.saveAll(entities);
    }

}
