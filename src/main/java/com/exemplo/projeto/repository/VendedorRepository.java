package com.exemplo.projeto.repository;

import com.exemplo.projeto.model.Vendedor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public abstract class VendedorRepository implements MongoRepository<Vendedor, String> {

}
