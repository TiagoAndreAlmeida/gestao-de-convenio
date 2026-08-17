package com.example.gestaoconvenios.infrastructure.convenios.controller.empresa;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.gestaoconvenios.application.convenios.cadastraempresa.CadastraEmpresaConveniadaUseCase;
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
    
    @Test
    @Description("should fail with incorrect request body")
    void shoudFailwithIncorrectRequestBody() throws Exception {
        CadastraEmpresaRequest request = new CadastraEmpresaRequest(null, null, null, null);
        
        mockMvc.perform(post("/empresas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").exists());
    }
}
