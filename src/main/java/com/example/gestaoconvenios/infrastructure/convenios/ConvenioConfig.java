package com.example.gestaoconvenios.infrastructure.convenios;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.gestaoconvenios.application.contatos.CadastraContatoUseCase;
import com.example.gestaoconvenios.application.convenios.cadastraempresa.CadastraEmpresaConveniadaUseCase;
import com.example.gestaoconvenios.domain.repository.ContatoRepository;
import com.example.gestaoconvenios.domain.repository.EmpresaConveniadaRepository;

@Configuration
public class ConvenioConfig {
    @Bean
    CadastraEmpresaConveniadaUseCase cadastraEmpresaConveniadaUseCase(EmpresaConveniadaRepository empresaConveniadaRepository, ContatoRepository contatoRepository) {
        return new CadastraEmpresaConveniadaUseCase(empresaConveniadaRepository, contatoRepository);
    }

    @Bean
    CadastraContatoUseCase cadastraContatoUseCase(ContatoRepository contatoRepository, EmpresaConveniadaRepository empresaConveniadaRepository) {
        return new CadastraContatoUseCase(contatoRepository, empresaConveniadaRepository);
    }
}
