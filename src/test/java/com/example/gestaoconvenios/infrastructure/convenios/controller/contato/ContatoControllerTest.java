package com.example.gestaoconvenios.infrastructure.convenios.controller.contato;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Stream;

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

import com.example.gestaoconvenios.application.contatos.CadastraContatoCommand;
import com.example.gestaoconvenios.application.contatos.CadastraContatoUseCase;
import com.example.gestaoconvenios.application.contatos.exceptions.ContatoAlreadyExistsException;
import com.example.gestaoconvenios.application.convenios.exceptions.EmpresaNotFoundException;
import com.example.gestaoconvenios.domain.entity.convenios.Contato;
import com.example.gestaoconvenios.domain.entity.convenios.EmpresaConveniada;
import com.example.gestaoconvenios.infrastructure.convenios.controller.contato.request.CadastraContatoRequest;
import com.example.gestaoconvenios.infrastructure.shared.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.mockito.ArgumentCaptor;

@WebMvcTest(ContatoController.class)
@Import({GlobalExceptionHandler.class})
public class ContatoControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private CadastraContatoUseCase useCase;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static Stream<CadastraContatoRequest> cadastraContatoRequestInvalid() {
        return Stream.of(
            new CadastraContatoRequest("", "", "", ""),
            new CadastraContatoRequest("João da Silva", "", "", ""),
            new CadastraContatoRequest("João da Silva", "Gerente de Vendas", "email@teste.com", "")
        );
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
            "João da Silva", "Gerente de Vendas", "email@teste.com", "5585996457841"
        );
        
        EmpresaConveniada empresa = new EmpresaConveniada(
            1L, "Empresa XPTO", "1234567865", "Rua z", true, false, 
            LocalDateTime.now(), LocalDateTime.now(), new ArrayList<>(), new ArrayList<>()
        );
        
        Contato contato = new Contato(
            1L, "João da Silva", "Gerente de Vendas", "email@teste.com", "5585996457841",
            empresa, LocalDateTime.now(), LocalDateTime.now()
        );
        
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
            "João da Silva", "Gerente de Vendas", "email@teste.com", "5585996457841"
        );
        
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
            "João da Silva", "Gerente de Vendas", "email@teste.com", "5585996457841"
        );
        
        when(useCase.execute(any())).thenThrow(new EmpresaNotFoundException(999L));

        mockMvc.perform(post("/empresas/999/contatos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }
}