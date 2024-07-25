package com.exemplo.projeto.controller;

import com.exemplo.projeto.dto.VendedorDto;
import com.exemplo.projeto.service.IVendedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vendedores")
public class VendedorController {

    @Autowired
    private IVendedorService vendedorService;

    @PostMapping
    public ResponseEntity<VendedorDto> createUser(@RequestBody VendedorDto vendedorDto) {
        return ResponseEntity.ok(vendedorService.createVendedor(vendedorDto));
    }

    @GetMapping("/{matricula}")
    public ResponseEntity<VendedorDto> readVendedorByMatricula(@PathVariable String matricula) {
        return ResponseEntity.ok(vendedorService.getVendedorByMatricula(matricula));
    }

    @PutMapping
    public ResponseEntity<VendedorDto> updateUser(@RequestBody VendedorDto vendedorDto) {
        return ResponseEntity.ok(vendedorService.updateVendedor(vendedorDto));
    }

    @DeleteMapping("/{matricula}")
    public ResponseEntity<Void> deleteUser(@PathVariable String matricula) {
        vendedorService.deleteVendedor(matricula);
        return ResponseEntity.noContent().build();
    }

}
