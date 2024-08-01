package com.exemplo.projeto.repository;

import com.exemplo.projeto.model.Filial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IFilialRepository extends JpaRepository<Filial, Long> {
}
