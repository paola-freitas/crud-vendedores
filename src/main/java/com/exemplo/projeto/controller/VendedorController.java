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
        if (matricula.isEmpty()) {
            ResponseEntity.badRequest().build();
        }
        ResponseEntity.badRequest().build();
        VendedorDto vendedorDto = vendedorService.getVendedorByMatricula(matricula);
        return vendedorDto != null ?
                ResponseEntity.ok(vendedorDto) : ResponseEntity.notFound().build();
    }

    @PutMapping
    public ResponseEntity<VendedorDto> updateVendedor(@RequestBody VendedorDto vendedorDto) {
        VendedorDto result = vendedorService.updateVendedor(vendedorDto);
        return result == null ? ResponseEntity.badRequest().build() : ResponseEntity.ok(result);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteVendedor(@RequestParam String matricula) {
        if (matricula.isEmpty()) {
            ResponseEntity.badRequest().build();
        }
        return vendedorService.deleteVendedor(matricula) ?
                ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
