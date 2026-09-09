package com.example.gestaoconvenios.application.contatos.cadastracontato;

public record CadastraContatoCommand(
    String nome, 
    String cargo, 
    String email, 
    String telefone,
    Long empresaConveniadaId
) {}
