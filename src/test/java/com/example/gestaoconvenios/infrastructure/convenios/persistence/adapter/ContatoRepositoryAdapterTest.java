package com.example.gestaoconvenios.infrastructure.convenios.persistence.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import com.example.gestaoconvenios.domain.entity.convenios.Contato;
import com.example.gestaoconvenios.domain.entity.convenios.EmpresaConveniada;
import com.example.gestaoconvenios.infrastructure.convenios.persistence.entity.ContatoEntity;
import com.example.gestaoconvenios.infrastructure.convenios.persistence.entity.EmpresaConveniadaEntity;
import com.example.gestaoconvenios.infrastructure.convenios.persistence.mapper.ContatoMapper;
import com.example.gestaoconvenios.infrastructure.convenios.persistence.repository.ContatoJpaRepository;
import com.example.gestaoconvenios.infrastructure.convenios.persistence.repository.EmpresaConveniadaJpaRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class ContatoRepositoryAdapterTest {

    @Mock
    private ContatoJpaRepository contatoJpaRepository;

    @Mock
    private ContatoMapper contatoMapper;

    @Mock
    private EmpresaConveniadaJpaRepository empresaConveniadaJpaRepository;

    @InjectMocks
    private ContatoRepositoryAdapter contatoRepositoryAdapter;

    private static final Long EMPRESA_ID = 1L;
    private static final String EMAIL_TESTE = "contato@teste.com";
    private static final String EMAIL_INEXISTENTE = "naoexiste@teste.com";

    private EmpresaConveniada criarEmpresaDomain() {
        return new EmpresaConveniada(
            EMPRESA_ID,
            "Empresa XPTO",
            "12345678901234",
            "Av zona sul",
            true,
            false,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    private EmpresaConveniadaEntity criarEmpresaEntity() {
        return new EmpresaConveniadaEntity(
            EMPRESA_ID,
            "Empresa XPTO",
            "12345678901234",
            "Av zona sul",
            true,
            false,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    private Contato criarContatoDomain(EmpresaConveniada empresa) {
        return new Contato(
            1L,
            "João Silva",
            "Gerente",
            EMAIL_TESTE,
            "11999999999",
            empresa,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    private ContatoEntity criarContatoEntity(EmpresaConveniadaEntity empresa) {
        return new ContatoEntity(
            1L,
            "João Silva",
            EMAIL_TESTE,
            "Gerente",
            "11999999999",
            empresa,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
    }

    @Test
    @Description("Should return true when email exists")
    void shouldReturnTrueWhenEmailExists() {
        when(contatoJpaRepository.existsByEmail(EMAIL_TESTE)).thenReturn(true);

        boolean result = contatoRepositoryAdapter.existsByEmail(EMAIL_TESTE);

        assertThat(result).isTrue();
        verify(contatoJpaRepository).existsByEmail(EMAIL_TESTE);
    }

    @Test
    @Description("Should return false when email does not exist")
    void shouldReturnFalseWhenEmailDoesNotExist() {
        when(contatoJpaRepository.existsByEmail(EMAIL_INEXISTENTE)).thenReturn(false);

        boolean result = contatoRepositoryAdapter.existsByEmail(EMAIL_INEXISTENTE);

        assertThat(result).isFalse();
        verify(contatoJpaRepository).existsByEmail(EMAIL_INEXISTENTE);
    }

    @Test
    @Description("Should save and return a single contato")
    void shouldSaveAndReturnContato() {
        EmpresaConveniada empresaDomain = criarEmpresaDomain();
        EmpresaConveniadaEntity empresaEntity = criarEmpresaEntity();
        Contato contatoDomain = criarContatoDomain(empresaDomain);
        ContatoEntity contatoEntity = criarContatoEntity(empresaEntity);

        when(empresaConveniadaJpaRepository.getReferenceById(EMPRESA_ID)).thenReturn(empresaEntity);
        when(contatoMapper.toEntity(contatoDomain, empresaEntity)).thenReturn(contatoEntity);
        when(contatoJpaRepository.save(contatoEntity)).thenReturn(contatoEntity);
        when(contatoMapper.toDomain(contatoEntity, empresaDomain)).thenReturn(contatoDomain);

        Contato result = contatoRepositoryAdapter.save(contatoDomain);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNome()).isEqualTo("João Silva");
        assertThat(result.getEmail()).isEqualTo(EMAIL_TESTE);
        assertThat(result.getCargo()).isEqualTo("Gerente");
        assertThat(result.getTelefone()).isEqualTo("11999999999");
        assertThat(result.getEmpresaConveniada()).isEqualTo(empresaDomain);

        verify(empresaConveniadaJpaRepository).getReferenceById(EMPRESA_ID);
        verify(contatoMapper).toEntity(contatoDomain, empresaEntity);
        verify(contatoJpaRepository).save(contatoEntity);
        verify(contatoMapper).toDomain(contatoEntity, empresaDomain);
    }

    @Test
    @Description("Should throw EntityNotFoundException when empresa does not exist on save")
    void shouldThrowExceptionWhenEmpresaNotFoundOnSave() {
        EmpresaConveniada empresaDomain = criarEmpresaDomain();
        Contato contatoDomain = criarContatoDomain(empresaDomain);

        when(empresaConveniadaJpaRepository.getReferenceById(EMPRESA_ID))
            .thenThrow(new EntityNotFoundException("Empresa não encontrada"));

        assertThatThrownBy(() -> contatoRepositoryAdapter.save(contatoDomain))
            .isInstanceOf(EntityNotFoundException.class);

        verify(empresaConveniadaJpaRepository).getReferenceById(EMPRESA_ID);
    }

    @Test
    @Description("Should save all contatos when list is not empty")
    void shouldSaveAllContatosWhenListNotEmpty() {
        EmpresaConveniada empresaDomain = criarEmpresaDomain();
        EmpresaConveniadaEntity empresaEntity = criarEmpresaEntity();
        Contato contato1 = criarContatoDomain(empresaDomain);
        Contato contato2 = new Contato(
            2L, "Maria Santos", "Diretora", "maria@teste.com", "11888888888",
            empresaDomain, LocalDateTime.now(), LocalDateTime.now()
        );
        List<Contato> contatos = List.of(contato1, contato2);

        ContatoEntity entity1 = criarContatoEntity(empresaEntity);
        ContatoEntity entity2 = new ContatoEntity(
            2L, "Maria Santos", "maria@teste.com", "Diretora", "11888888888",
            empresaEntity, LocalDateTime.now(), LocalDateTime.now()
        );
        List<ContatoEntity> entities = List.of(entity1, entity2);

        when(empresaConveniadaJpaRepository.getReferenceById(anyLong())).thenReturn(empresaEntity);
        when(contatoMapper.toEntity(contato1, empresaEntity)).thenReturn(entity1);
        when(contatoMapper.toEntity(contato2, empresaEntity)).thenReturn(entity2);

        contatoRepositoryAdapter.saveAll(contatos);

        verify(empresaConveniadaJpaRepository, times(2)).getReferenceById(EMPRESA_ID);
        verify(contatoMapper).toEntity(contato1, empresaEntity);
        verify(contatoMapper).toEntity(contato2, empresaEntity);
        verify(contatoJpaRepository).saveAll(entities);
    }

    @Test
    @Description("Should call saveAll with empty list when list is empty")
    void shouldCallSaveAllWithEmptyListWhenListIsEmpty() {
        contatoRepositoryAdapter.saveAll(Collections.emptyList());

        verify(contatoJpaRepository).saveAll(Collections.emptyList());
    }

    @Test
    @Description("Should throw NullPointerException when list is null")
    void shouldThrowExceptionWhenListIsNull() {
        assertThatThrownBy(() -> contatoRepositoryAdapter.saveAll(null))
            .isInstanceOf(NullPointerException.class);
    }
}