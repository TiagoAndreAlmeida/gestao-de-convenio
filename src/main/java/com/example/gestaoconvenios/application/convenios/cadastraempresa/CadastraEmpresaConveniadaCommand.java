package com.example.gestaoconvenios.application.convenios.cadastraempresa;

import java.util.List;

import com.example.gestaoconvenios.application.contatos.CadastraContatoCommand;

public record CadastraEmpresaConveniadaCommand(
    String razaoSocial,
    String cnpj,
    String endereco,
    List<CadastraContatoCommand> contatos
) {}
