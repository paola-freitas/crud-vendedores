package com.exemplo.projeto.repository;

import com.exemplo.projeto.model.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public abstract class VendedorRepository implements JpaRepository<Vendedor, Long> {

}
