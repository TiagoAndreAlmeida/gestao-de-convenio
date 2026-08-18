package com.example.gestaoconvenios.infrastructure.convenios.controller.empresa;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.gestaoconvenios.application.convenios.cadastraempresa.CadastraEmpresaConveniadaUseCase;
import com.example.gestaoconvenios.domain.entity.convenios.EmpresaConveniada;
import com.example.gestaoconvenios.infrastructure.convenios.controller.contato.request.CadastraContatoRequest;
import com.example.gestaoconvenios.infrastructure.convenios.controller.empresa.request.CadastraEmpresaRequest;
import com.example.gestaoconvenios.infrastructure.shared.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EmpresaConveniadaController.class)
@Import({GlobalExceptionHandler.class})
public class EmpresaConveniadaControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private CadastraEmpresaConveniadaUseCase useCase;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static Stream<CadastraEmpresaRequest> cadastraEmpresaRequest() {
        CadastraContatoRequest contatoRequest = new CadastraContatoRequest("", "", "", "");
        return Stream.of(
            new CadastraEmpresaRequest(null, null, null, null),
            new CadastraEmpresaRequest("", "", "", new ArrayList()),
            new CadastraEmpresaRequest("Empresa XPTO", "1234567865", "Rua z", new ArrayList()),
            new CadastraEmpresaRequest("Empresa XPTO", "1234567865", "Rua z", List.of(contatoRequest))
        );
    }
    
    @ParameterizedTest
    @MethodSource("cadastraEmpresaRequest")
    @Description("should fail with incorrect request body")
    void shoudFailwithIncorrectRequestBody(CadastraEmpresaRequest request) throws Exception {        
        mockMvc.perform(post("/empresas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @Description("should return 201 created")
    void shoudReturn201Created() throws Exception {
        CadastraContatoRequest contatoRequest = new CadastraContatoRequest("Luiz", "CEO", "luiz@email.com", "88457458");
        CadastraEmpresaRequest request = new CadastraEmpresaRequest(
            "Empresa XPTO", 
            "4567895498", 
            "Endereço X", 
            List.of(contatoRequest)
        );
        EmpresaConveniada savedEmpresa = new EmpresaConveniada(
            1L, 
            "Empresa XPTO",
            "4567895498", 
            "Endereço X", 
            true, 
            false, 
            LocalDateTime.now(), 
            LocalDateTime.now()
        );
        when(useCase.execute(any())).thenReturn(savedEmpresa);

        mockMvc.perform(post("/empresas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.razaoSocial").exists())
            .andExpect(jsonPath("$.cnpj").exists())
            .andExpect(jsonPath("$.endereco").exists())
            .andExpect(jsonPath("$.ativa").exists())
            .andExpect(jsonPath("$.criadoEm").exists())
            .andExpect(jsonPath("$.atualizadoEm").exists());
    }
}
