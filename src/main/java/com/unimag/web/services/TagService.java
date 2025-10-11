package com.unimag.web.services;

import com.unimag.web.api.dto.TagDto;
import java.util.List;

public interface TagService {
    TagDto.TagResponse create(TagDto.TagCreateRequest request);
    TagDto.TagResponse findById(Long id);
    List<TagDto.TagResponse> findAll();
    void delete(Long id);
}
