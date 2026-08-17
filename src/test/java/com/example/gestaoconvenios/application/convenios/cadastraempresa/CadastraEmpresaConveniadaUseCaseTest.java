package com.example.gestaoconvenios.application.convenios.cadastraempresa;

import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import com.example.gestaoconvenios.application.contatos.CadastraContatoCommand;
import com.example.gestaoconvenios.application.convenios.exceptions.EmpresaJaCadastradaException;
import com.example.gestaoconvenios.application.convenios.exceptions.EmpresaSemContatoException;
import com.example.gestaoconvenios.domain.entity.convenios.EmpresaConveniada;
import com.example.gestaoconvenios.domain.repository.ContatoRepository;
import com.example.gestaoconvenios.domain.repository.EmpresaConveniadaRepository;

@ExtendWith(MockitoExtension.class)
public class CadastraEmpresaConveniadaUseCaseTest {
    @Mock
    private EmpresaConveniadaRepository empresaConveniadaRepository;
    @Mock
    private ContatoRepository contatoRepository;

    @InjectMocks
    CadastraEmpresaConveniadaUseCase useCase;

    private static Stream<CadastraEmpresaConveniadaCommand> cadastraEmpresacommandSemContato() {
    return Stream.of(
            new CadastraEmpresaConveniadaCommand(
                    "Empresa XPTO",
                    "1234566542",
                    "Av zona sul",
                    null
            ),
            new CadastraEmpresaConveniadaCommand(
                    "Empresa XPTO",
                    "1234566542",
                    "Av zona sul",
                    List.of()
            )
        );
    }

    @Test
    @DisplayName("Should create a new empresa conveniada")
    void shouldCreateNewEmpresaConveniada() {
        CadastraContatoCommand contatoCommand = new CadastraContatoCommand(
            "João", 
            "Diretor", 
            "joao@email.com", 
            "1234567899", 
            null
        );

        CadastraEmpresaConveniadaCommand empresaCommand = new CadastraEmpresaConveniadaCommand(
            "Empresa XPTO", 
            "1234566542", 
            "Av zona sul", 
            List.of(contatoCommand)
        );

        EmpresaConveniada savedEmpresa = new EmpresaConveniada(
            1L, 
            "Empresa XPTO",
            "1234566542", 
            "Rua A", 
            true, 
            false, 
            null, 
            null
        );

        when(empresaConveniadaRepository.existsByCnpj(empresaCommand.cnpj())).thenReturn(false);
        when(empresaConveniadaRepository.save(any(EmpresaConveniada.class))).thenReturn(savedEmpresa);

        EmpresaConveniada result = useCase.execute(empresaCommand);

        assertThat(result).isNotNull();
        assertThat(result).isSameAs(savedEmpresa);
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getCnpj()).isEqualTo("1234566542");
        assertThat(result.getRazaoSocial()).isEqualTo("Empresa XPTO");
        assertThat(result.getEndereco()).isEqualTo("Rua A");
        assertThat(result.getAtiva()).isEqualTo(true);
        assertThat(result.getExcluida()).isEqualTo(false);
    }

    @ParameterizedTest
    @MethodSource("cadastraEmpresacommandSemContato")
    @Description("Should throw EmpresaSemContatoExecption when contato args is null or empy array")
    void shoudThrowEmpresaSemContatoExecption(CadastraEmpresaConveniadaCommand command) {
        EmpresaSemContatoException execption = assertThrows(EmpresaSemContatoException.class, () -> useCase.execute(command));
        assertThat(execption.getMessage()).isEqualTo("Uma empresa precisa ter pelo menos um contato");
    }

    @Test
    @DisplayName("should throw EmpresaJaCadastradaExecption")
    void shoudThrowEmpresaJaCadastradaExecption() {
        CadastraContatoCommand contatoCommand = new CadastraContatoCommand(
            "João", 
            "Diretor", 
            "joao@email.com", 
            "1234567899", 
            null
        );

        CadastraEmpresaConveniadaCommand empresaCommand = new CadastraEmpresaConveniadaCommand(
            "Empresa XPTO", 
            "1234566542", 
            "Av zona sul", 
            List.of(contatoCommand)
        );

        when(empresaConveniadaRepository.existsByCnpj(empresaCommand.cnpj())).thenReturn(true);

        EmpresaJaCadastradaException exception = assertThrows(EmpresaJaCadastradaException.class, () -> useCase.execute(empresaCommand));
        assertThat(exception.getMessage()).isEqualTo("Já existe uma empresa com este CNPJ no sistema: "+empresaCommand.cnpj());
    }
}
