package com.example.gestaoconvenios.infrastructure.convenios.controller.empresa;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.gestaoconvenios.application.contatos.CadastraContatoCommand;
import com.example.gestaoconvenios.application.convenios.cadastraempresa.CadastraEmpresaConveniadaCommand;
import com.example.gestaoconvenios.application.convenios.cadastraempresa.CadastraEmpresaConveniadaUseCase;
import com.example.gestaoconvenios.domain.entity.convenios.EmpresaConveniada;
import com.example.gestaoconvenios.infrastructure.convenios.controller.empresa.request.CadastraEmpresaRequest;
import com.example.gestaoconvenios.infrastructure.convenios.controller.empresa.response.CadastraEmpresaConveniadaResponse;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/empresas")
public class EmpresaConveniadaController {
    private CadastraEmpresaConveniadaUseCase cadastraEmpresaConveniadaUseCase;

    public EmpresaConveniadaController(CadastraEmpresaConveniadaUseCase cadastraEmpresaConveniadaUseCase) {
        this.cadastraEmpresaConveniadaUseCase = cadastraEmpresaConveniadaUseCase;
    }


    @PostMapping("")
    public ResponseEntity<CadastraEmpresaConveniadaResponse> cadastraEmpresa(@Valid @RequestBody CadastraEmpresaRequest request) {
        List<CadastraContatoCommand> contatoCommand = request.contatos().stream().map(item -> new CadastraContatoCommand(item.nome(), item.cargo(), item.email(), item.telefone(), null)).toList();
        CadastraEmpresaConveniadaCommand command = new CadastraEmpresaConveniadaCommand(
            request.razaoSocial(), request.cnpj(), request.endereco(), contatoCommand
        );
        EmpresaConveniada empresa = this.cadastraEmpresaConveniadaUseCase.execute(command);
        CadastraEmpresaConveniadaResponse response = new CadastraEmpresaConveniadaResponse(
            empresa.getRazaoSocial(), empresa.getCnpj(), empresa.getEndereco(), empresa.getAtiva(), empresa.getCriadaEm(), empresa.getAtualizadaEm()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response); 
 
    }
    
}
