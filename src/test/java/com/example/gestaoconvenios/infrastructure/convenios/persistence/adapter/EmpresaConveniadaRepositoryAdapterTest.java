package com.example.gestaoconvenios.infrastructure.convenios.persistence.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import com.example.gestaoconvenios.domain.entity.convenios.EmpresaConveniada;
import com.example.gestaoconvenios.infrastructure.convenios.persistence.entity.EmpresaConveniadaEntity;
import com.example.gestaoconvenios.infrastructure.convenios.persistence.mapper.EmpresaConveniadaMapper;
import com.example.gestaoconvenios.infrastructure.convenios.persistence.repository.EmpresaConveniadaJpaRepository;

@ExtendWith(MockitoExtension.class)
public class EmpresaConveniadaRepositoryAdapterTest {
    @Mock
    private EmpresaConveniadaJpaRepository empresaConveniadaJpaRepository;

    @Mock
    private EmpresaConveniadaMapper empresaConveniadaMapper;

    @InjectMocks
    private EmpresaConveniadaRepositoryAdapter empresaConveniadaRepositoryAdapter;

    @Test
    @Description("Should return true when existsByCnpj is called with an existing CNPJ")
    void shouldReturnTrueWhenExistsByCnpjIsCalledWithExistingCnpj() {
        when(empresaConveniadaJpaRepository.existsByCnpj("12345678901234")).thenReturn(true);
        boolean result = empresaConveniadaJpaRepository.existsByCnpj("12345678901234");
        assertThat(result).isTrue();      
    }

    @Test
    @Description("Should return false when existsByCnpj is called with a non-existing CNPJ")
    void shouldReturnFalseWhenExistsByCnpjIsCalledWithNonExistingCnpj() {
        when(empresaConveniadaJpaRepository.existsByCnpj("12345678901234")).thenReturn(false);
        boolean result = empresaConveniadaJpaRepository.existsByCnpj("12345678901234");
        assertThat(result).isFalse();
    }

    @Test
    @Description("Should save and return a new empresa conveniada")
    void shouldSaveAndReturnNewEmpresaConveniada() {
        EmpresaConveniada empresaConveniada = new EmpresaConveniada(
            1L, 
            "Empresa XPTO", 
            "12345678901234", 
            "Av zona sul", 
            true, 
            false, 
            LocalDateTime.now(), 
            LocalDateTime.now()
        );
        EmpresaConveniadaEntity entity = new EmpresaConveniadaEntity(
            1L, 
            "Empresa XPTO", 
            "12345678901234", 
            "Av zona sul", 
            true, 
            false, 
            LocalDateTime.now(), 
            LocalDateTime.now()
        );
        when(empresaConveniadaMapper.toEntity(empresaConveniada)).thenReturn(entity);
        when(empresaConveniadaJpaRepository.save(entity)).thenReturn(entity);
        when(empresaConveniadaMapper.toDomain(entity)).thenReturn(empresaConveniada);

        EmpresaConveniada savedEmpresa = empresaConveniadaRepositoryAdapter.save(empresaConveniada);
        assertThat(savedEmpresa).isNotNull();
        assertThat(savedEmpresa.getId()).isEqualTo(1L);
        assertThat(savedEmpresa.getRazaoSocial()).isEqualTo("Empresa XPTO");
        assertThat(savedEmpresa.getCnpj()).isEqualTo("12345678901234");
        assertThat(savedEmpresa.getAtiva()).isEqualTo(true);
    }
}
