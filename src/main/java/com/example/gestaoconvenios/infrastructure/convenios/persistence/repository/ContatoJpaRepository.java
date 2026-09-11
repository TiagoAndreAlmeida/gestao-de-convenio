package com.example.gestaoconvenios.infrastructure.convenios.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.gestaoconvenios.infrastructure.convenios.persistence.entity.ContatoEntity;

public interface ContatoJpaRepository extends JpaRepository<ContatoEntity, Long> {
    boolean existsByEmail(String email);

    @Query("""
        SELECT c
        FROM ContatoEntity c
        WHERE c.empresaConveniada.id = :empresaConveniadaId
            AND (
                :search IS NULL
                OR LOWER(c.nome) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(c.email) LIKE LOWER(CONCAT('%', :search, '%'))
            )
        """)
    Page<ContatoEntity> findByEmpresaConveniada(Long empresaConveniadaId, String search, Pageable pageable);
}
