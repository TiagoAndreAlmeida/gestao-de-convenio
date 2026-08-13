package com.example.gestaoconvenios.domain.entity.convenios;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EmpresaConveniada {
    private Long id;
    private String razaoSocial;
    private String cnpj;
    private String endereco;
    private boolean ativa;
    private boolean excluida;
    private LocalDateTime criadaEm;
    private LocalDateTime atualizadoEm;
    List<Contato> contatos = new ArrayList<>();
    List<Contrato> contratos = new ArrayList<>();

    public EmpresaConveniada() {
    }

    public EmpresaConveniada(Long id, String razaoSocial, String cnpj, String endereco, boolean ativa, boolean excluida,
            LocalDateTime criadaEm, LocalDateTime atualizadoEm, List<Contato> contatos, List<Contrato> contratos) {
        this.id = id;
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.endereco = endereco;
        this.ativa = ativa;
        this.excluida = excluida;
        this.criadaEm = criadaEm;
        this.atualizadoEm = atualizadoEm;
        this.contatos = contatos;
        this.contratos = contratos;
    }

    public EmpresaConveniada(Long id, String razaoSocial, String cnpj, String endereco, boolean ativa, boolean excluida,
            LocalDateTime criadaEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.endereco = endereco;
        this.ativa = ativa;
        this.excluida = excluida;
        this.criadaEm = criadaEm;
        this.atualizadoEm = atualizadoEm;
    }

    public Long getId() {
        return id;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getEndereco() {
        return endereco;
    }

    public boolean getAtiva() {
        return ativa;
    }

    public boolean getExcluida() {
        return excluida;
    }

    public LocalDateTime getCriadaEm() {
        return criadaEm;
    }

    public LocalDateTime getatualizadoEm() {
        return atualizadoEm;
    }

    public List<Contato> getContatos() {
        return contatos;
    }

    public List<Contrato> getContratos() {
        return contratos;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public void setExcluida(boolean excluida) {
        this.excluida = excluida;
    }

    public void setContatos(List<Contato> contatos) {
        this.contatos = contatos;
    }

    public void setContratos(List<Contrato> contratos) {
        this.contratos = contratos;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
    
}
