package com.example.gestaoconvenios.application.convenios.exceptions;

public class EmpresaNotFoundException extends RuntimeException {
    public EmpresaNotFoundException(Long id) {
        super("Empresa conveniada não encontrada com o ID: " + id);
    }

}
