package com.unimag.web.services.mapper;

import com.unimag.web.api.dto.TagDto;
import com.unimag.web.domain.Tag;


public class TagMapper {

    public static TagDto.TagResponse toResponse(Tag tag) {
        if (tag == null) {
            return null;
        }
        return new TagDto.TagResponse(
                tag.getId(),
                tag.getName()
        );
    }

    public static Tag toEntity(TagDto.TagCreateRequest request) {
        if (request == null) {
            return null;
        }
        Tag tag = new Tag();
        tag.setName(request.name());
        return tag;
    }
}