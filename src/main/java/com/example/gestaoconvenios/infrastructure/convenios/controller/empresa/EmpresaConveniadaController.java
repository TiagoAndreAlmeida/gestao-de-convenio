package com.example.gestaoconvenios.infrastructure.convenios.controller.empresa;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gestaoconvenios.application.contatos.CadastraContatoCommand;
import com.example.gestaoconvenios.application.convenios.cadastraempresa.CadastraEmpresaConveniadaCommand;
import com.example.gestaoconvenios.application.convenios.cadastraempresa.CadastraEmpresaConveniadaUseCase;
import com.example.gestaoconvenios.domain.entity.convenios.EmpresaConveniada;
import com.example.gestaoconvenios.infrastructure.convenios.controller.empresa.request.CadastraEmpresaRequest;
import com.example.gestaoconvenios.infrastructure.convenios.controller.empresa.response.CadastraEmpresaConveniadaResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/empresas")
@Tag(name = "Empresas Conveniadas", description = "Endpoints para gerenciamento de empresas parceiras e convênios")
public class EmpresaConveniadaController {
    private CadastraEmpresaConveniadaUseCase cadastraEmpresaConveniadaUseCase;

    public EmpresaConveniadaController(CadastraEmpresaConveniadaUseCase cadastraEmpresaConveniadaUseCase) {
        this.cadastraEmpresaConveniadaUseCase = cadastraEmpresaConveniadaUseCase;
    }


    
    @PostMapping("")
    @Operation(
        summary = "Cadastra uma nova empresa conveniada",
        description = "Registra uma nova empresa parceira no sistema contendo obrigatoriamente um ou mais contatos iniciais associados.",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Empresa conveniada criada com sucesso",
                content = @Content(schema = @Schema(implementation = CadastraEmpresaConveniadaResponse.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Dados inválidos",
                content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Conflito: Empresa com o CNPJ informado já  está cadastrada",
                content = @Content(schema = @Schema(hidden = true))
            ),
        }
    )
    public ResponseEntity<CadastraEmpresaConveniadaResponse> cadastraEmpresa(@Valid @RequestBody CadastraEmpresaRequest request) {
        List<CadastraContatoCommand> contatoCommand = request.contatos().stream().map(item -> new CadastraContatoCommand(item.nome(), item.cargo(), item.email(), item.telefone(), null)).toList();
        CadastraEmpresaConveniadaCommand command = new CadastraEmpresaConveniadaCommand(
            request.razaoSocial(), request.cnpj(), request.endereco(), contatoCommand
        );
        EmpresaConveniada empresa = this.cadastraEmpresaConveniadaUseCase.execute(command);
        CadastraEmpresaConveniadaResponse response = new CadastraEmpresaConveniadaResponse(
            empresa.getRazaoSocial(), empresa.getCnpj(), empresa.getEndereco(), empresa.getAtiva(), empresa.getCriadoEm(), empresa.getatualizadoEm()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response); 
 
    }
    
}
