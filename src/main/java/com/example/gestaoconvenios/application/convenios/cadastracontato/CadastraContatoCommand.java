package com.example.gestaoconvenios.application.convenios.cadastracontato;

public record CadastraContatoCommand(
    String nome, 
    String cargo, 
    String email, 
    String telefone,
    Long empresaConveniadaId
) {}
