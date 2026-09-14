package com.example.gestaoconvenios.application.convenios.buscacontatos;

import com.example.gestaoconvenios.application.convenios.exceptions.EmpresaNotFoundException;
import com.example.gestaoconvenios.application.shared.pagination.PaginatedResult;
import com.example.gestaoconvenios.domain.entity.convenios.Contato;
import com.example.gestaoconvenios.domain.entity.convenios.EmpresaConveniada;
import com.example.gestaoconvenios.domain.repository.ContatoRepository;
import com.example.gestaoconvenios.domain.repository.EmpresaConveniadaRepository;

public class BuscaContatosUseCase {
    private ContatoRepository contatoRepository;
    private EmpresaConveniadaRepository empresaConveniadaRepository;

    public BuscaContatosUseCase(ContatoRepository contatoRepository, EmpresaConveniadaRepository empresaConveniadaRepository) {
        this.contatoRepository = contatoRepository;
        this.empresaConveniadaRepository = empresaConveniadaRepository;
    }

    public PaginatedResult<Contato> execute(BuscaContatosCommand command) {
        // TODO: talvez apenas um metodo exists pelo id já fosse o suficiente para saber se a empresa existe. já que não fazemos uso dela.
        EmpresaConveniada empresaConveniada = empresaConveniadaRepository.findById(command.empresaConveniadaId())
            .orElseThrow(() -> new EmpresaNotFoundException(command.empresaConveniadaId()));

        PaginatedResult<Contato> contatos = contatoRepository
            .findByEmpresaConveniada(empresaConveniada.getId(), command.search(), command.page(), command.size());

        return contatos;
    }
}
