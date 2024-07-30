package com.exemplo.projeto.service;

import com.exemplo.projeto.dto.VendedorDto;
import com.exemplo.projeto.enums.TipoContratacao;
import com.exemplo.projeto.exceptions.VendedorNotFoundException;
import com.exemplo.projeto.exceptions.VendedorValidationDataNascimentoException;
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

import javax.transaction.Transactional;
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

    @Transactional
    public boolean createVendedor(VendedorDto vendedorDto) {
        if (vendedorDto == null || isValidVendedorDto(vendedorDto))
            return false;

        Vendedor vendedorEntity = VendedorMapper.toEntity(vendedorDto);
        Vendedor vendedorCreated = vendedorRepository.save(vendedorEntity);

        String generatedMatricula = generateNewMatricula(vendedorCreated.getTipoContratacao(), vendedorCreated.getId());
        vendedorRepository.updateMatricula(vendedorCreated.getId(), generatedMatricula);

        return vendedorRepository.existsById(vendedorCreated.getId());
    }

    @Override
    public VendedorDto getVendedorByMatricula(String matricula) {
        validateNotNull(matricula, "A matrícula não pode ser nula.");
        if (!vendedorRepository.existsByMatricula(matricula)) {
            return null;
        }
        Long id = extractIdFromMatricula(matricula);
        if(!vendedorRepository.existsById(id)) {
            return null;
        }
        Vendedor vendedor = vendedorRepository.findById(id)
                .orElseThrow(() -> new VendedorNotFoundException(matricula));

        validateNotNull(vendedor.getIdFilial(), "O idFilial não pode ser nulo.");
        VendedorDto vendedorDto = VendedorMapper.toDTO(vendedor);
        filialRepository.findById(vendedor.getIdFilial())
                .ifPresent(filial -> vendedorDto.setFilial(FilialMapper.toDto(filial)));

        return vendedorDto;
    }

    @Transactional
    public VendedorDto updateVendedor(VendedorDto vendedorWillBeUpdated) {
        if (vendedorWillBeUpdated == null || isValidVendedorDto(vendedorWillBeUpdated))
            return null;

        String matriculaToBeUpdate = vendedorWillBeUpdated.getMatricula();
        Long id = extractIdFromMatricula(matriculaToBeUpdate);
        boolean vendedorExists = vendedorRepository.existsById(id);
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
        vendedorWithNewValues.updatedMatricula();
        vendedorWithNewValues.setIdFilial(vendedorWillBeUpdated.getFilial().getId());

        if(vendedorWillBeUpdated.getTipoContratacao() != vendedorWithNewValues.getTipoContratacao()) {
            String generatedMatricula = generateNewMatricula(vendedorWithNewValues.getTipoContratacao(), vendedorWithNewValues.getId());
            vendedorRepository.updateMatricula(vendedorWithNewValues.getId(), generatedMatricula);
        }
        vendedorRepository.save(vendedorWithNewValues);

        return VendedorMapper.toDTO(vendedorWithNewValues);
    }

    @Override
    public boolean deleteVendedor(String matricula) {
        validateNotNull(matricula, "A matrícula não pode ser nula.");

        Long id = extractIdFromMatricula(matricula);
        if (!vendedorRepository.existsById(id)) {
            return false;
        }

        vendedorRepository.deleteById(id);
        return true;
    }

    private boolean isValidVendedorDto(VendedorDto vendedorDto) {
        return isValidTipoContratacao(vendedorDto.getTipoContratacao())
                && isValidDocumentoByTipoContratacao(vendedorDto.getDocumento(), vendedorDto.getTipoContratacao())
                && isValidDataNascimento(vendedorDto.getDataNascimento())
                && isValidEmail(vendedorDto.getEmail())
                && vendedorDto.getFilial() == null;
    }

    private boolean isValidTipoContratacao(TipoContratacao tipoContratacao) {
        try {
            TipoContratacao tipoContratacaoEnum = TipoContratacao.valueOf(String.valueOf(tipoContratacao));
        } catch (IllegalArgumentException e) {
            throw new VendedorValidationTipoContratacaoException();
        }
        return true;
    }

    private boolean isValidDocumentoByTipoContratacao(String documento, TipoContratacao tipoContratacao) {
        boolean validCPF = isValidCPF(documento);
        boolean validCNPJ = isValidCNPJ(documento);

        if(tipoContratacao == null)
            return false;

        if (tipoContratacao == TipoContratacao.PESSOA_JURIDICA && !validCNPJ) {
            throw new VendedorValidationDocumentoException();
        } else if ((tipoContratacao == TipoContratacao.OUTSOURCING || tipoContratacao == TipoContratacao.CLT)
                    && !validCPF) {
            throw new VendedorValidationDocumentoException();
        }
        return true;
    }

    private boolean isValidCPF(String cpf) {
        return cpf != null && cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
    }

    private boolean isValidCNPJ(String cnpj) {
        return cnpj != null && cnpj.matches("\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}");
    }

    private boolean isValidDataNascimento(LocalDate date) {
        if (date != null) {
            int idade = Period.between(date, LocalDate.now()).getYears();
            if (date.isAfter(LocalDate.now()) || idade <= 18) {
                throw new VendedorValidationDataNascimentoException();
            }
        }
        return true;
    }

    private boolean isValidEmail(String email) {
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

    private String extractTipoContratacaoFromMatricula(String matricula) {
        if (matricula == null)
            return null;
        String[] parts = matricula.split("-");
        return parts[1];
    }

    private String generateNewMatricula(TipoContratacao tipoContratacao, Long id) {
        String suffix = tipoContratacao.getSuffix();
        return id + "-" + suffix;
    }

    private void validateNotNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

}
