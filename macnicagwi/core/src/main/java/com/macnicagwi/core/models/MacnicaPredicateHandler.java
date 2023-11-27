package com.macnicagwi.core.models;

import static com.macnicagwi.core.constants.MacnicaCoreConstants.COMMA_SEPARATOR;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.DOT_SEPARATOR;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.PN_ARTICLE_DATE;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.PN_EVENT_START_DATE;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.PN_PRODUCT_MANUFACTURER_RANKING;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.PN_PUBLISHED_DATE;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.PN_QUARTER;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.PN_YEAR;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_CONTENT_TYPE;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_CONTENT_TYPE_ASSET;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_FULLTEXT;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_LIMIT;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_OFFSET;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_ORDER_BY;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_ORDER_BY_ASSET_TITLE;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_ORDER_BY_PATH;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_ORDER_BY_SORT;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_ORDER_BY_TITLE;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_ORDER_SORT_ASC;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_ORDER_SORT_DESC;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_PATH;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.macnicagwi.core.components.models.impl.MultiLevelFilterImpl;
import com.macnicagwi.core.constants.MacnicaCoreConstants;

import lombok.Getter;

/**
 * Simple implementation for generating JCR Search predicate map from
 * {@link SlingHttpServletRequest} and {@link Resource}objects
 * 
 */
