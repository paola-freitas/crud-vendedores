package com.exemplo.projeto.service.mapper;

import com.exemplo.projeto.dto.VendedorDto;
import com.exemplo.projeto.enums.TipoContratacao;
import com.exemplo.projeto.exceptions.NotFoundObjectException;
import com.exemplo.projeto.exceptions.VendedorNotFoundException;
import com.exemplo.projeto.model.Vendedor;

public class VendedorMapper {

    public static VendedorDto toDTO(Vendedor entity) {
        if (entity == null) {
            throw new NotFoundObjectException("Vendedor não encontrado.");
        }


        VendedorDto dto = new VendedorDto();
        TipoContratacao tipoContratacao = entity.getTipoContratacao();
        dto.setNome(entity.getNome());
        dto.setDataNascimento(entity.getDataNascimento());
        dto.setDocumento(entity.getDocumento());
        dto.setEmail(entity.getEmail());
        dto.setTipoContratacao(tipoContratacao.getDescriptor());

        if (entity.getId() != null && entity.getTipoContratacao() != null) {
            String suffix = tipoContratacao.getSuffix();
            dto.setMatricula(entity.getId() + "-" + suffix);
        }

        return dto;
    }

    public static Vendedor toEntity(VendedorDto dto) {
        if (dto == null) {
            throw new NotFoundObjectException("Vendedor não encontrado.");
        }


        Vendedor entity = new Vendedor();
        entity.setNome(dto.getNome());
        entity.setDataNascimento(dto.getDataNascimento() != null ?
                dto.getDataNascimento() : null);
        entity.setDocumento(dto.getDocumento());
        entity.setEmail(dto.getEmail());
        entity.setTipoContratacao(TipoContratacao.valueOf(dto.getTipoContratacao().toUpperCase()));
        entity.setIdFilial(dto.getFilial().getId());
        return entity;
    }

    public static Vendedor toUpdateAndEntity(String matricula, VendedorDto dto) {

        Vendedor vendedorUpdated = toEntity(dto);

        vendedorUpdated.setId(extractIdFromMatricula(matricula));
        vendedorUpdated.updatedMatricula();

        return vendedorUpdated;
    }

    public static Long extractIdFromMatricula(String matricula) {
        if (matricula == null) {
            return null;
        }
        String[] parts = matricula.split("-");
        return Long.valueOf(parts[0]);
    }
}
