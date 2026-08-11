package com.example.gestaoconvenios.infrastructure.convenios.persistence.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "empresa_conveniada")
public class EmpresaConveniadaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 250, name = "razao_social")
    private String razaoSocial;

    @Column(nullable = false, unique = true, length = 50)
    private String cnpj;

    @Column(nullable = false, length = 250)
    private String endereco;

    @Column(nullable = false)
    private boolean ativa;

    @Column(nullable = false)
    private boolean excluida;

    @Column(nullable = false, name = "criado_em", updatable = false)
    @CreationTimestamp
    private LocalDateTime criadoEm;

    @Column(nullable = false, name = "atualizado_em")
    @UpdateTimestamp
    private LocalDateTime atualizadoEm;

    protected EmpresaConveniadaEntity() {
    }

    public EmpresaConveniadaEntity(long id, String razaoSocial, String cnpj, String endereco, boolean ativa,
            boolean excluida, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.endereco = endereco;
        this.ativa = ativa;
        this.excluida = excluida;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
    }

    public long getId() {
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

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
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
}
