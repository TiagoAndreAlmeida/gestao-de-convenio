package com.example.gestaoconvenios.application.contatos;

import com.example.gestaoconvenios.application.contatos.exceptions.ContatoAlreadyExistsException;
import com.example.gestaoconvenios.application.convenios.exceptions.EmpresaNotFoundException;
import com.example.gestaoconvenios.domain.entity.convenios.Contato;
import com.example.gestaoconvenios.domain.entity.convenios.EmpresaConveniada;
import com.example.gestaoconvenios.domain.repository.ContatoRepository;
import com.example.gestaoconvenios.domain.repository.EmpresaConveniadaRepository;

public class CadastraContatoUseCase {
    private ContatoRepository contatoRepository;
    private EmpresaConveniadaRepository empresaConveniadaRepository;

    public CadastraContatoUseCase(ContatoRepository contatoRepository, EmpresaConveniadaRepository empresaConveniadaRepository) {
        this.contatoRepository = contatoRepository;
        this.empresaConveniadaRepository = empresaConveniadaRepository;
    }

    public Contato execute(CadastraContatoCommand command) {
        EmpresaConveniada empresa = this.empresaConveniadaRepository.findById(command.empresaConveniadaId())
            .orElseThrow(() -> new EmpresaNotFoundException(command.empresaConveniadaId()));

        if(contatoRepository.existsByEmail(command.email())) {
            throw new ContatoAlreadyExistsException(command.email());
        }

        Contato contato = new Contato();
        contato.setNome(command.nome());
        contato.setCargo(command.cargo());
        contato.setEmail(command.email());
        contato.setTelefone(command.telefone());
        contato.setEmpresaConveniada(empresa);

        return this.contatoRepository.save(contato);
    }

}
