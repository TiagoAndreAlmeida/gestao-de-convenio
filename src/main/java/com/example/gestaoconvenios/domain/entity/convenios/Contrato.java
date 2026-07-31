package com.example.gestaoconvenios.domain.entity.convenios;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Contrato {
    private LocalDate vigenciaInicial, vigenciaFinal;
    private LocalDateTime criadoEm, atualizadoEm;
    private EmpresaConveniada empresaConveniada;
    private String urlDocumentoContrato;
    
    public Contrato() {
    }
    
    public Contrato(LocalDate vigenciaInicial, LocalDate vigenciaFinal, LocalDateTime criadoEm,
            LocalDateTime atualizadoEm, EmpresaConveniada empresaConveniada, String urlDocumentoContrato) {
        this.vigenciaInicial = vigenciaInicial;
        this.vigenciaFinal = vigenciaFinal;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
        this.empresaConveniada = empresaConveniada;
        this.urlDocumentoContrato = urlDocumentoContrato;
    }

    public LocalDate getVigenciaInicial() {
        return vigenciaInicial;
    }

    public void setVigenciaInicial(LocalDate vigenciaInicial) {
        this.vigenciaInicial = vigenciaInicial;
    }

    public LocalDate getVigenciaFinal() {
        return vigenciaFinal;
    }

    public void setVigenciaFinal(LocalDate vigenciaFinal) {
        this.vigenciaFinal = vigenciaFinal;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public EmpresaConveniada getEmpresaConveniada() {
        return empresaConveniada;
    }

    public void setEmpresaConveniada(EmpresaConveniada empresaConveniada) {
        this.empresaConveniada = empresaConveniada;
    }

    public String getUrlDocumentoContrato() {
        return urlDocumentoContrato;
    }

    public void setUrlDocumentoContrato(String urlDocumentoContrato) {
        this.urlDocumentoContrato = urlDocumentoContrato;
    }

    public boolean isVirgente() {
        LocalDate now = LocalDate.now();
        return this.vigenciaInicial.isAfter(now) && this.vigenciaFinal.isBefore(now); 
    }
}
