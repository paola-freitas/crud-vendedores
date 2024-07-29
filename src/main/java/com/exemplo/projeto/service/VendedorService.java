package com.exemplo.projeto.service;

import com.exemplo.projeto.dto.FilialDto;
import com.exemplo.projeto.dto.VendedorDto;
import com.exemplo.projeto.enums.TipoContratacao;
import com.exemplo.projeto.exceptions.VendedorNotFoundException;
import com.exemplo.projeto.exceptions.VendedorValidationDocumentoException;
import com.exemplo.projeto.exceptions.VendedorValidationTipoContratacaoException;
import com.exemplo.projeto.model.Filial;
import com.exemplo.projeto.repository.IFilialRepository;
import com.exemplo.projeto.service.mapper.FilialMapper;
import com.exemplo.projeto.service.mapper.VendedorMapper;
import com.exemplo.projeto.model.Vendedor;
import com.exemplo.projeto.repository.IVendedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class VendedorService implements IVendedorService {

    private final IVendedorRepository vendedorRepository;
    private final IFilialRepository filialRepository;

    @Autowired
    public VendedorService(IVendedorRepository vendedorRepository, IFilialRepository filialRepository) {
        this.vendedorRepository = vendedorRepository;
        this.filialRepository = filialRepository;
    }

    @Override
    public boolean createVendedor(VendedorDto vendedorDto) {
        if (vendedorDto == null)
            return false;
        if (!validationOfTipoContratacao(vendedorDto.getTipoContratacao())
                || !validationOfDocumentoByTipoContratacao(vendedorDto.getDocumento(), vendedorDto.getTipoContratacao())
                || !validationOfDataNascimento(vendedorDto.getDataNascimento())
                || !validationOfEmail(vendedorDto.getEmail()))
            return false;

        Vendedor vendedorEntity = VendedorMapper.toEntity(vendedorDto);
        Vendedor vendedorCreated = vendedorRepository.save(vendedorEntity);
        Optional<Vendedor> vendedorWasCreated = vendedorRepository.findById(vendedorCreated.getId());

        return vendedorWasCreated.isPresent();
    }

    @Override
    public VendedorDto getVendedorByMatricula(String matricula) {
        if (matricula == null) {
            throw new IllegalArgumentException("A matrícula não pode ser nula.");
        }

        Long id = extractIdFromMatricula(matricula);
        Optional<Vendedor> vendedorOptional = vendedorRepository.findById(id);

        Vendedor vendedor = vendedorOptional.orElseThrow(() -> new VendedorNotFoundException(matricula));

        if (vendedor.getIdFilial() == null) {
            throw new IllegalArgumentException("O idFilial não pode ser nulo.");
        }

        VendedorDto vendedorDto = VendedorMapper.toDTO(vendedor);
        boolean filialExists = filialRepository.existsById(vendedor.getIdFilial());
        if (filialExists){
            Optional<Filial> filial = filialRepository.findById(vendedor.getIdFilial());
            vendedorDto.setFilial(FilialMapper.toDto(filial.get()));
        }
        return vendedorDto;
    }

    @Override
    public VendedorDto updateVendedor(VendedorDto vendedorWillBeUpdated) {
        String matriculaToBeUpdate = vendedorWillBeUpdated.getMatricula();
        boolean vendedorExists = vendedorRepository.existsById(extractIdFromMatricula(matriculaToBeUpdate));
        if (!vendedorExists)
            throw new VendedorNotFoundException(matriculaToBeUpdate);

        Vendedor vendedorWithNewValues = new Vendedor();
        vendedorWithNewValues.setId(extractIdFromMatricula(matriculaToBeUpdate));
        vendedorWithNewValues.setNome(vendedorWillBeUpdated.getNome());
        vendedorWithNewValues.setDataNascimento(vendedorWillBeUpdated.getDataNascimento() != null ?
                vendedorWillBeUpdated.getDataNascimento() : null);
        vendedorWithNewValues.setDocumento(vendedorWillBeUpdated.getDocumento());
        vendedorWithNewValues.setEmail(vendedorWillBeUpdated.getEmail());
        vendedorWithNewValues.setTipoContratacao(vendedorWillBeUpdated.getTipoContratacao());
        vendedorWithNewValues.setIdFilial(vendedorWillBeUpdated.getFilial().getId());

        vendedorRepository.save(vendedorWithNewValues);

        return VendedorMapper.toDTO(vendedorWithNewValues);
    }

    @Override
    public boolean deleteVendedor(String matricula) {
        if (matricula == null) {
            throw new IllegalArgumentException("A matrícula não pode ser nula.");
        }
        Long id = extractIdFromMatricula(matricula);
        boolean vendedorExists = vendedorRepository.existsById(id);
        if (!vendedorExists) {
            return false;
        }
        vendedorRepository.deleteById(id);
        return true;
    }

    private boolean validationOfTipoContratacao(TipoContratacao tipoContratacao) {
        try {
            TipoContratacao tipoContratacaoEnum = TipoContratacao.valueOf(String.valueOf(tipoContratacao));
        } catch (IllegalArgumentException e) {
            throw new VendedorValidationTipoContratacaoException();
        }
        return true;
    }

    private boolean validationOfDocumentoByTipoContratacao(String documento, TipoContratacao tipoContratacao) {
        boolean validCPF = validationOfCPF(documento);
        boolean validCNPJ = validationOfCNPJ(documento);

        if (tipoContratacao == TipoContratacao.PESSOA_JURIDICA && !validCNPJ) {
            throw new VendedorValidationDocumentoException();
        } else if ((tipoContratacao == TipoContratacao.OUTSOURCING || tipoContratacao == TipoContratacao.CLT)
                    && !validCPF) {
            throw new VendedorValidationDocumentoException();
        }
        return true;
    }

    private boolean validationOfCPF(String cpf) {
        return cpf != null && cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
    }

    private boolean validationOfCNPJ(String cnpj) {
        return cnpj != null && cnpj.matches("\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}");
    }

    private boolean validationOfDataNascimento(LocalDate date) {
        if (date == null)
            return true;
        if (date.isAfter(LocalDate.now())) {
            return false;
        }
        int idade = Period.between(date, LocalDate.now()).getYears();
        return idade >= 18;
    }

    private boolean validationOfEmail(String email) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private Long extractIdFromMatricula(String matricula) {
        if (matricula == null)
            return null;
        String[] parts = matricula.split("-");
        return Long.valueOf(parts[0]);
    }
}
