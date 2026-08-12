package com.example.gestaoconvenios.infrastructure.convenios.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gestaoconvenios.infrastructure.convenios.persistence.entity.ContatoEntity;

public interface ContatoJpaRepository extends JpaRepository<ContatoEntity, Long> {
    boolean existsByEmail(String email);
}
