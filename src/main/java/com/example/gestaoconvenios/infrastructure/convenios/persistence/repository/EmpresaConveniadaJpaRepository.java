package com.example.gestaoconvenios.infrastructure.convenios.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.gestaoconvenios.infrastructure.convenios.persistence.entity.EmpresaConveniadaEntity;

public interface EmpresaConveniadaJpaRepository extends JpaRepository<EmpresaConveniadaEntity, Long> {
    boolean existsByCnpj(String cnpj);
}
