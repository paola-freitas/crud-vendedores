package com.exemplo.projeto.service;

import com.exemplo.projeto.dto.VendedorDto;

public interface IVendedorService {

    boolean createVendedor(VendedorDto vendedorDto);

    VendedorDto getVendedorByMatricula(String matricula);

    VendedorDto updateVendedor(VendedorDto vendedorDto);

    boolean deleteVendedor(String matricula);
}
