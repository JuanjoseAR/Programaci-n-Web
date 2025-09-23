package com.unimag.web.domine.repositories;

import com.unimag.web.domain.Tag;

import com.unimag.web.repositories.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class TagRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    private TagRepository tagRepository;

    @Test
    @DisplayName("findByName debe devolver el tag si existe")
    void testFindByName_found() {

        Tag tag = Tag.builder()
                .name("Promo")
                .build();
        tagRepository.save(tag);


        Optional<Tag> result = tagRepository.findByName("Promo");


        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Promo");
    }

    @Test
    @DisplayName("findByName debe devolver vacío si no existe")
    void testFindByName_notFound() {

        Optional<Tag> result = tagRepository.findByName("NoExiste");


        assertThat(result).isNotPresent();
    }

    @Test
    @DisplayName("findByNameIn debe devolver los tags que coincidan con los nombres")
    void testFindByNameIn() {

        Tag tag1 = Tag.builder().name("Oferta").build();
        Tag tag2 = Tag.builder().name("Internacional").build();
        Tag tag3 = Tag.builder().name("Charter").build();
        tagRepository.saveAll(List.of(tag1, tag2, tag3));


        List<Tag> result = tagRepository.findByNameIn(List.of("Oferta", "Charter"));


        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting(Tag::getName)
                .containsExactlyInAnyOrder("Oferta", "Charter");
    }
}
