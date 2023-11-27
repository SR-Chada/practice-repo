package com.macnicagwi.core.components.dtos;

import java.util.List;

import lombok.Data;

@Data
public class FacetSearchRequestDto {
	/*specifies the parent page under which the search is to be executed*/
    private String searchPageRootPath;
    /*specifies the parent asset folder under which the search is to be executed*/
    private String searchAssetRootPath;
    /*specifies the filter type (single-level/multi-level/none)*/
    private String filterType;
    /*specifies the parent tag node under which the search is to be executed*/
    private List<String> filterRootPaths;
    /*Search keyword for free text search*/
    private String searchText;
    /*Search filters selected on the client side by the site visitor*/
    private List<SelectedSearchFacetDto> selectedSearchFacets;
    /*Specifies property based on which search results need to be sorted out*/
    private String sortByProperty;
    /*Indicates the starting result of a paginated search result set*/
    private int searchResultsOffSet;
    /*Indicates the number of results to be retrieved in one search cycle*/
    private int searchResultsBlockSize;
    /* Specifies if the search facets are required to be sent as part of search response*/
    private boolean getSearchFacets;
    /* Specifies the parent content under with the search needs to be executed - Not required from client side*/
    private String contentRootPath;
    /* Specifies the response content type (Page or Asset) */
    private String contentType;
}
