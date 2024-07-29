package com.exemplo.projeto.service.mapper;

import com.exemplo.projeto.dto.FilialDto;
import com.exemplo.projeto.model.Filial;

public class FilialMapper {

    public static FilialDto toDto(Filial entity) {
        if (entity == null) {
            return null;
        }

        FilialDto dto = new FilialDto();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setCnpj(entity.getCnpj());
        dto.setCidade(entity.getCidade());
        dto.setUf(entity.getUf());
        dto.setTipo(entity.getTipo());
        dto.setAtivo(entity.isAtivo());
        dto.setDataCadastro(entity.getDataCadastro());
        dto.setUltimaAtualizacao(entity.getUltimaAtualizacao());
        return dto;
    }
}
