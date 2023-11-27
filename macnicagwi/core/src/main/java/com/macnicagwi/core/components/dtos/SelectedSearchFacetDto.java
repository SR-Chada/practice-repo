package com.macnicagwi.core.components.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelectedSearchFacetDto {

    @NonNull
    private String facetKey;

    @NonNull
    private List<String> facetValues;
}
