package com.exemplo.projeto.service;

import com.exemplo.projeto.dto.FilialDto;
import com.exemplo.projeto.dto.VendedorDto;
import com.exemplo.projeto.enums.TipoContratacao;
import com.exemplo.projeto.model.Vendedor;
import com.exemplo.projeto.repository.IVendedorRepository;
import com.exemplo.projeto.service.mapper.VendedorMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class VendedorServiceTest {

    @Mock
    private IVendedorRepository vendedorRepository;

    @Mock
    private IFilialService filialService;

    @InjectMocks
    private VendedorService vendedorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateVendedor() {
        VendedorDto vendedorDto = createSampleVendedorDto();
        Vendedor vendedorEntity = createSampleVendedorEntity();

        when(vendedorRepository.save(any(Vendedor.class))).thenReturn(vendedorEntity);

        assertDoesNotThrow(() -> vendedorService.createVendedor(vendedorDto));

        verify(vendedorRepository, times(1)).save(any(Vendedor.class));
        verify(vendedorRepository, times(1)).updateMatricula(anyLong(), anyString());
        verify(vendedorRepository, times(1)).existsById(anyLong());
    }

    @Test
    void testGetVendedorByMatricula() {
        String matricula = "1-CLT";
        Vendedor vendedorEntity = createSampleVendedorEntity();

        when(vendedorRepository.findById(1L)).thenReturn(Optional.of(vendedorEntity));
        when(filialService.getFilialById(anyLong())).thenReturn(createSampleFilialDto());

        assertDoesNotThrow(() -> {
            VendedorDto result = vendedorService.getVendedorByMatricula(matricula);
            assertNotNull(result);
            assertEquals(matricula, result.getMatricula());
        });

        verify(vendedorRepository, times(1)).findById(anyLong());
        verify(filialService, times(1)).getFilialById(anyLong());
    }

    @Test
    void testUpdateVendedor() {
        VendedorDto vendedorDto = createSampleVendedorDto();
        Vendedor vendedorEntity = VendedorMapper.toEntity(vendedorDto);
        FilialDto filialDto = createSampleFilialDto();

        when(vendedorRepository.findById(1L)).thenReturn(Optional.of(vendedorEntity));
        when(vendedorRepository.saveAndFlush(any(Vendedor.class))).thenReturn(vendedorEntity);
        when(filialService.getFilialById(1L)).thenReturn(filialDto);

        assertDoesNotThrow(() -> {
            VendedorDto result = vendedorService.updateVendedor(vendedorDto);
            assertNotNull(result);
            assertEquals(vendedorDto.getMatricula(), result.getMatricula());
        });

        verify(vendedorRepository, times(1)).findById(anyLong());
        verify(vendedorRepository, times(1)).saveAndFlush(any(Vendedor.class));
        verify(filialService, times(2)).getFilialById(anyLong());
    }

    @Test
    void testDeleteVendedor() {
        String matricula = "1-CLT"; // Example matricula

        when(vendedorRepository.existsById(anyLong())).thenReturn(true);

        assertDoesNotThrow(() -> vendedorService.deleteVendedor(matricula));

        verify(vendedorRepository, times(1)).existsById(anyLong());
        verify(vendedorRepository, times(1)).deleteById(anyLong());
    }

    private VendedorDto createSampleVendedorDto() {
        VendedorDto vendedorDto = new VendedorDto();
        vendedorDto.setMatricula("1-CLT");
        vendedorDto.setNome("Fulano");
        vendedorDto.setTipoContratacao("CLT");
        vendedorDto.setDocumento("123.456.789-00");
        vendedorDto.setDataNascimento(LocalDate.of(1990, 1, 1));
        vendedorDto.setEmail("fulano@example.com");
        vendedorDto.setFilial(createSampleFilialDto());
        return vendedorDto;
    }

    private Vendedor createSampleVendedorEntity() {
        Vendedor vendedor = new Vendedor();
        vendedor.setId(1L);
        vendedor.setMatricula("1-CLT");
        vendedor.setNome("Fulano");
        vendedor.setTipoContratacao(TipoContratacao.CLT);
        vendedor.setDocumento("123.456.789-00");
        vendedor.setDataNascimento(LocalDate.of(1990, 1, 1));
        vendedor.setEmail("fulano@example.com");
        vendedor.setIdFilial(1L);
        return vendedor;
    }

    private FilialDto createSampleFilialDto() {
        FilialDto filialDto = new FilialDto();
        filialDto.setId(1L);
        filialDto.setNome("Filial Teste");
        return filialDto;
    }
}
