package com.exemplo.projeto.service.mapper;

import com.exemplo.projeto.dto.VendedorDto;
import com.exemplo.projeto.enums.TipoContratacao;
import com.exemplo.projeto.model.Vendedor;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VendedorMapperTest {

    @Test
    public void testToDTO() {
        Vendedor entity = new Vendedor(
                123L,
                "123-OUT",
                "João Silva",
                LocalDate.of(1990, 12, 12),
                "123.456.789-00",
                "joao.silva@email.com",
                TipoContratacao.OUTSOURCING,
                321L,
                "Filial teste 1"
        );

        VendedorDto dto = VendedorMapper.toDTO(entity);

        assertEquals("123-OUT", dto.getMatricula());
        assertEquals("João Silva", dto.getNome());
        assertEquals("123.456.789-00", dto.getDocumento());
        assertEquals("joao.silva@email.com", dto.getEmail());
        assertEquals("Outsourcing", dto.getTipoContratacao());
        assertEquals("1990-12-12", dto.getDataNascimento().toString());
        assertEquals(321L, dto.getIdFilial());
        assertEquals("Filial teste 1", dto.getNomeFilial());
    }

    @Test
    public void testToEntity() {
        VendedorDto dto = new VendedorDto();
        dto.setNome("Ana Maria");
        dto.setDocumento("12.123.123/0001-99");
        dto.setEmail("ana.maria@email.com.br");
        dto.setTipoContratacao(TipoContratacao.PESSOA_JURIDICA);
        dto.setDataNascimento(LocalDate.parse("1990-12-12"));
        dto.setIdFilial(123L);
        dto.setNomeFilial("Filial teste 2");

        Vendedor entity = VendedorMapper.toEntity(dto);

        assertEquals("Ana Maria", entity.getNome());
        assertEquals("12.123.123/0001-99", entity.getDocumento());
        assertEquals("ana.maria@email.com.br", entity.getEmail());
        assertEquals(TipoContratacao.PESSOA_JURIDICA, entity.getTipoContratacao());
        assertEquals(LocalDate.of(1990, 12, 12), entity.getDataNascimento());
        assertEquals(123L, entity.getIdFilial());
        assertEquals("Filial teste 2", entity.getNomeFilial());
    }
}
