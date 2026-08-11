package com.example.gestaoconvenios.infrastructure.convenios.persistence.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "contatos")
public class ContatoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nome;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, length = 50)
    private String cargo;

    @Column(nullable = false, length = 50)
    private String telefone;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "empresa_conveniada_id", nullable = false)
    private EmpresaConveniadaEntity empresaConveniada;

    @Column(name = "criado_em", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em", nullable = false)
    @UpdateTimestamp
    private LocalDateTime atualizadoEm;

    protected ContatoEntity() {
    }

    public ContatoEntity(Long id, String nome, String email, String cargo, String telefone,
            EmpresaConveniadaEntity empresaConveniada, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cargo = cargo;
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

    public String getEmail() {
        return email;
    }

    public String getCargo() {
        return cargo;
    }

    public String getTelefone() {
        return telefone;
    }

    public EmpresaConveniadaEntity getEmpresaConveniada() {
        return empresaConveniada;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setEmpresaConveniada(EmpresaConveniadaEntity empresaConveniada) {
        this.empresaConveniada = empresaConveniada;
    }
}
