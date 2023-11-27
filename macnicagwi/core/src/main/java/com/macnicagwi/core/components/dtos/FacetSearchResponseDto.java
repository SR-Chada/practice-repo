package com.macnicagwi.core.components.dtos;

import java.util.List;

import com.drew.lang.annotations.NotNull;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.macnicagwi.core.models.SearchFilter;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
@JsonInclude(Include.NON_EMPTY)
public class FacetSearchResponseDto {
    
    @NotNull
    private int searchResultsTotalCount;

    @NotNull
    private List<SearchFilter> searchFilters;

    @NotNull
    private List<?> searchResults;

}


