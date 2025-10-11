package com.unimag.web.services.mapper;

import com.unimag.web.api.dto.TagDto.*;
import com.unimag.web.domain.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TagMapper {


    TagResponse toResponse(Tag tag);

    Tag toEntity(TagCreateRequest request);
}
