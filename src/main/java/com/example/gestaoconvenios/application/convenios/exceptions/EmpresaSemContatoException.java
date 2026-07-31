package com.example.gestaoconvenios.application.convenios.exceptions;

public class EmpresaSemContatoException extends RuntimeException {

    public EmpresaSemContatoException() {
        super("Uma empresa precisa ter pelo menos um contato");
    }
}
