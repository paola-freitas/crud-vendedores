package com.exemplo.projeto.service;

import com.exemplo.projeto.dto.FilialDto;
import com.exemplo.projeto.exceptions.NotFoundObjectException;
import com.exemplo.projeto.model.Filial;
import com.exemplo.projeto.repository.IFilialRepository;
import com.exemplo.projeto.service.mapper.FilialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FilialService implements IFilialService{

    private final IFilialRepository filialRepository;

    @Autowired
    public FilialService(IFilialRepository filialRepository) {
        this.filialRepository = filialRepository;
    }

    public FilialDto getFilialById(Long id) {
        Optional<Filial> optionalFilial = filialRepository.findById(id);
        if (optionalFilial.isEmpty()) {
            throw new NotFoundObjectException("Filial não encontrada.");
        }
        return FilialMapper.toDto(optionalFilial.get());
    }


}