@Model(adaptables = SlingHttpServletRequest.class)
public class MacnicaPredicateHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MacnicaPredicateHandler.class);
    private static final String FACET_TAG = "t";
    private static final String FACET_QUERY_STRING = "q";
    private static final String PAGINATION_FROM = "f";
    private static final String SEARCH_PROPERTY = "_property";
    private static final String SEARCH_VALUE = "_value";

    private static final String NP_JCR_CONTENT = "@jcr:content/";
    private static final String NP_ASSET_METADATA = "@jcr:content/metadata/";
    public static final String SEARCH_CONTENT_TYPE_PAGE = "cq:Page";

    /**
     * Default Maximum number of list items to be fetched.
     */
    private static final int DEFAULT_MAX_ITEMS = 10;

    /**
     * Default search type
     */
    private static final String DEFAULT_SEARCH_CONTENT_TYPE = "both";

    /**
     * The current {@link SlingHttpServletRequest}.
     */
    @Self
    private SlingHttpServletRequest request;

    @Inject
    @Optional
    private String rootPath;

    @Inject
    @Optional
    private String directory;
    
    @Inject
    @Optional
    private String secondaryrootPath;

    @Inject
    @Optional
    private String secondarydirectory;

    @Inject
    @Default(intValues = DEFAULT_MAX_ITEMS)
    private Integer maxItems;

    @Inject
    @Default(values = DEFAULT_SEARCH_CONTENT_TYPE)
    private String searchContentType;

    @Inject
    @Optional
    @Default(values="path")
    private String orderBy;

    @Inject
    @Optional
    @Default(values="asc")
    private String sortOrder;

    @ScriptVariable
    private Page currentPage;

    @Getter
    private Map<String, String> predicates = new HashMap<>();

    private String searchRoot;

    private Integer offset;

    private String tagFilters;

    @Getter
    private String queryString;
    
	@ValueMapValue
	@Nullable
	@Optional
	private String listFrom;
	
	@ValueMapValue
	@Nullable
	@Optional
	private String parentPage;
	
	@ValueMapValue
	@Nullable
	@Optional
	private String searchIn;
    
    
    @ValueMapValue
    @Optional
    @Default(values=StringUtils.EMPTY)
    private String query;

    /**
     * Content Types.
     */
    protected enum SearchContentType {
        ASSET("asset"),
        PAGE("page"),
        ALL("all");

        private final String value;

        SearchContentType(String value) {
            this.value = value;
        }

        /**
         * Get the content type from the string value.
         *
         * @param value The value.
         * @return The content type if it exists, or null if no content type has a
         *         matching value.
         */
        @Nullable
        public static SearchContentType fromString(String value) {
            for (SearchContentType s : values()) {
                if (StringUtils.equals(value, s.value)) {
                    return s;
                }
            }
            return null;
        }
    }

    /**
     * Sort orders.
     */
    private enum SortOrder {
        /**
         * Ascending.
         */
        ASC(SEARCH_ORDER_SORT_ASC),

        /**
         * Descending.
         */
        DESC(SEARCH_ORDER_SORT_DESC);

        private final String value;

        SortOrder(String value) {
            this.value = value;
        }

        /**
         * Get the sort order from string value.
         *
         * @param value The string value.
         * @return The sort order, or ascending if not found.
         */
        @NotNull
        public static SortOrder fromString(String value) {
            for (SortOrder s : values()) {
                if (StringUtils.equals(value, s.value)) {
                    return s;
                }
            }
            return ASC;
        }
    }

    /**
     * Order by options.
     */
    private enum OrderBy {
        /**
         * Order by page title.
         */
        TITLE(SEARCH_ORDER_BY_TITLE),
        /**
         * Order by asset title.
         */
        ASSET_TITLE(SEARCH_ORDER_BY_ASSET_TITLE),

        /**
         * Order by last article date (applicable for news and technical articles).
         */
        ARTICLE_DATE(PN_ARTICLE_DATE),

        /**
         * Order by publish date (Macnica GWI custom asset metadata property. Not to be
         * confused by asset's published date).
         */
        PUBLISH_DATE(PN_PUBLISHED_DATE),

        /**
         * Order by event start date page property (applicable for event detail pages).
         */
        EVENT_START_DATE(PN_EVENT_START_DATE),

        /**
         * Order by year (custom asset metadata property and page property).
         */
        YEAR(PN_YEAR),
        /**
         * Order by quarter (custom asset metadata property and page property).
         */
        QUARTER(PN_QUARTER),

        /**
         * Order by resource path.
         */
        PATH(SEARCH_ORDER_BY_PATH),
        
        /**
         * Order by resource path.
         */
        PRODUCT_MANUFACTURER_RANKING(PN_PRODUCT_MANUFACTURER_RANKING);

        private final String value;

        OrderBy(String value) {
            this.value = value;
        }

        /**
         * Get the order by from string value.
         *
         * @param value The string value.
         * @return The order by field, or null if not found.
         */
        @Nullable
        public static OrderBy fromString(String value) {
            for (OrderBy s : values()) {
                if (StringUtils.equals(value, s.value)) {
                    return s;
                }
            }
            return null;
        }
    }

    
    @PostConstruct
    public void init() {
        try {
            LOGGER.trace("Initializing predicate handler");
            offset = NumberUtils.toInt(request.getParameter(PAGINATION_FROM), 0);
            tagFilters = request.getParameter(FACET_TAG);
            queryString = request.getParameter(FACET_QUERY_STRING);
            if (getSearchContentType().value.equalsIgnoreCase(SearchContentType.ALL.value)) {
                if (StringUtils.isBlank(queryString) || StringUtils.isBlank(rootPath)
                        || StringUtils.isBlank(directory)) {
                    LOGGER.warn(
                            "Unable to Generate Predicate Map for Search Results. Query String {}, Page Root Path {}, Directory {} ",
                            queryString, rootPath, directory);
                    return;
                }
                generateFreeTextSearchPredicates();
                return;
            }
            searchRoot = StringUtils.isNotBlank(rootPath) ? rootPath : directory;
            if (StringUtils.isBlank(searchRoot)) {
                LOGGER.error("Search Root Not found for resource {}", request.getResource().getPath());
                return;
            }
            generatePredicates();
        } catch (Exception e) {
            LOGGER.error("Error in initializing predicate handler", e);
        }
    }
    
    public Map<String,String> getProductListPredicates(){
        if(StringUtils.isBlank(listFrom) || listFrom.equals("static") || listFrom.equals("tags")) {
            return Collections.emptyMap();
        }
        String path =currentPage.getPath();
        int counter =0;
        Map<String,String> productListPredicates = new LinkedHashMap<>();
        if(listFrom.equals("children")){
        	path = StringUtils.isBlank(parentPage)?currentPage.getPath():parentPage;
        } else if(listFrom.equals("search") && StringUtils.isNotBlank(query)){
        	path = StringUtils.isBlank(searchIn)?currentPage.getPath():searchIn;
        	productListPredicates.put(SEARCH_FULLTEXT, query);
        } else { return Collections.emptyMap();}
        productListPredicates.put(SEARCH_PATH, path);
        productListPredicates.put(SEARCH_CONTENT_TYPE, SEARCH_CONTENT_TYPE_PAGE);
        productListPredicates.put(SEARCH_LIMIT, ""+maxItems);
        productListPredicates.put(SEARCH_ORDER_BY, NP_JCR_CONTENT + getOrderBy().value);
        if (getOrderBy() != OrderBy.PATH) {
        	productListPredicates.put(SEARCH_ORDER_BY_SORT, sortOrder);
        }
       
        
        if(StringUtils.isNotBlank(queryString)){
        	productListPredicates.put(SEARCH_FULLTEXT, queryString);
        	productListPredicates.put(SEARCH_CONTENT_TYPE, SEARCH_CONTENT_TYPE_PAGE);
            setSearchContentTypePredicate();        
            productListPredicates.put(counter + SEARCH_PROPERTY, NP_JCR_CONTENT + MacnicaCoreConstants.PN_PAGE_TYPE);
            productListPredicates.put(counter + SEARCH_PROPERTY + DOT_SEPARATOR + counter+ SEARCH_VALUE, MacnicaCoreConstants.PAGE_TYPE_MANUFACTURER_LANDING);

            counter++;
            Map<String,List<String>> tagFilterMap = getFilterTagMap();
            for (Entry<String, List<String>> entry : tagFilterMap.entrySet()) {
            	productListPredicates.put(counter + SEARCH_PROPERTY, NP_JCR_CONTENT + entry.getKey());
                List<String> values = entry.getValue();
                for (int i = 0; i < values.size(); i++) {
                	productListPredicates.put(counter + SEARCH_PROPERTY + DOT_SEPARATOR + (i + 1) + SEARCH_VALUE, values.get(i));
                }
                counter++;
            }
            
            productListPredicates.put(SEARCH_OFFSET, "" + offset);
            productListPredicates.put(SEARCH_LIMIT, "" + maxItems);
        } else {
            Map<String,List<String>> tagFilterMap = getFilterTagMap();
            for (Entry<String, List<String>> entry : tagFilterMap.entrySet()) {
            	productListPredicates.put(counter + SEARCH_PROPERTY, NP_JCR_CONTENT + entry.getKey());
                List<String> values = entry.getValue();
                for (int i = 0; i < values.size(); i++) {
                	productListPredicates.put(counter + SEARCH_PROPERTY + DOT_SEPARATOR + (i + 1) + SEARCH_VALUE, values.get(i));
                }
                counter++;
            }
            productListPredicates.put(SEARCH_ORDER_BY, NP_JCR_CONTENT + getOrderBy().value);
            if (getOrderBy() != OrderBy.PATH) {
            	productListPredicates.put(SEARCH_ORDER_BY_SORT, getSortOrder().value);
            }
            productListPredicates.put(SEARCH_OFFSET, "" + offset);
            productListPredicates.put(SEARCH_LIMIT, "" + maxItems);
        }
        
        LOGGER.info("Product line list predicates {} ", productListPredicates);
        
        return productListPredicates;

    	
    }
    public Map<String,String> getManufacturerListTitlePredicates(){
        if(StringUtils.isBlank(searchRoot)) {
            return Collections.emptyMap();
        }
        Map<String,String> manufacturerListPredicates = new LinkedHashMap<>();
        manufacturerListPredicates.put(SEARCH_PATH, searchRoot);
        manufacturerListPredicates.put(MacnicaCoreConstants.SEARCH_CONTENT_TYPE, "cq:page");
        manufacturerListPredicates.put("property", "@jcr:content/pageType");
        manufacturerListPredicates.put("property.value", MacnicaCoreConstants.PAGE_TYPE_MANUFACTURER_LANDING);
        manufacturerListPredicates.put(MacnicaCoreConstants.SEARCH_LIMIT, MacnicaCoreConstants.SEARCH_ALL_RESULTS);
        return manufacturerListPredicates;
    }
    public Map<String,String> getManufacturerListPredicates(){
        if(StringUtils.isBlank(searchRoot)) {
            return Collections.emptyMap();
        }
        int counter =0;
        Map<String,String> manufacturerListPredicates = new LinkedHashMap<>();
        manufacturerListPredicates.put(SEARCH_PATH, searchRoot);

        if(StringUtils.isNotBlank(queryString)){
            manufacturerListPredicates.put(SEARCH_FULLTEXT, queryString);
            manufacturerListPredicates.put(SEARCH_CONTENT_TYPE, SEARCH_CONTENT_TYPE_PAGE);
            setSearchContentTypePredicate();        
            manufacturerListPredicates.put(counter + SEARCH_PROPERTY, NP_JCR_CONTENT + MacnicaCoreConstants.PN_PAGE_TYPE);
            manufacturerListPredicates.put(counter + SEARCH_PROPERTY + DOT_SEPARATOR + counter+ SEARCH_VALUE, MacnicaCoreConstants.PAGE_TYPE_MANUFACTURER_LANDING);

            counter++;
            Map<String,List<String>> tagFilterMap = getFilterTagMap();
            for (Entry<String, List<String>> entry : tagFilterMap.entrySet()) {
                manufacturerListPredicates.put(counter + SEARCH_PROPERTY, NP_JCR_CONTENT + entry.getKey());
                List<String> values = entry.getValue();
                for (int i = 0; i < values.size(); i++) {
                    manufacturerListPredicates.put(counter + SEARCH_PROPERTY + DOT_SEPARATOR + (i + 1) + SEARCH_VALUE, values.get(i));
                }
                counter++;
            }
            
            manufacturerListPredicates.put(SEARCH_OFFSET, "" + offset);
            manufacturerListPredicates.put(SEARCH_LIMIT, "" + maxItems);
        } else {
            manufacturerListPredicates.put(SEARCH_CONTENT_TYPE, SEARCH_CONTENT_TYPE_PAGE);
            setSearchContentTypePredicate();        
            manufacturerListPredicates.put(counter + SEARCH_PROPERTY, NP_JCR_CONTENT + MacnicaCoreConstants.PN_PAGE_TYPE);
            manufacturerListPredicates.put(counter + SEARCH_PROPERTY + DOT_SEPARATOR + counter+ SEARCH_VALUE, MacnicaCoreConstants.PAGE_TYPE_MANUFACTURER_LANDING);
            counter++;
            Map<String,List<String>> tagFilterMap = getFilterTagMap();
            for (Entry<String, List<String>> entry : tagFilterMap.entrySet()) {
                manufacturerListPredicates.put(counter + SEARCH_PROPERTY, NP_JCR_CONTENT + entry.getKey());
                List<String> values = entry.getValue();
                for (int i = 0; i < values.size(); i++) {
                    manufacturerListPredicates.put(counter + SEARCH_PROPERTY + DOT_SEPARATOR + (i + 1) + SEARCH_VALUE, values.get(i));
                }
                counter++;
            }
            manufacturerListPredicates.put(SEARCH_ORDER_BY, NP_JCR_CONTENT + getOrderBy().value);
            if (getOrderBy() != OrderBy.PATH) {
                manufacturerListPredicates.put(SEARCH_ORDER_BY_SORT, getSortOrder().value);
            }
            manufacturerListPredicates.put(SEARCH_OFFSET, "" + offset);
            manufacturerListPredicates.put(SEARCH_LIMIT, "" + maxItems);
        }
    
        return manufacturerListPredicates;
    }
    
    private void generateFreeTextSearchPredicates() {
        if(StringUtils.isBlank(rootPath) || StringUtils.isBlank(directory)) {
            predicates.clear();
            return;
        }
        LOGGER.trace("Generating Free text search predicates");
        predicates.put("group.p.or", "true");
        predicates.put("fulltext", queryString);
        predicates.put("group.1_group.type", "cq:Page");
        predicates.put("group.1_group.path", rootPath);
        if (StringUtils.isNotBlank(secondaryrootPath)) {
            predicates.put("group.3_group.type", "cq:Page");
            predicates.put("group.3_group.path", secondaryrootPath);
        }
                        
        Map<String, List<String>> tagFilterMap = getFilterTagMap();
        
        int counter = 1;
        for (Entry<String, List<String>> entry : tagFilterMap.entrySet()) {
            predicates.put("group.1_group."+counter + SEARCH_PROPERTY, NP_JCR_CONTENT + entry.getKey());
            List<String> values = entry.getValue();
            for (int i = 0; i < values.size(); i++) {
                predicates.put("group.1_group."+counter + SEARCH_PROPERTY + DOT_SEPARATOR + i + 1 + SEARCH_VALUE, values.get(i));
            }
            counter++;

        }
        
        // add all page restrictions
        counter = 1;
        predicates.put("group.2_group.type", "dam:Asset");
        predicates.put("group.2_group.path", directory);
        if (StringUtils.isNotBlank(secondarydirectory)) {
            predicates.put("group.4_group.type", "dam:Asset");
            predicates.put("group.4_group.path", secondarydirectory);
        }
        for (Entry<String, List<String>> entry : tagFilterMap.entrySet()) {
            predicates.put("group.2_group."+counter + SEARCH_PROPERTY, NP_ASSET_METADATA + entry.getKey());
            List<String> values = entry.getValue();
            for (int i = 0; i < values.size(); i++) {
                predicates.put("group.2_group."+counter + SEARCH_PROPERTY + DOT_SEPARATOR + i + 1 + SEARCH_VALUE, values.get(i));
            }
            counter++;

        }
        // add all asset restrictions

        setPaginationPredicates();

    }

    /**
     * Generates search predicates for Card List and DownloadList components
     */
    private void generatePredicates() {
        predicates.put(SEARCH_PATH, searchRoot);
        setSearchContentTypePredicate();
        if (StringUtils.isNotEmpty(queryString))
            predicates.put(SEARCH_FULLTEXT, queryString);
        String searchPropertyPath = getSearchContentType().equals(SearchContentType.ASSET) ? NP_ASSET_METADATA
                : NP_JCR_CONTENT;
        setTagFilterPredicates(getFilterTagMap(), searchPropertyPath);
        setSortPredicates();
        setPaginationPredicates();
    }

    private void setTagFilterPredicates(Map<String, List<String>> tagFilterMap, String searchPropertyPath) {
        int counter = 1;
        for (Entry<String, List<String>> entry : tagFilterMap.entrySet()) {
            predicates.put(counter + SEARCH_PROPERTY, searchPropertyPath + entry.getKey());
            List<String> values = entry.getValue();
            for (int i = 0; i < values.size(); i++) {
                predicates.put(counter + SEARCH_PROPERTY + DOT_SEPARATOR + i + 1 + SEARCH_VALUE, values.get(i));
            }
            counter++;

        }
        // Execute only when assets are being listed
        if (getSearchContentType().value.equalsIgnoreCase(SearchContentType.ASSET.value)) {
            // Predicate to list only downloadable assets
            predicates.put(counter + SEARCH_PROPERTY, searchPropertyPath + MacnicaCoreConstants.PN_IS_DOWNLOADABLE);
            predicates.put(counter + SEARCH_PROPERTY + DOT_SEPARATOR + 1 + SEARCH_VALUE, BooleanUtils.TRUE);
    
        }

    }

    private Map<String, List<String>> getFilterTagMap() {
        if (!StringUtils.isBlank(tagFilters)) {
            String[] appliedTags = tagFilters.split(COMMA_SEPARATOR);
            if (!StringUtils.isAllBlank(appliedTags)) {
                Map<String, List<String>> tagValueMap;
                tagValueMap = Arrays.asList(appliedTags).stream().filter(StringUtils::isNotBlank)
                        .collect(Collectors.groupingBy(MultiLevelFilterImpl::getPropertyName));
                return tagValueMap.entrySet().stream().filter(Objects::nonNull)
                        .filter(entry -> StringUtils.isNotBlank(entry.getKey()) && Objects.nonNull(entry.getValue()))
                        .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
            }
        }
        return Collections.emptyMap();
    }

    private void setPaginationPredicates() {
        predicates.put(SEARCH_OFFSET, "" + offset);
        predicates.put(SEARCH_LIMIT, "" + maxItems);
        
    }

    private void setSearchContentTypePredicate() {

        switch (getSearchContentType()) {
            case ASSET:
                predicates.put(SEARCH_CONTENT_TYPE, SEARCH_CONTENT_TYPE_ASSET);
                break;
            case PAGE:
                predicates.put(SEARCH_CONTENT_TYPE, NameConstants.NT_PAGE);
                break;
            default:
                LOGGER.warn("In valid search content type {}. Not adding any content type predicate",
                        getSearchContentType());

        }
    }

    private void setSortPredicates() {
        String propertyPath = getSearchContentType().equals(SearchContentType.ASSET) ? NP_ASSET_METADATA
                : NP_JCR_CONTENT;
        predicates.put(SEARCH_ORDER_BY, propertyPath + getOrderBy().value);
        if (getOrderBy() != OrderBy.PATH) {
            predicates.put(SEARCH_ORDER_BY_SORT, getSortOrder().value);
        }

    }

    private OrderBy getOrderBy() {
        return java.util.Optional.ofNullable(OrderBy.fromString(orderBy))
                .orElse(OrderBy.PATH);
    }

    private SortOrder getSortOrder() {
        return java.util.Optional.ofNullable(SortOrder.fromString(sortOrder))
                .orElse(SortOrder.DESC);
    }

    private SearchContentType getSearchContentType() {
        return java.util.Optional.ofNullable(SearchContentType.fromString(searchContentType))
                .orElse(SearchContentType.ALL);

    }

}
