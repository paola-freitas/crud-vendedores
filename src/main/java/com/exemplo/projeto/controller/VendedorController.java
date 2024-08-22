package com.exemplo.projeto.controller;

import com.exemplo.projeto.dto.VendedorDto;
import com.exemplo.projeto.service.IVendedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendedores")
public class VendedorController {

    @Autowired
    private IVendedorService vendedorService;

    @PostMapping
    public ResponseEntity<Void> createVendedor(@RequestBody VendedorDto vendedorDto) {
        vendedorService.createVendedor(vendedorDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<VendedorDto> readVendedorByMatricula(@RequestParam String matricula) {
        VendedorDto result = vendedorService.getVendedorByMatricula(matricula);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping
    public ResponseEntity<VendedorDto> updateVendedor(@RequestBody VendedorDto vendedorDto) {
        VendedorDto result = vendedorService.updateVendedor(vendedorDto);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteVendedor(@RequestParam String matricula) {
        vendedorService.deleteVendedor(matricula);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
