package com.unimag.web.services.impl;

import com.unimag.web.api.dto.TagDto;
import com.unimag.web.exception.NotFoundException;
import com.unimag.web.services.mapper.TagMapper;
import com.unimag.web.repositories.TagRepository;
import com.unimag.web.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    public TagDto.TagResponse create(TagDto.TagCreateRequest request) {
        return tagMapper.toResponse(tagRepository.save(tagMapper.toEntity(request)));
    }

    @Override
    @Transactional(readOnly = true)
    public TagDto.TagResponse findById(Long id) {
        return tagRepository.findById(id)
                .map(tagMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Tag con id " + id + " no encontrado."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagDto.TagResponse> findAll() {
        return tagRepository.findAll().stream()
                .map(tagMapper::toResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {
        tagRepository.deleteById(id);
    }
}