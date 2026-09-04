package com.example.gestaoconvenios.application.contatos.exceptions;

public class ContatoAlreadyExistsException extends RuntimeException {
    public ContatoAlreadyExistsException(String email) {
        super("Contato com o email informado já está cadastrado: " + email);
    }

}
