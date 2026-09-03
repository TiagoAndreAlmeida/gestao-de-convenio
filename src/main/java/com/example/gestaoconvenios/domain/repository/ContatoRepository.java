package com.example.gestaoconvenios.domain.repository;

import java.util.List;

import com.example.gestaoconvenios.domain.entity.convenios.Contato;

public interface ContatoRepository {
    boolean existsByEmail(String email);
    void saveAll(List<Contato> contatos);
    Contato save(Contato contato);
}
