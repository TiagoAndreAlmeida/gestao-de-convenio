package com.example.gestaoconvenios.application.convenios.buscacontatos;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import com.example.gestaoconvenios.application.convenios.exceptions.EmpresaNotFoundException;
import com.example.gestaoconvenios.application.shared.pagination.PaginatedResult;
import com.example.gestaoconvenios.domain.entity.convenios.Contato;
import com.example.gestaoconvenios.domain.entity.convenios.EmpresaConveniada;
import com.example.gestaoconvenios.domain.repository.ContatoRepository;
import com.example.gestaoconvenios.domain.repository.EmpresaConveniadaRepository;

@ExtendWith(MockitoExtension.class)
class BuscaContatosUseCaseTest {

    @Mock
    private ContatoRepository contatoRepository;

    @Mock
    private EmpresaConveniadaRepository empresaConveniadaRepository;

    @Mock
    private EmpresaConveniada empresaConveniada;

    @Mock
    private PaginatedResult<Contato> paginatedResult;

    @InjectMocks
    private BuscaContatosUseCase useCase;

    @Test
    @Description("should return paginated result of contatos for a given empresa conveniada")
    void shouldReturnContatosDaEmpresa() {
        Long empresaId = 1L;
        String search = "joao";
        int page = 0;
        int size = 10;

        BuscaContatosCommand command = new BuscaContatosCommand(empresaId, search, page, size);

        when(empresaConveniadaRepository.findById(empresaId))
            .thenReturn(Optional.of(empresaConveniada));

        when(empresaConveniada.getId())
            .thenReturn(empresaId);

        when(contatoRepository.findByEmpresaConveniada(empresaId, search, page, size))
            .thenReturn(paginatedResult);

        PaginatedResult<Contato> result = useCase.execute(command);

        assertSame(paginatedResult, result);

        verify(empresaConveniadaRepository)
            .findById(empresaId);

        verify(contatoRepository)
            .findByEmpresaConveniada(
                empresaId,
                search,
                page,
                size
            );
    }

    @Test
    @Description("should throw EmpresaNotFoundException when empresa conveniada does not exist")
    void shouldThrowExceptionWhenEmpresaDoesNotExist() {
        Long empresaId = 999L;

        BuscaContatosCommand command =
            new BuscaContatosCommand(
                empresaId,
                "joao",
                0,
                10
            );

        when(empresaConveniadaRepository.findById(empresaId))
            .thenReturn(Optional.empty());

        assertThrows(
            EmpresaNotFoundException.class,
            () -> useCase.execute(command)
        );

        verify(contatoRepository, never())
            .findByEmpresaConveniada(
                empresaId,
                "joao",
                0,
                10
            );
    }
}