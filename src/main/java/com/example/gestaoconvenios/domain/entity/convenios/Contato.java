package com.example.gestaoconvenios.domain.entity.convenios;

import java.time.LocalDateTime;

public class Contato {
    private Long id;
    private String nome, cargo, email, telefone;
    private EmpresaConveniada empresaConveniada;
    private LocalDateTime criadoEm, atualizadoEm;
    
    public Contato() {
    }
    
    public Contato(Long id, String nome, String cargo, String email, String telefone, EmpresaConveniada empresaConveniada,
            LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        
        this.id = id;
        this.nome = nome;
        this.cargo = cargo;
        this.email = email;
        this.telefone = telefone;
        this.empresaConveniada = empresaConveniada;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public EmpresaConveniada getEmpresaConveniada() {
        return empresaConveniada;
    }

    public void setEmpresaConveniada(EmpresaConveniada empresaConveniada) {
        this.empresaConveniada = empresaConveniada;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }
    
}
