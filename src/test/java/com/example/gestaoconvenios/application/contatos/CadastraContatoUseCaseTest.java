package com.example.gestaoconvenios.application.contatos;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import com.example.gestaoconvenios.application.contatos.exceptions.ContatoAlreadyExistsException;
import com.example.gestaoconvenios.application.convenios.exceptions.EmpresaNotFoundException;
import com.example.gestaoconvenios.domain.entity.convenios.Contato;
import com.example.gestaoconvenios.domain.entity.convenios.EmpresaConveniada;
import com.example.gestaoconvenios.domain.repository.ContatoRepository;
import com.example.gestaoconvenios.domain.repository.EmpresaConveniadaRepository;

@ExtendWith(MockitoExtension.class)
class CadastraContatoUseCaseTest {

    @Mock
    private ContatoRepository contatoRepository;

    @Mock
    private EmpresaConveniadaRepository empresaConveniadaRepository;

    @InjectMocks
    private CadastraContatoUseCase useCase;

    @Test
    @Description("Should create and return a new contato when empresa exists and email is unique")
    void shouldCreateContato() {
        EmpresaConveniada empresa = new EmpresaConveniada(
            1L, "Empresa XPTO", "12345678901234", "Rua A", true, false,
            LocalDateTime.now(), LocalDateTime.now(), new java.util.ArrayList<>(), new java.util.ArrayList<>()
        );

        CadastraContatoCommand command = new CadastraContatoCommand(
            "João", "Gerente", "joao@email.com", "11999999999", 1L
        );

        Contato contato = new Contato(1L, "João", "Gerente", "joao@email.com", "11999999999", empresa, LocalDateTime.now(), LocalDateTime.now());

        when(empresaConveniadaRepository.findById(1L)).thenReturn(Optional.of(empresa));
        when(contatoRepository.existsByEmail("joao@email.com")).thenReturn(false);
        when(contatoRepository.save(any(Contato.class))).thenReturn(contato);

        Contato result = useCase.execute(command);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNome()).isEqualTo("João");
        assertThat(result.getCargo()).isEqualTo("Gerente");
        assertThat(result.getEmail()).isEqualTo("joao@email.com");
        assertThat(result.getTelefone()).isEqualTo("11999999999");
        assertThat(result.getEmpresaConveniada()).isEqualTo(empresa);

        ArgumentCaptor<Contato> captor = ArgumentCaptor.forClass(Contato.class);
        verify(contatoRepository).save(captor.capture());

        Contato savedContato = captor.getValue();
        assertThat(savedContato.getNome()).isEqualTo("João");
        assertThat(savedContato.getCargo()).isEqualTo("Gerente");
        assertThat(savedContato.getEmail()).isEqualTo("joao@email.com");
        assertThat(savedContato.getTelefone()).isEqualTo("11999999999");
        assertThat(savedContato.getEmpresaConveniada()).isEqualTo(empresa);
    }

    @Test
    @DisplayName("Should throw EmpresaNotFoundException when empresa does not exist")
    @Description("Should throw EmpresaNotFoundException when empresaConveniadaId is not found")
    void shouldThrowEmpresaNotFoundWhenEmpresaDoesNotExist() {
        CadastraContatoCommand command = new CadastraContatoCommand(
            "João", "Gerente", "joao@email.com", "11999999999", 999L
        );

        when(empresaConveniadaRepository.findById(999L)).thenReturn(Optional.empty());

        EmpresaNotFoundException exception = assertThrows(EmpresaNotFoundException.class, () -> useCase.execute(command));

        assertThat(exception.getMessage()).isEqualTo("Empresa conveniada não encontrada com o ID: 999");
        verify(empresaConveniadaRepository).findById(999L);
    }

    @Test
    @DisplayName("Should throw ContatoAlreadyExistsException when email already exists")
    @Description("Should throw ContatoAlreadyExistsException when email is already registered")
    void shouldThrowContatoAlreadyExistsWhenEmailExists() {
        EmpresaConveniada empresa = new EmpresaConveniada(
            1L, "Empresa XPTO", "12345678901234", "Rua A", true, false,
            LocalDateTime.now(), LocalDateTime.now(), new java.util.ArrayList<>(), new java.util.ArrayList<>()
        );

        CadastraContatoCommand command = new CadastraContatoCommand(
            "João", "Gerente", "joao@email.com", "11999999999", 1L
        );

        when(empresaConveniadaRepository.findById(1L)).thenReturn(Optional.of(empresa));
        when(contatoRepository.existsByEmail("joao@email.com")).thenReturn(true);

        ContatoAlreadyExistsException exception = assertThrows(ContatoAlreadyExistsException.class, () -> useCase.execute(command));

        assertThat(exception.getMessage()).isEqualTo("Contato com o email informado já está cadastrado: joao@email.com");
        verify(empresaConveniadaRepository).findById(1L);
        verify(contatoRepository).existsByEmail("joao@email.com");
    }
}