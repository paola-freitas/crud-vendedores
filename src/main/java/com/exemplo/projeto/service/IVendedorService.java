package com.exemplo.projeto.service;

import com.exemplo.projeto.dto.VendedorDto;

public interface IVendedorService {

    void createVendedor(VendedorDto vendedorDto);

    VendedorDto getVendedorByMatricula(String matricula);

    VendedorDto updateVendedor(VendedorDto vendedorDto);

    void deleteVendedor(String matricula);
}
