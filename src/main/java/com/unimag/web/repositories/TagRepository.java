package com.unimag.web.repositories;

import com.unimag.web.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByName(String nombre);

    List<Tag> findByNameIn(List<String> names);
}
