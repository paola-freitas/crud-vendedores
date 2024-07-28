package com.exemplo.projeto.repository;

import com.exemplo.projeto.model.Vendedor;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface IVendedorRepository extends MongoRepository<Vendedor, String> {
    Vendedor findById(Long id);
}
