package com.example.gestaoconvenios.application.convenios.cadastraempresa;

import java.util.List;

import com.example.gestaoconvenios.application.convenios.cadastracontato.CadastraContatoCommand;

public record CadastraEmpresaConveniadaCommand(
    String razaoSocial,
    String cnpj,
    String endereco,
    List<CadastraContatoCommand> contatos
) {}
