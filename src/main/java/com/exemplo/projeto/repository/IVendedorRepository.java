package com.exemplo.projeto.repository;

import com.exemplo.projeto.dto.VendedorDto;
import com.exemplo.projeto.model.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IVendedorRepository extends JpaRepository<Vendedor, Long> {

}
