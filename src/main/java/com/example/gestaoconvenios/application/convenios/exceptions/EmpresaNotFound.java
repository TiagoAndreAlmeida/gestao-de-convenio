package com.example.gestaoconvenios.application.convenios.exceptions;

public class EmpresaNotFound extends RuntimeException {
    public EmpresaNotFound(Long id) {
        super("Empresa conveniada não encontrada com o ID: " + id);
    }

}
