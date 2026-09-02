package com.example.gestaoconvenios.domain.repository;

import java.util.Optional;

import com.example.gestaoconvenios.domain.entity.convenios.EmpresaConveniada;

public interface EmpresaConveniadaRepository {
    boolean existsByCnpj(String cnpj);
    Optional<EmpresaConveniada> findById(Long id);
    EmpresaConveniada save(EmpresaConveniada empresaConveniada);

}
