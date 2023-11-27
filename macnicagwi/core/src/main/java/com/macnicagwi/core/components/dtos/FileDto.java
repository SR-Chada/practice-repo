package com.macnicagwi.core.components.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class FileDto {

    @NonNull
    private String path;

    @NonNull
    private String damSha;

    @NonNull
    private String format;
}
