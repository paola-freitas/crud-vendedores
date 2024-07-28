package com.exemplo.projeto.service.mapper;

import com.exemplo.projeto.dto.VendedorDto;
import com.exemplo.projeto.enums.TipoContratacao;
import com.exemplo.projeto.exceptions.VendedorNotFoundException;
import com.exemplo.projeto.model.Vendedor;

public class VendedorMapper {

    public static VendedorDto toDTO(Vendedor vendedor) {
        if (vendedor == null)
            throw new VendedorNotFoundException();
        VendedorDto dto = new VendedorDto();
        dto.setNome(vendedor.getNome());
        dto.setDataNascimento(vendedor.getDataNascimento() != null ?
                vendedor.getDataNascimento() : null);
        dto.setDocumento(vendedor.getDocumento());
        dto.setEmail(vendedor.getEmail());
        dto.setTipoContratacao(vendedor.getTipoContratacao());
        dto.setIdFilial(vendedor.getIdFilial());
        dto.setNomeFilial(vendedor.getNomeFilial());

        if (vendedor.getId() != null && vendedor.getTipoContratacao() != null) {
            String suffix = String.valueOf(vendedor.getTipoContratacao());
            dto.setMatricula(vendedor.getId() + "-" + suffix);
        }
        return dto;
    }

    public static Vendedor toEntity(VendedorDto vendedorDto) {
        if (vendedorDto == null)
            throw new VendedorNotFoundException();
        Vendedor entity = new Vendedor();

        entity.setNome(vendedorDto.getNome());
        entity.setMatricula(vendedorDto.getMatricula());
        entity.setDataNascimento(vendedorDto.getDataNascimento() != null ?
                vendedorDto.getDataNascimento() : null);
        entity.setDocumento(vendedorDto.getDocumento());
        entity.setEmail(vendedorDto.getEmail());
        entity.setTipoContratacao(vendedorDto.getTipoContratacao());
        entity.setIdFilial(vendedorDto.getIdFilial() != null ?
                vendedorDto.getIdFilial() : null);
        entity.setNomeFilial(vendedorDto.getNomeFilial());
        return entity;
    }
}
