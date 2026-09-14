package com.example.gestaoconvenios.infrastructure.convenios.controller.contato;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.http.MediaType;

import com.example.gestaoconvenios.application.convenios.buscacontatos.BuscaContatosCommand;
import com.example.gestaoconvenios.application.convenios.buscacontatos.BuscaContatosUseCase;
import com.example.gestaoconvenios.application.convenios.cadastracontato.CadastraContatoCommand;
import com.example.gestaoconvenios.application.convenios.cadastracontato.CadastraContatoUseCase;
import com.example.gestaoconvenios.application.convenios.exceptions.ContatoAlreadyExistsException;
import com.example.gestaoconvenios.application.convenios.exceptions.EmpresaNotFoundException;
import com.example.gestaoconvenios.application.shared.pagination.PaginatedResult;
import com.example.gestaoconvenios.domain.entity.convenios.Contato;
import com.example.gestaoconvenios.domain.entity.convenios.EmpresaConveniada;
import com.example.gestaoconvenios.infrastructure.convenios.controller.contato.request.CadastraContatoRequest;
import com.example.gestaoconvenios.infrastructure.shared.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.mockito.ArgumentCaptor;

@WebMvcTest(ContatoController.class)
@Import({ GlobalExceptionHandler.class })
public class ContatoControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private CadastraContatoUseCase useCase;

    @MockitoBean
    private BuscaContatosUseCase buscaContatosUseCase;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    class CadastraContatoTests {
        private static Stream<CadastraContatoRequest> cadastraContatoRequestInvalid() {
            return Stream.of(
                    new CadastraContatoRequest("", "", "", ""),
                    new CadastraContatoRequest("João da Silva", "", "", ""),
                    new CadastraContatoRequest("João da Silva", "Gerente de Vendas", "email@teste.com", ""));
        }

        @ParameterizedTest
        @MethodSource("cadastraContatoRequestInvalid")
        @Description("should fail with incorrect request body")
        void shoudFailwithIncorrectRequestBody(CadastraContatoRequest request) throws Exception {
            mockMvc.perform(post("/empresas/1/contatos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andDo(MockMvcResultHandlers.print())
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").exists());
        }

        @Test
        @Description("should return 201 when contact is created successfully")
        void shouldReturn201WhenContactIsCreatedSuccessfully() throws Exception {
            CadastraContatoRequest request = new CadastraContatoRequest(
                    "João da Silva", "Gerente de Vendas", "email@teste.com", "5585996457841");

            EmpresaConveniada empresa = new EmpresaConveniada(
                    1L, "Empresa XPTO", "1234567865", "Rua z", true, false,
                    LocalDateTime.now(), LocalDateTime.now(), new ArrayList<>(), new ArrayList<>());

            Contato contato = new Contato(
                    1L, "João da Silva", "Gerente de Vendas", "email@teste.com", "5585996457841",
                    empresa, LocalDateTime.now(), LocalDateTime.now());

            when(useCase.execute(any(CadastraContatoCommand.class))).thenReturn(contato);

            mockMvc.perform(post("/empresas/1/contatos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.nome").value("João da Silva"))
                    .andExpect(jsonPath("$.cargo").value("Gerente de Vendas"))
                    .andExpect(jsonPath("$.email").value("email@teste.com"))
                    .andExpect(jsonPath("$.telefone").value("5585996457841"))
                    .andExpect(jsonPath("$.empresaId").value(1L));

            ArgumentCaptor<CadastraContatoCommand> captor = ArgumentCaptor.forClass(CadastraContatoCommand.class);
            verify(useCase).execute(captor.capture());

            CadastraContatoCommand capturedCommand = captor.getValue();
            assertThat(capturedCommand.empresaConveniadaId()).isEqualTo(1L);
            assertThat(capturedCommand.nome()).isEqualTo("João da Silva");
            assertThat(capturedCommand.cargo()).isEqualTo("Gerente de Vendas");
            assertThat(capturedCommand.email()).isEqualTo("email@teste.com");
            assertThat(capturedCommand.telefone()).isEqualTo("5585996457841");
        }

        @Test
        @Description("should return 409 when email already exists")
        void shouldReturn409WhenEmailAlreadyExists() throws Exception {
            CadastraContatoRequest request = new CadastraContatoRequest(
                    "João da Silva", "Gerente de Vendas", "email@teste.com", "5585996457841");

            when(useCase.execute(any())).thenThrow(new ContatoAlreadyExistsException("email@teste.com"));

            mockMvc.perform(post("/empresas/1/contatos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.message").exists());
        }

        @Test
        @Description("should return 404 when empresa not found")
        void shouldReturn404WhenEmpresaNotFound() throws Exception {
            CadastraContatoRequest request = new CadastraContatoRequest(
                    "João da Silva", "Gerente de Vendas", "email@teste.com", "5585996457841");

            when(useCase.execute(any())).thenThrow(new EmpresaNotFoundException(999L));

            mockMvc.perform(post("/empresas/999/contatos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").exists());
        }
    }

    @Nested
    class BuscaContatosTests {
        @Test
        @Description("should return paginated list of contacts when search is successful")
        void shouldReturnPaginatedListOfContactsWhenSearchIsSuccessful() throws Exception {
            Long empresaId = 1L;
            EmpresaConveniada empresa = mock(EmpresaConveniada.class);

            Contato contato = new Contato(1L, "João da Silva", "Gerente de Vendas", "email@teste.com", "5585996457841",
                    empresa, LocalDateTime.now(), LocalDateTime.now());

            PaginatedResult<Contato> result = new PaginatedResult<Contato>(List.of(contato), 0, 10, 25, 3);

            when(buscaContatosUseCase.execute(any(BuscaContatosCommand.class)))
                    .thenReturn(result);

            mockMvc.perform(get("/empresas/{empresaId}/contatos", empresaId)
                    .param("page", "0")
                    .param("size", "10")
                    .param("search", "João"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content").isArray())
                    .andExpect(jsonPath("$.content[0].id").value(contato.getId()))
                    .andExpect(jsonPath("$.content[0].nome").value(contato.getNome()))
                    .andExpect(jsonPath("$.content[0].cargo").value(contato.getCargo()))
                    .andExpect(jsonPath("$.content[0].email").value(contato.getEmail()))
                    .andExpect(jsonPath("$.content[0].telefone").value(contato.getTelefone()))
                    .andExpect(jsonPath("$.page").value(0))
                    .andExpect(jsonPath("$.size").value(10))
                    .andExpect(jsonPath("$.totalElements").value(25))
                    .andExpect(jsonPath("$.totalPages").value(3));

            ArgumentCaptor<BuscaContatosCommand> commandCaptor = ArgumentCaptor.forClass(BuscaContatosCommand.class);
            verify(buscaContatosUseCase).execute(commandCaptor.capture());
            BuscaContatosCommand command = commandCaptor.getValue();
            assertThat(command.empresaConveniadaId()).isEqualTo(empresaId);
            assertThat(command.search()).isEqualTo("João");
            assertThat(command.page()).isEqualTo(0);
            assertThat(command.size()).isEqualTo(10);
        }

        @Test
        @Description("should return 400 when page parameter is missing")
        void shouldReturnBadRequestWhenPageIsMissing() throws Exception {
            mockMvc.perform(get("/empresas/{empresaId}/contatos", 1L)
                .param("size", "10"))
                .andExpect(status().isBadRequest());

            verifyNoInteractions(buscaContatosUseCase);
        }

        @Test
        @Description("should return 400 when size parameter is missing")
        void shouldReturnBadRequestWhenSizeIsInvalid() throws Exception {
            mockMvc.perform(get("/empresas/{empresaId}/contatos", 1L)
                .param("page", "0")
                .param("size", "0"))
                .andExpect(status().isBadRequest());

            verifyNoInteractions(buscaContatosUseCase);
        }
    }

}