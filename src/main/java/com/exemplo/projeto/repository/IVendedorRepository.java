package com.exemplo.projeto.repository;

import com.exemplo.projeto.model.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IVendedorRepository extends JpaRepository<Vendedor, Long> {

}
