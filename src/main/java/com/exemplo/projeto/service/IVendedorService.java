package com.exemplo.projeto.service;

import com.exemplo.projeto.dto.VendedorDto;

import java.util.List;

public interface IVendedorService {

    VendedorDto createVendedor(VendedorDto vendedorDto);

    VendedorDto getVendedorByMatricula(String matricula);

    VendedorDto updateVendedor(VendedorDto vendedorDto);

    void deleteVendedor(String matricula);
}
