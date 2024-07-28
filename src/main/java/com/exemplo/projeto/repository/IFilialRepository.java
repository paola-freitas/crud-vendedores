package com.exemplo.projeto.repository;

import com.exemplo.projeto.model.Filial;
import com.exemplo.projeto.model.Vendedor;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IFilialRepository extends MongoRepository<Filial, String> {
}
