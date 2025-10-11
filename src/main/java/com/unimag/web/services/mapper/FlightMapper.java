package com.unimag.web.services.mapper;

import com.unimag.web.api.dto.FlightDto.*;
import com.unimag.web.domain.Flight;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        uses = {AirlineMapper.class, AirportMapper.class, TagMapper.class}
)
public interface FlightMapper {


    FlightResponse toResponse(Flight flight);


    @Mapping(target = "airline", ignore = true)
    @Mapping(target = "origin", ignore = true)
    @Mapping(target = "destination", ignore = true)
    @Mapping(target = "tags", source = "tagIds", qualifiedByName = "mapTagIdsToEntities")
    Flight toEntity(FlightCreateRequest request);

    @Named("mapTagIdsToEntities")
    default Set<com.unimag.web.domain.Tag> mapTagIdsToEntities(Set<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) return new HashSet<>();
        return tagIds.stream()
                .map(id -> {
                    com.unimag.web.domain.Tag tag = new com.unimag.web.domain.Tag();
                    tag.setId(id);
                    return tag;
                })
                .collect(Collectors.toSet());
    }
}
