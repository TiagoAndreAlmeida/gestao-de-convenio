package com.example.gestaoconvenios.application.convenios.cadastraempresa;

import java.util.List;

import com.example.gestaoconvenios.application.convenios.exceptions.EmpresaJaCadastradaException;
import com.example.gestaoconvenios.application.convenios.exceptions.EmpresaSemContatoException;
import com.example.gestaoconvenios.domain.entity.convenios.Contato;
import com.example.gestaoconvenios.domain.entity.convenios.EmpresaConveniada;
import com.example.gestaoconvenios.domain.repository.ContatoRepository;
import com.example.gestaoconvenios.domain.repository.EmpresaConveniadaRepository;

import jakarta.transaction.Transactional;

public class CadastraEmpresaConveniadaUseCase {
    private EmpresaConveniadaRepository empresaRepository;
    private ContatoRepository contatoRepository;

    public CadastraEmpresaConveniadaUseCase(EmpresaConveniadaRepository empresaRepository, ContatoRepository contatoRepository) {
        this.empresaRepository = empresaRepository;
        this.contatoRepository = contatoRepository;
    }

    @Transactional
    public EmpresaConveniada execute(
        CadastraEmpresaConveniadaCommand command
    ) {
        if (command.contatos().isEmpty() || command.contatos() == null) {
            throw new EmpresaSemContatoException();
        }
        
        if (this.empresaRepository.existsByCnpj(command.cnpj())) {
            throw new EmpresaJaCadastradaException(command.cnpj());
        }

        EmpresaConveniada empresa = new EmpresaConveniada();
        empresa.setCnpj(command.cnpj());
        empresa.setRazaoSocial(command.razaoSocial());
        empresa.setEndereco(command.endereco());

        EmpresaConveniada saved = this.empresaRepository.save(empresa);

        List<Contato> contatos = command.contatos().stream().map(item -> {
            Contato contato = new Contato();
            contato.setNome(item.nome());
            contato.setEmail(item.email());
            contato.setTelefone(item.telefone());
            contato.setCargo(item.cargo());
            contato.setEmpresaConveniada(saved);
            return contato;
        }).toList();

        this.contatoRepository.saveAll(contatos);

        return empresa;

    }
}
