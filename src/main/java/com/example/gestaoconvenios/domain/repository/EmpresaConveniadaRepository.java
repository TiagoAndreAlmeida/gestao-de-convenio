package com.example.gestaoconvenios.domain.repository;

import com.example.gestaoconvenios.domain.entity.convenios.EmpresaConveniada;

public interface EmpresaConveniadaRepository {
    boolean existsByCnpj(String cnpj);
    EmpresaConveniada save(EmpresaConveniada empresaConveniada);
}
