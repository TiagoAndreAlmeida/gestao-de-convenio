package com.example.gestaoconvenios.infrastructure.convenios.persistence.adapter;

import java.util.List;

import com.example.gestaoconvenios.domain.entity.convenios.Contato;
import com.example.gestaoconvenios.domain.repository.ContatoRepository;
import com.example.gestaoconvenios.infrastructure.convenios.persistence.entity.ContatoEntity;
import com.example.gestaoconvenios.infrastructure.convenios.persistence.entity.EmpresaConveniadaEntity;
import com.example.gestaoconvenios.infrastructure.convenios.persistence.mapper.ContatoMapper;
import com.example.gestaoconvenios.infrastructure.convenios.persistence.repository.ContatoJpaRepository;

public class ContatoRepositoryAdapter implements ContatoRepository {
    private ContatoJpaRepository contatoJpaRepository;
    private ContatoMapper contatoMapper;

    public ContatoRepositoryAdapter(ContatoJpaRepository contatoJpaRepository, ContatoMapper contatoMapper) {
        this.contatoJpaRepository = contatoJpaRepository;
        this.contatoMapper = contatoMapper;
    }

    @Override
    public boolean existsByEmail(String email) {
        return this.contatoJpaRepository.existsByEmail(email);
    }

    @Override
    public void saveAll(List<Contato> contatos) {
        
        List<ContatoEntity> entities = contatos.stream()
                .map(contato -> new ContatoEntity(
                        contato.getId(),
                        contato.getNome(),
                        contato.getEmail(),
                        contato.getCargo(),
                        contato.getTelefone(),
                        empresaEntity,
                        contato.getCriadoEm(),
                        contato.getAtualizadoEm()
                ))
                .toList();

        contatoJpaRepository.saveAll(entities);
    }

}
