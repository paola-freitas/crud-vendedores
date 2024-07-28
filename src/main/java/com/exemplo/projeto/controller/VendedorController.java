package com.exemplo.projeto.controller;

import com.exemplo.projeto.dto.VendedorDto;
import com.exemplo.projeto.service.IVendedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/vendedores")
public class VendedorController {

    @Autowired
    private IVendedorService vendedorService;

    @PostMapping
    public ResponseEntity<Void> createVendedor(@RequestBody @Valid VendedorDto vendedorDto) {
        return vendedorService.createVendedor(vendedorDto) ?
                ResponseEntity.noContent().build() : ResponseEntity.badRequest().build();
    }

    @GetMapping("/{matricula}")
    public ResponseEntity<VendedorDto> readVendedorByMatricula(@PathVariable String matricula) {
        VendedorDto vendedorDto = vendedorService.getVendedorByMatricula(matricula);
        return vendedorDto != null ?
                ResponseEntity.ok(vendedorDto) : ResponseEntity.notFound().build();
    }

    @PutMapping
    public ResponseEntity<VendedorDto> updateVendedor(@RequestBody @Valid VendedorDto vendedorDto) {
        return ResponseEntity.ok(vendedorService.updateVendedor(vendedorDto));
    }

    @DeleteMapping("/{matricula}")
    public ResponseEntity<Void> deleteVendedor(@PathVariable String matricula) {
        return vendedorService.deleteVendedor(matricula) ?
                ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
