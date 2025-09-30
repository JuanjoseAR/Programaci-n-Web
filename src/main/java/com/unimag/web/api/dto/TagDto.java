package com.unimag.web.api.dto;

import java.io.Serializable;

public class TagDto {

    public record TagCreateRequest(
            String name
    ) implements Serializable {}

    public record TagResponse(
            Long id,
            String name
    ) implements Serializable {}
}
