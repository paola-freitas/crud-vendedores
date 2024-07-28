package com.exemplo.projeto.service;

import com.exemplo.projeto.dto.VendedorDto;
import com.exemplo.projeto.enums.TipoContratacao;
import com.exemplo.projeto.exceptions.VendedorNotFoundException;
import com.exemplo.projeto.exceptions.VendedorValidationException;
import com.exemplo.projeto.model.Vendedor;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class VendedorServiceTest {
    @Mock
    private IVendedorRepository vendedorRepository;

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

    private static VendedorDto createValidVendedorDTO() {
        VendedorDto vendedorDto = new VendedorDto();
        vendedorDto.setMatricula("123-OUT");
        vendedorDto.setNome("João Silva Atualizado");
        vendedorDto.setDataNascimento(LocalDate.parse("1990-01-31"));
        vendedorDto.setDocumento("123.456.789-00");
        vendedorDto.setEmail("joao.silva@email.com");
        vendedorDto.setTipoContratacao("OUTSOURCING");
        vendedorDto.setIdFilial(1L);
        vendedorDto.setNomeFilial("Nome Filial 1");
        return vendedorDto;
    }

    private static Vendedor createValidVendedorEntity() {
        Vendedor vendedorEntity = new Vendedor();
        vendedorEntity.setId(123L);
        vendedorEntity.setNome("João Silva");
        vendedorEntity.setDataNascimento(LocalDate.parse("1990-01-31"));
        vendedorEntity.setDocumento("123.456.789-00");
        vendedorEntity.setEmail("joao.silva@email.com");
        vendedorEntity.setTipoContratacao(TipoContratacao.OUTSOURCING);
        vendedorEntity.setIdFilial(null);
        vendedorEntity.setNomeFilial("Nome Filial 1");
        return vendedorEntity;
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
                null,
                "Nome Filial 1"
        );

        when(vendedorRepository.findById(123L)).thenReturn(existVendedor);
        when(vendedorRepository.save(existVendedor)).thenReturn(existVendedor);

        VendedorDto updatedDto = vendedorService.updateVendedor(vendedorDto);

        assertEquals("João Silva Atualizado", updatedDto.getNome());
    }

    @Test
    public void testUpdateVendedorNotFoundException() {
        VendedorDto vendedorDto = createValidVendedorDTO();

        when(vendedorRepository.findById(123L)).thenReturn(null);

        VendedorNotFoundException exception = assertThrows(VendedorNotFoundException.class, () -> {
            vendedorService.updateVendedor(vendedorDto);
        });

        assertEquals("Vendedor com matrícula 123-OUT não encontrado.", exception.getMessage());
    }



    @Test
    public void testCreateVendedorWithoutMandatoryValues() {
        VendedorDto vendedorDto = createValidVendedorDTO();
        vendedorDto.setMatricula(null);
        vendedorDto.setDataNascimento(null);
        vendedorDto.setIdFilial(null);

        Vendedor vendedorEntity = VendedorMapper.toEntity(vendedorDto);
        vendedorEntity.setId(10000001L);

        when(vendedorRepository.save(any(Vendedor.class))).thenReturn(vendedorEntity);
        when(vendedorRepository.findById(anyLong())).thenReturn(vendedorEntity);

        assertTrue(vendedorService.createVendedor(vendedorDto));
    }

    @Test
    public void testCreateVendedorWithInvalidDate() {
        VendedorDto vendedorDto = createValidVendedorDTO();
        vendedorDto.setDataNascimento(LocalDate.now().plusDays(10));

        boolean result = vendedorService.createVendedor(vendedorDto);

        assertFalse(result, "Data de nascimento deve ser uma data passada");
    }

    @Test
    public void testCreateVendedorWithInvalidTipoContratacao() {
        VendedorDto vendedorDto = createValidVendedorDTO();
        vendedorDto.setTipoContratacao("INVALIDO");

        when(vendedorRepository.save(any(Vendedor.class))).thenReturn(null);

        VendedorValidationException exception = assertThrows(VendedorValidationException.class, () -> {
            vendedorService.createVendedor(vendedorDto);
        });

        assertEquals("Tipo contratação informado está incorreto.", exception.getMessage());
    }

    /*@Test
    public void testCreateVendedorWithValidCPF() {
        VendedorDto vendedorDto = createValidVendedorDTO();
        vendedorDto.setTipoContratacao("CLT");
        vendedorDto.setDocumento("123.456.789-09");

        when(vendedorRepository.save(any(Vendedor.class))).thenReturn(null);
        when(vendedorRepository.findById(anyLong())).thenReturn(new Vendedor());

        boolean result = vendedorService.createVendedor(vendedorDto);

        assertTrue(result, "Vendedor should be created with a valid CPF");
    }*/

    @Test
    public void testCreateVendedorWithInvalidCPF() {
        VendedorDto vendedorDto = createValidVendedorDTO();
        vendedorDto.setTipoContratacao("CLT");
        vendedorDto.setDocumento("123.456.789-0X"); // CPF inválido

        boolean result = vendedorService.createVendedor(vendedorDto);

        assertFalse(result, "Vendedor should not be created with an invalid CPF");
    }

    @Test
    public void testCreateVendedorWithValidCNPJ() {
        VendedorDto vendedorDto = createValidVendedorDTO();
        vendedorDto.setTipoContratacao("PJ");
        vendedorDto.setDocumento("12.345.678/0001-95"); // CNPJ válido

        when(vendedorRepository.save(any(Vendedor.class))).thenReturn(new Vendedor());
        when(vendedorRepository.findById(anyLong())).thenReturn(new Vendedor());

        boolean result = vendedorService.createVendedor(vendedorDto);

        assertTrue(result, "Vendedor should be created with a valid CNPJ");
    }

    @Test
    public void testCreateVendedorWithInvalidCNPJ() {
        VendedorDto vendedorDto = createValidVendedorDTO();
        vendedorDto.setTipoContratacao("PJ");
        vendedorDto.setDocumento("12.345.678/0001-XX"); // CNPJ inválido

        boolean result = vendedorService.createVendedor(vendedorDto);

        assertFalse(result, "Vendedor should not be created with an invalid CNPJ");
    }


    /*

    @Test
    public void testExtractIdFromMatriculaInvalid() {
        String invalidMatricula = "123OUT";

        assertThrows(IllegalArgumentException.class, () -> {
            vendedorService.extractIdFromMatricula(invalidMatricula);
        });
    }

 */
}
