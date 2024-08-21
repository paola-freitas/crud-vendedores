package com.exemplo.projeto.service;

import com.exemplo.projeto.dto.VendedorDto;
import com.exemplo.projeto.enums.TipoContratacao;
import com.exemplo.projeto.exceptions.*;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.exemplo.projeto.service.mapper.VendedorMapper.*;

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
    public void createVendedor(VendedorDto vendedorDto) {
        isValidVendedorDto(vendedorDto);

        Vendedor vendedorEntity = VendedorMapper.toEntity(vendedorDto);
        Vendedor vendedorCreated = vendedorRepository.save(vendedorEntity);
        String generatedMatricula = generateNewMatricula(vendedorCreated.getTipoContratacao(), vendedorCreated.getId());
        vendedorRepository.updateMatricula(vendedorCreated.getId(), generatedMatricula);

        vendedorRepository.existsById(vendedorCreated.getId());
    }

    @Override
    public VendedorDto getVendedorByMatricula(String matricula) {
        if (matricula == null || matricula.isEmpty()) {
            throw new InvalidValueException("Matrícula não deve ser vazia.");
        }
        if (!vendedorRepository.existsByMatricula(matricula)) {
            throw new NotFoundObjectException("Vendedor não encontrado.");
        }

        Long id = extractIdFromMatricula(matricula);
        Vendedor vendedor = vendedorRepository.findById(id)
                .orElseThrow(() -> new NotFoundObjectException("Vendedor não encontrado."));
        VendedorDto vendedorDto = VendedorMapper.toDTO(vendedor);
        filialRepository.findById(vendedor.getIdFilial())
                .ifPresent(filial -> vendedorDto.setFilial(FilialMapper.toDto(filial)));

        return vendedorDto;
    }

    @Transactional
    public VendedorDto updateVendedor(VendedorDto vendedorWillBeUpdated) {
        if (vendedorWillBeUpdated == null) {
            throw new InvalidValueException("Vendedor não deve ser vazio.");
        }

        isValidVendedorDto(vendedorWillBeUpdated);
        String matriculaToBeUpdate = vendedorWillBeUpdated.getMatricula();
        if (!vendedorRepository.existsByMatricula(matriculaToBeUpdate)) {
            return null;
        }
        Long id = extractIdFromMatricula(matriculaToBeUpdate);
        boolean vendedorExists = vendedorRepository.existsById(id);
        if (!vendedorExists) {
            throw new VendedorNotFoundException(matriculaToBeUpdate);
        }
        Vendedor vendedorWithNewValues = toUpdateAndEntity(matriculaToBeUpdate, vendedorWillBeUpdated);

        if (!vendedorWillBeUpdated.getTipoContratacao().equalsIgnoreCase(vendedorWithNewValues.getTipoContratacao().getDescriptor())) {
            String generatedMatricula = generateNewMatricula(vendedorWithNewValues.getTipoContratacao(), vendedorWithNewValues.getId());
            vendedorRepository.updateMatricula(vendedorWithNewValues.getId(), generatedMatricula);
        }
        vendedorRepository.save(vendedorWithNewValues);
        VendedorDto vendedorDto = VendedorMapper.toDTO(vendedorWithNewValues);
        filialRepository.findById(vendedorWithNewValues.getIdFilial())
                .ifPresent(filial -> vendedorDto.setFilial(FilialMapper.toDto(filial)));

        return vendedorDto;
    }

    @Override
    public boolean deleteVendedor(String matricula) {
        if (matricula == null || matricula.isEmpty()) {
            throw new InvalidValueException("Matrícula não deve ser vazia.");
        }

        Long id = extractIdFromMatricula(matricula);
        if (!vendedorRepository.existsById(id)) {
            return false;
        }
        vendedorRepository.deleteById(id);
        return true;
    }

    private void isValidVendedorDto(VendedorDto vendedorDto) {
        if (vendedorDto == null) {
            throw new InvalidValueException("Vendedor não deve ser vazio.");
        }
        isValidTipoContratacao(vendedorDto.getTipoContratacao());
        isValidDocumentoByTipoContratacao(vendedorDto.getDocumento(), vendedorDto.getTipoContratacao());
        isValidDataNascimento(vendedorDto.getDataNascimento());
        isValidEmail(vendedorDto.getEmail());
        if (vendedorDto.getFilial() == null) {
            throw new InvalidValueException("Filial não deve ser null.");
        }
    }

    private void isValidTipoContratacao(String tipoContratacao) {
        int cont = 0;
        for (TipoContratacao tc : TipoContratacao.values()) {
            if (tc.name().equalsIgnoreCase(tipoContratacao)) {
                cont++;
            }
        }
        if (cont == 0) {
            throw new InvalidValueException("Tipo contratação informada não é válida.");
        }
    }

    private void isValidDocumentoByTipoContratacao(String documento, String tipoContratacao) {
        boolean validCPF = isValidCPF(documento);
        boolean validCNPJ = isValidCNPJ(documento);

        if (tipoContratacao == null) {
            throw new InvalidValueException("Tipo contratação não deve ser null.");
        }
        if (tipoContratacao.equals(TipoContratacao.PESSOA_JURIDICA.getDescriptor().toUpperCase()) && !validCNPJ) {
            throw new InvalidValueException("Tipo contratação não condiz com o documento informado.");
        } else if ((tipoContratacao.equalsIgnoreCase(TipoContratacao.OUTSOURCING.getDescriptor())
                || tipoContratacao.equalsIgnoreCase(TipoContratacao.CLT.getDescriptor()))
                && !validCPF) {
            throw new InvalidValueException("Tipo contratação não condiz com o documento informado.");
        }
    }

    private boolean isValidCPF(String cpf) {
        return cpf != null && cpf.matches("\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}");
    }

    private boolean isValidCNPJ(String cnpj) {
        return cnpj != null && cnpj.matches("\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}");
    }

    private void isValidDataNascimento(LocalDate date) {
        if (date != null) {
            if (date.isAfter(LocalDate.now())) {
                throw new InvalidValueException("A data deve ser uma data passada.");
            }
            int idade = Period.between(date, LocalDate.now()).getYears();
            if (idade < 18) {
                throw new InvalidValueException("Deve-se ter mais de 18 anos.");
            }
        }
    }

    private void isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new InvalidValueException("O e-mail informado não é válido.");
        }
    }

    private String generateNewMatricula(TipoContratacao tipoContratacao, Long id) {
        String suffix = tipoContratacao.getSuffix();
        return id + "-" + suffix;
    }
}
