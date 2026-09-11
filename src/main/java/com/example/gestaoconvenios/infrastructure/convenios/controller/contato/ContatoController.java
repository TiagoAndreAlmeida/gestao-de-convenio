package com.example.gestaoconvenios.infrastructure.convenios.controller.contato;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gestaoconvenios.application.convenios.contatos.buscacontatos.BuscaContatosCommand;
import com.example.gestaoconvenios.application.convenios.contatos.buscacontatos.BuscaContatosUseCase;
import com.example.gestaoconvenios.application.convenios.contatos.cadastracontato.CadastraContatoCommand;
import com.example.gestaoconvenios.application.convenios.contatos.cadastracontato.CadastraContatoUseCase;
import com.example.gestaoconvenios.application.shared.pagination.PaginatedResult;
import com.example.gestaoconvenios.domain.entity.convenios.Contato;
import com.example.gestaoconvenios.infrastructure.convenios.controller.contato.request.BuscaContatosRequest;
import com.example.gestaoconvenios.infrastructure.convenios.controller.contato.request.CadastraContatoRequest;
import com.example.gestaoconvenios.infrastructure.convenios.controller.contato.response.BuscaContatoResponse;
import com.example.gestaoconvenios.infrastructure.convenios.controller.contato.response.CadastraContatoResponse;
import com.example.gestaoconvenios.infrastructure.shared.response.PaginatedResponse;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("")
@Tag(name = "Contatos", description = "Endpoints para gerenciamento de contatos de empresas conveniadas")
public class ContatoController {
    private CadastraContatoUseCase cadastraContatoUseCase;
    private BuscaContatosUseCase buscaContatosUseCase;

    public ContatoController(CadastraContatoUseCase cadastraContatoUseCase, BuscaContatosUseCase buscaContatosUseCase) {
        this.cadastraContatoUseCase = cadastraContatoUseCase;
        this.buscaContatosUseCase = buscaContatosUseCase;
    }

    @PostMapping("/empresas/{empresaId}/contatos")
    @Operation(
        summary = "Cadastra um novo contato para uma empresa conveniada",
        description = "Registra um novo contato associado a uma empresa conveniada existente no sistema.",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Contato criado com sucesso",
                content = @Content(schema = @Schema(implementation = CadastraContatoResponse.class))
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Dados inválidos",
                content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Empresa conveniada não encontrada",
                content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Conflito: Contato com o email informado já está cadastrado",
                content = @Content(schema = @Schema(hidden = true))
            )
        }
    )
    public ResponseEntity<CadastraContatoResponse> cadastraContato(
        @PathVariable Long empresaId, @Valid @RequestBody CadastraContatoRequest request
    ) {
        CadastraContatoCommand command = new CadastraContatoCommand(
            request.nome(),
            request.cargo(),
            request.email(),
            request.telefone(),
            empresaId
        );

        Contato contato = cadastraContatoUseCase.execute(command);
        CadastraContatoResponse response = new CadastraContatoResponse(
            contato.getId(),
            contato.getNome(),
            contato.getCargo(),
            contato.getEmail(),
            contato.getTelefone(),
            contato.getEmpresaConveniada().getId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/empresas/{empresaId}/contatos")
    @Operation(
        summary = "Busca contatos de uma empresa conveniada",
        description = "Retorna uma lista de contatos associados a uma empresa conveniada existente no sistema, filtrando por nome ou email.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de contatos retornada com sucesso",
                content = @Content(schema = @Schema(implementation = BuscaContatoResponse.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Empresa conveniada não encontrada",
                content = @Content(schema = @Schema(hidden = true))
            )
        }
    )
    public ResponseEntity<PaginatedResponse<BuscaContatoResponse>> buscaContatos(@PathVariable Long empresaId, @Valid @ModelAttribute BuscaContatosRequest request) {
        BuscaContatosCommand command = new BuscaContatosCommand(
            empresaId,
            request.search(),
            request.page(),
            request.size()
        );

        PaginatedResult<Contato> domainContatos = buscaContatosUseCase.execute(command);
        PaginatedResponse<BuscaContatoResponse> response = new PaginatedResponse<BuscaContatoResponse>(
            domainContatos.content().stream()
                .map(contato -> new BuscaContatoResponse(
                    contato.getId(),
                    contato.getNome(),
                    contato.getCargo(),
                    contato.getEmail(),
                    contato.getTelefone(),
                    contato.getCriadoEm(),
                    contato.getAtualizadoEm()
                ))
                .toList(),
            domainContatos.page(),
            domainContatos.size(),
            domainContatos.totalElements(),
            domainContatos.totalPages()
        );
        
        return ResponseEntity.ok(response);
    }
    
}
