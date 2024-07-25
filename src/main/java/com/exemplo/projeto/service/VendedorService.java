package com.exemplo.projeto.service;

import com.exemplo.projeto.dto.VendedorDto;
import com.exemplo.projeto.model.Vendedor;
import com.exemplo.projeto.repository.IVendedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendedorService implements IVendedorService {

    private final IVendedorRepository vendedorRepository;

    @Autowired
    public VendedorService(IVendedorRepository vendedorRepository) {
        this.vendedorRepository = vendedorRepository;
    }

    @Override
    public VendedorDto createVendedor(VendedorDto vendedorDto) {
        Vendedor vendedor = vendedorRepository.save(convertToEntity((vendedorDto)));
        return convertToDTO(vendedor);
    }

    @Override
    public VendedorDto getVendedorByMatricula(String matricula) {
        Long id = extractIdFromMatricula(matricula);
        Vendedor vendedor = vendedorRepository.findById(id).orElseThrow(() -> new RuntimeException("Seller not found"));
        return convertToDTO(vendedor);
    }

    @Override
    public VendedorDto updateVendedor(VendedorDto vendedorDto) {
        getVendedorByMatricula(vendedorDto.getMatricula());
        createVendedor(vendedorDto);

        return vendedorDto;
    }

    @Override
    public void deleteVendedor(String matricula) {
        Long id = extractIdFromMatricula(matricula);
        vendedorRepository.deleteById(id);
    }

    private Long extractIdFromMatricula(String matricula) {
        String[] parts = matricula.split("-");
        return Long.parseLong(parts[0]);
    }

    private VendedorDto convertToDTO(Vendedor vendedor) {
        VendedorDto vendedorDto = new VendedorDto();
        return vendedorDto;
    }

    private Vendedor convertToEntity(VendedorDto vendedorDto) {
        Long id = extractIdFromMatricula(vendedorDto.getMatricula());
        Vendedor vendedor = new Vendedor();
        vendedor.setId(id);
        vendedor.setNome(vendedorDto.getNome());
        vendedor.setDataNascimento(vendedorDto.getDataNascimento());
        vendedor.setDocumento(vendedorDto.getDocumento());
        vendedor.setTipoContratacao(vendedorDto.getTipoContratacao());
        return vendedor;
    }
}
