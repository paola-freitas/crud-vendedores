package com.exemplo.projeto.repository;

import com.exemplo.projeto.enums.TipoContratacao;
import com.exemplo.projeto.model.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IVendedorRepository extends JpaRepository<Vendedor, Long> {

    @Modifying
    @Query("UPDATE Vendedor v SET v.matricula = :matricula WHERE v.id = :id")
    void updateMatricula(@Param("id") Long id, @Param("matricula") String matricula);

    boolean existsByMatricula(String matricula);
}
