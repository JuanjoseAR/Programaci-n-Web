package com.unimag.web.mapper;

import com.unimag.web.domain.Tag;
import com.unimag.web.api.dto.TagDto;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {

    public TagDto.TagResponse toResponse(Tag tag) {
        if (tag == null) {
            return null;
        }
        return new TagDto.TagResponse(
                tag.getId(),
                tag.getName()
        );
    }

    public Tag toEntity(TagDto.TagCreateRequest request) {
        if (request == null) {
            return null;
        }
        Tag tag = new Tag();
        tag.setName(request.name());
        return tag;
    }
}