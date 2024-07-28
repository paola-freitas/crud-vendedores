package com.exemplo.projeto.service;

import com.exemplo.projeto.dto.VendedorDto;
import com.exemplo.projeto.enums.TipoContratacao;
import com.exemplo.projeto.exceptions.VendedorNotFoundException;
import com.exemplo.projeto.exceptions.VendedorValidationException;
import com.exemplo.projeto.service.mapper.VendedorMapper;
import com.exemplo.projeto.model.Vendedor;
import com.exemplo.projeto.repository.IVendedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VendedorService implements IVendedorService {

    private final IVendedorRepository vendedorRepository;


    @Autowired
    public VendedorService(IVendedorRepository vendedorRepository) {
        this.vendedorRepository = vendedorRepository;
    }

    @Override
    public boolean createVendedor(VendedorDto vendedorDto) {
        if (vendedorDto == null)
            return false;

        if (validationOfTipoContratacao(vendedorDto.getTipoContratacao())
                || validationOfDocumentoByTipoContratacao(vendedorDto.getDocumento(), vendedorDto.getTipoContratacao()))
            return false;


        Vendedor vendedorEntity = VendedorMapper.toEntity(vendedorDto);

        Vendedor vendedorCreated = vendedorRepository.save(vendedorEntity);
        Vendedor vendedorWasCreated = vendedorRepository.findById(vendedorCreated.getId());
        return vendedorWasCreated != null;
    }

    @Override
    public VendedorDto getVendedorByMatricula(String matricula) {
        if (matricula == null)
            return null;
        Long id = extractIdFromMatricula(matricula);
        Vendedor vendedor = vendedorRepository.findById(id);
        return vendedor != null ? VendedorMapper.toDTO(vendedor) : null;
    }

    @Override
    public VendedorDto updateVendedor(VendedorDto vendedorUpdated) {
        String matriculaToBeUpdate = vendedorUpdated.getMatricula();
        VendedorDto vendedorExists = getVendedorByMatricula(matriculaToBeUpdate);

        if (vendedorExists == null)
            throw new VendedorNotFoundException(matriculaToBeUpdate);

        Vendedor vendedorWithNewValues = new Vendedor();
        vendedorWithNewValues.setId(extractIdFromMatricula(matriculaToBeUpdate));
        vendedorWithNewValues.setNome(vendedorUpdated.getNome());
        vendedorWithNewValues.setDataNascimento(vendedorUpdated.getDataNascimento() != null ?
                vendedorUpdated.getDataNascimento() : null);
        vendedorWithNewValues.setDocumento(vendedorUpdated.getDocumento());
        vendedorWithNewValues.setEmail(vendedorUpdated.getEmail());
        vendedorWithNewValues.setTipoContratacao(TipoContratacao.valueOf(vendedorUpdated.getTipoContratacao()));
        vendedorWithNewValues.setIdFilial(vendedorUpdated.getIdFilial());
        vendedorWithNewValues.setNomeFilial(vendedorUpdated.getNomeFilial());

        vendedorRepository.save(vendedorWithNewValues);

        return VendedorMapper.toDTO(vendedorWithNewValues);
    }

    @Override
    public boolean deleteVendedor(String matricula) {
        Long id = extractIdFromMatricula(matricula);
        if (getVendedorByMatricula(matricula) == null)
            return false;
        vendedorRepository.deleteById(String.valueOf(id));
        return true;

    }

    private boolean validationOfTipoContratacao(String tipoContratacao) {
        try {
            TipoContratacao tipoContratacaoEnum = TipoContratacao.valueOf(tipoContratacao);
        } catch (IllegalArgumentException e) {
            throw new VendedorValidationException();
        }
        return true;
    }

    private boolean validationOfDocumentoByTipoContratacao(String documento, String tipoContratacao) {
        if ("PJ".equalsIgnoreCase(tipoContratacao) || "Pessoa Juridica".equalsIgnoreCase(tipoContratacao))
            return isValidCNPJ(documento);
        else
            return isValidCPF(documento);
    }

    private boolean isValidCPF(String cpf) {
        return cpf != null && cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
    }

    private boolean isValidCNPJ(String cnpj) {
        return cnpj != null && cnpj.matches("\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}");
    }

    private Long extractIdFromMatricula(String matricula) {
        if (matricula == null)
            return null;
        String[] parts = matricula.split("-");
        return Long.valueOf(parts[0]);
    }
}
