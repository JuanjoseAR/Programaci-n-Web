package com.unimag.web.repositories;

import com.unimag.web.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    // Retornar un Tag si existe buscando por nombre
    Optional<Tag> findByName(String nombre);

    //Buscar todas las tags según una lista de nombres y retornar una lista de tag
    List<Tag> findByNameIn(List<String> names);
}
