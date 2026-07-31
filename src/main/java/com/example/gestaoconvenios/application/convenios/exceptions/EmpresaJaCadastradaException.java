package com.example.gestaoconvenios.application.convenios.exceptions;

public class EmpresaJaCadastradaException extends RuntimeException {
    public EmpresaJaCadastradaException(String cnpj) {
        super("Já existe uma empresa com este CNPJ no sistema: "+cnpj);
    }
}
