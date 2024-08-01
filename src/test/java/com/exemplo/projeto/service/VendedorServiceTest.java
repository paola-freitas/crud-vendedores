package com.exemplo.projeto.service;

import com.exemplo.projeto.dto.FilialDto;
import com.exemplo.projeto.dto.VendedorDto;
import com.exemplo.projeto.enums.TipoContratacao;
import com.exemplo.projeto.exceptions.VendedorNotFoundException;
import com.exemplo.projeto.exceptions.VendedorValidationDataNascimentoException;
import com.exemplo.projeto.exceptions.VendedorValidationDocumentoException;
import com.exemplo.projeto.exceptions.VendedorValidationTipoContratacaoException;
import com.exemplo.projeto.model.Filial;
import com.exemplo.projeto.model.Vendedor;
import com.exemplo.projeto.repository.IFilialRepository;
import com.exemplo.projeto.repository.IVendedorRepository;
import com.exemplo.projeto.service.mapper.VendedorMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class VendedorServiceTest {
    @Mock
    private IVendedorRepository vendedorRepository;

    @Mock
    private IFilialRepository filialRepository;

    @InjectMocks
    private VendedorService vendedorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    public static void init() {
        VendedorDto validVendedorDTO = createValidVendedorDTO();
        Vendedor validVendedorEntity = createValidVendedorEntity();
    }

    private static FilialDto createValidFilialDTO() {
        FilialDto filialDto = new FilialDto();
        filialDto.setId(1L);
        filialDto.setNome("Filial 1");
        filialDto.setCnpj("11.222.333/4444-55");
        filialDto.setCidade("Cidade");
        filialDto.setUf("UF");
        filialDto.setAtivo(true);
        filialDto.setTipo("Ativo teste");
        filialDto.setDataCadastro(LocalDate.parse("1990-12-12"));
        filialDto.setUltimaAtualizacao(LocalDate.parse("1990-12-12"));
        return filialDto;
    }

    private static Filial createValidFilialEntity() {
        Filial filialEntity = new Filial();
        filialEntity.setId(1L);
        filialEntity.setNome("Filial 1");
        filialEntity.setCnpj("11.222.333/4444-55");
        filialEntity.setCidade("Cidade");
        filialEntity.setUf("UF");
        filialEntity.setAtivo(true);
        filialEntity.setTipo("Ativo teste");
        filialEntity.setDataCadastro(LocalDate.parse("1990-12-12"));
        filialEntity.setUltimaAtualizacao(LocalDate.parse("1990-12-12"));
        return filialEntity;
    }

    private static VendedorDto createValidVendedorDTO() {
        VendedorDto vendedorDto = new VendedorDto();
        vendedorDto.setMatricula("123-OUT");
        vendedorDto.setNome("João Silva Atualizado");
        vendedorDto.setDataNascimento(LocalDate.parse("1990-01-31"));
        vendedorDto.setDocumento("123.456.789-00");
        vendedorDto.setEmail("joao.silva@email.com");
        vendedorDto.setTipoContratacao(TipoContratacao.OUTSOURCING);
        vendedorDto.setFilial(createValidFilialDTO());
        return vendedorDto;
    }

    private static Vendedor createValidVendedorEntity() {
        Vendedor vendedorEntity = new Vendedor();
        vendedorEntity.setId(123L);
        vendedorEntity.setMatricula("123-OUT");
        vendedorEntity.setNome("João Silva");
        vendedorEntity.setDataNascimento(LocalDate.parse("1990-01-31"));
        vendedorEntity.setDocumento("123.456.789-00");
        vendedorEntity.setEmail("joao.silva@email.com");
        vendedorEntity.setTipoContratacao(TipoContratacao.OUTSOURCING);
        vendedorEntity.setIdFilial(1L);
        vendedorEntity.setNomeFilial("Nome Filial 1");
        return vendedorEntity;
    }

    @Test
    public void testCreateVendedorWithoutMandatoryValues() {
        VendedorDto vendedorDto = createValidVendedorDTO();
        vendedorDto.setMatricula(null);
        vendedorDto.setDataNascimento(null);

        Vendedor vendedorEntity = VendedorMapper.toEntity(vendedorDto);
        vendedorEntity.setId(10000001L);

        when(vendedorRepository.save(any(Vendedor.class))).thenReturn(vendedorEntity);
        when(vendedorRepository.existsById(vendedorEntity.getId())).thenReturn(true);

        assertTrue(vendedorService.createVendedor(vendedorDto));
    }

    @Test
    public void testCreateVendedorWithInvalidDate() {
        VendedorDto vendedorDto = createValidVendedorDTO();
        vendedorDto.setDataNascimento(LocalDate.now().plusDays(10));

        when(vendedorRepository.save(any(Vendedor.class))).thenReturn(null);
        VendedorValidationDataNascimentoException exception = assertThrows(VendedorValidationDataNascimentoException.class, () -> {
            vendedorService.createVendedor(vendedorDto);
        });

        assertEquals("Data de nascimento possui valor inválido.", exception.getMessage());
    }

    @Test
    public void testCreateVendedorWithInvalidTipoContratacao() {
        VendedorDto vendedorDto = createValidVendedorDTO();
        vendedorDto.setTipoContratacao(null);

        when(vendedorRepository.save(any(Vendedor.class))).thenReturn(null);
        VendedorValidationTipoContratacaoException exception = assertThrows(VendedorValidationTipoContratacaoException.class, () -> {
            vendedorService.createVendedor(vendedorDto);
        });

        assertEquals("Tipo contratação informado não encontrado.", exception.getMessage());
    }

    @Test
    public void testCreateVendedorWithValidCPF() {
        VendedorDto vendedorDto = createValidVendedorDTO();
        vendedorDto.setTipoContratacao(TipoContratacao.CLT);
        vendedorDto.setDocumento("123.456.789-09");

        Vendedor vendedorEntity = VendedorMapper.toEntity(vendedorDto);
        vendedorEntity.setId(10000001L);

        when(vendedorRepository.save(any(Vendedor.class))).thenReturn(vendedorEntity);
        when(vendedorRepository.existsById(vendedorEntity.getId())).thenReturn(true);

        assertTrue(vendedorService.createVendedor(vendedorDto));
    }

    @Test
    public void testCreateVendedorWithValidCNPJButNotMatchWithTipoContratacao() {
        VendedorDto vendedorDto = createValidVendedorDTO();
        vendedorDto.setTipoContratacao(TipoContratacao.OUTSOURCING);
        vendedorDto.setDocumento("12.345.678/0001-95");

        VendedorValidationDocumentoException exception = assertThrows(VendedorValidationDocumentoException.class, () -> {
            vendedorService.createVendedor(vendedorDto);
        });

        assertEquals("Documento com formato errado ou não condiz com o tipo de contratação informado.",
                exception.getMessage());
    }

    @Test
    public void testCreateVendedorWithInvalidCNPJ() {
        VendedorDto vendedorDto = createValidVendedorDTO();
        vendedorDto.setTipoContratacao(TipoContratacao.PESSOA_JURIDICA);
        vendedorDto.setDocumento("12.345.678/0001-XX");

        VendedorValidationDocumentoException exception = assertThrows(VendedorValidationDocumentoException.class, () -> {
            vendedorService.createVendedor(vendedorDto);
        });

        assertEquals("Documento com formato errado ou não condiz com o tipo de contratação informado.",
                exception.getMessage());
    }

    @Test
    public void testReadVendedorWithValidMatricula() {
        String matricula = "123-OUT";
        Vendedor vendedor = createValidVendedorEntity();

        when(vendedorRepository.existsByMatricula(matricula)).thenReturn(true);
        when(vendedorRepository.findById(123L)).thenReturn(Optional.of(vendedor));
        when(vendedorRepository.existsById(123L)).thenReturn(true);
        when(filialRepository.existsById(1L)).thenReturn(true);
        when(filialRepository.findById(1L)).thenReturn(Optional.of(createValidFilialEntity()));

        VendedorDto vendedorDto = vendedorService.getVendedorByMatricula(matricula);

        assertEquals("João Silva", vendedorDto.getNome());
    }

    @Test
    public void testReadVendedorWithMatriculaNull() {
        String matricula = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            vendedorService.getVendedorByMatricula(matricula);
        });

        assertEquals("A matrícula não pode ser nula.",
                exception.getMessage());
    }


    @Test
    public void testUpdateVendedorExistsAndReceivesValidValues() {
        VendedorDto vendedorDto = createValidVendedorDTO();

        Vendedor existVendedor = new Vendedor(
                123L,
                "123-OUT",
                "João Silva",
                LocalDate.of(1990, 1, 31),
                "123.456.789-00",
                "email@email.com",
                TipoContratacao.OUTSOURCING,
                1L,
                "Nome Filial 1"
        );

        when(vendedorRepository.existsByMatricula("123-OUT")).thenReturn(true);
        when(vendedorRepository.existsById(existVendedor.getId())).thenReturn(true);
        when(vendedorRepository.save(existVendedor)).thenReturn(existVendedor);

        VendedorDto updatedDto = vendedorService.updateVendedor(vendedorDto);

        assertEquals("João Silva Atualizado", updatedDto.getNome());
    }

    @Test
    public void testUpdateVendedorNotFoundException() {
        VendedorDto vendedorDto = createValidVendedorDTO();

        when(vendedorRepository.existsByMatricula("123-OUT")).thenReturn(true);
        when(vendedorRepository.existsById(123L)).thenReturn(false);

        VendedorNotFoundException exception = assertThrows(VendedorNotFoundException.class, () -> {
            vendedorService.updateVendedor(vendedorDto);
        });

        assertEquals("Vendedor com matrícula 123-OUT não existe.", exception.getMessage());
    }

    @Test
    public void testDeleteVendedorWithMatriculaNull() {
        String matricula = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            vendedorService.deleteVendedor(matricula);
        });

        assertEquals("A matrícula não pode ser nula.",
                exception.getMessage());
    }

    @Test
    public void testDeleteVendedorExist() {
        String matricula = "123-OUT";

        when(vendedorRepository.existsById(123L)).thenReturn(true);
        vendedorRepository.deleteById(123L);

        boolean result = vendedorService.deleteVendedor(matricula);

        assertTrue(result);
    }
}
