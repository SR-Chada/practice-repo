package com.macnicagwi.core.components.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
public class ProductLineLogoDto {

    @NonNull
    @JsonInclude(Include.NON_EMPTY)
    private String path;

    @NonNull
    private String altText;
}
