package com.macnicagwi.core.constants;

import java.util.HashMap;
import java.util.Map;

import com.day.cq.dam.api.DamConstants;

/**
 * This class provides global constants.
 * <p>
 * When relevant, constant names are always prefixed with a type qualifier (NT for a node type, PN for a property, RT
 * for a resource type...)
 *
 * @author Sumit Agarwal
 */
public final class MacnicaCoreConstants {

    private MacnicaCoreConstants() {
        // Disallow instantiation
    }

    /* String constants for OSGI */
    public static final String MACNICA_SUB_SERVICE = "macnicagwi-wcm-system";
    
    /* Search constants */
    public static final String SEARCH_PATH = "path";
    public static final String SEARCH_CONTENT_TYPE = "type";
    public static final String SEARCH_CONTENT_TYPE_ASSET = "dam:Asset";
    
    public static final String SEARCH_FULLTEXT = "fulltext";
    public static final String SEARCH_NODENAME = "nodename";
    public static final String SEARCH_PROPERTY = "%s_property";
    public static final String SEARCH_PROPERTY_VALUE = "%s_property.%s_value";
    public static final String SEARCH_LIMIT = "p.limit";
    public static final String SEARCH_OFFSET = "p.offset";
    public static final String SEARCH_ALL_RESULTS = "-1";
    public static final String SEARCH_ORDER_BY = "orderby";
    public static final String SEARCH_ORDER_BY_SORT = "orderby.sort";
    public static final String SEARCH_ORDER_SORT_ASC = "asc";
    public static final String SEARCH_ORDER_SORT_DESC = "desc";
    public static final String SEARCH_ORDER_BY_PATH = "path";
    public static final String SEARCH_ORDER_BY_TITLE = "jcr:title";
    public static final String SEARCH_ORDER_BY_ASSET_TITLE = "dc:title";
    public static final String SEARCH_PROPERTIES_AND_OPERATION = "property.and";
    public static final String SEARCH_PROPERTIES_OR_OPERATION = "property.or";
    
    /*
     * Page Types
     */
    public static final String PAGE_TYPE_MANUFACTURER_LANDING="productManufacturerLandingPage";
    public static final String PAGE_TYPE_PRODUCT_LINE_DETAIL="productLineLandingPage";
    
    /*
     * Component Resource Types
     */    
    public static final String IMAGE_COMPONENT_RESOURCE_TYPE="macnicagwi/components/content/image";
    

    /* Utility constants */
    public static final String SPACE_SEPARATOR = " ";
    public static final String COMMA_SEPARATOR = ",";
    public static final String DOT_SEPARATOR = ".";
    public static final String PATH_SEPARATOR = "/";
    public static final String EQUAL_SEPARATOR = "=";
    public static final String DASH_SEPARATOR = "-";
    public static final String AT_SYMBOL = "@";
    public static final String LINE_SEPARATOR = "\n";
    public static final String PIPE_SEPARATOR = "|";
    
    public static final String CORE_DOWNLOAD_SERVLET_SELECTOR = "coredownload";

    /* Macnica Specific constants */
    public static final String PRODUCT_LINE_LOGOS_PARENT_FOLDER_PATH = "/content/dam/macnicagwi/%s/%s/public/%s/logos/product-lines";

    /* Macnica Asset Metadat Specific constants */
    public static final String ASSET_METADATA_PROPERTY = "jcr:content/metadata/";
    public static final String ASSET_METADATA_DAM_SHA_PROPERTY_PATH = ASSET_METADATA_PROPERTY + DamConstants.PN_SHA1;
    public static final String ASSET_METADATA_BUSINESS_CATEGORIES_PROPERTY_PATH = ASSET_METADATA_PROPERTY + "businessCategories";
    public static final String ASSET_METADATA_MANUFACTURER_PROPERTY_PATH = ASSET_METADATA_PROPERTY + "manufacturer";
    public static final String ASSET_METADATA_PRODUCT_LINE_PROPERTY_PATH = ASSET_METADATA_PROPERTY + "productLine";
    public static final String ASSET_METADATA_CHINESE_YEAR_PROPERTY_PATH = ASSET_METADATA_PROPERTY + "chineseYear";
    public static final String ASSET_METADATA_SEARCHABLE_PROPERTY_PATH = ASSET_METADATA_PROPERTY + "isSearchable";
    public static final String ASSET_METADATA_DOWNLOADABLE_PROPERTY_PATH = ASSET_METADATA_PROPERTY + "isDownloadable";
    public static final String ASSET_METADATA_RESTRICTED_DOWNLOAD_PROPERTY_PATH = ASSET_METADATA_PROPERTY + "isRestrictedDownload";
    public static final String ASSET_METADATA_CONTENT_TYPE_PROPERTY_PATH = ASSET_METADATA_PROPERTY + "contentType";
    public static final String ASSET_METADATA_YEAR_PROPERTY_PATH = ASSET_METADATA_PROPERTY + "year";
    public static final String ASSET_METADATA_REPORT_TYPE_PROPERTY_PATH = ASSET_METADATA_PROPERTY + "reportType";
    public static final String ASSET_METADATA_QUARTER_PROPERTY_PATH = ASSET_METADATA_PROPERTY + "quarter";
    
    /*
     * Node Names
     */
    public static final String NN_PRODUCT_lINE_LOGO="productlinelogo";
    public static final String NN_MANUFACTURER_LOGO="manufacturerlogo";
    public static final String NN_AUTHOR_IMAGE="authorImage";
    
    
    /**
     * Property Name for article date
     */
    public static final String PN_ARTICLE_DATE = "date";
    /**
     * Property Name for Event Start date
     */
    public static final String PN_EVENT_START_DATE = "startDate";
    /**
     * Property Name for Event end date
     */
    public static final String PN_EVENT_END_DATE = "endDate";
    /**
     * Property Name for Event registration Start date
     */
    public static final String PN_EVENT_REGISTRATION_START_DATE = "startDateOfReg";
    /**
     * Property Name for Event registration end date
     */
    public static final String PN_EVENT_REGISTRATION_END_DATE = "endDateOfReg";
    /**
     * Property Name for Event location
     */
    public static final String PN_EVENT_LOCATION = "location";
    /**
     * Property Name for Event status
     */
    public static final String PN_EVENT_STATUS= "status";
    /**
     * Property Name for Manufacturer ranking
     */
    public static final String PN_PRODUCT_MANUFACTURER_RANKING= "productManufacturerRanking";
    
    /**
     * Attribute specifying the link target
     */
    public static final String PN_LINK_TARGET="linkTarget";
    
    
	/**
	 * JCR Properties used for storing tag names against pages or assets
	 */
	public static final String PN_MANUFACTURER = "manufacturer";
	public static final String PN_PRODUCT_LINE = "productLines";
	public static final String PN_PRODUCT_FAMILY = "productFamily";
	public static final String PN_APPLICATION_CATEGORY = "applicationCategories";
	public static final String PN_BUSINESS_CATEGORY = "businessCategories";
	public static final String PN_PAGE_TYPE = "pageType";
	public static final String PN_FEATURES = "features";
	public static final String PN_EVNENT_STATUS = "status";
	public static final String PN_YEAR = "year";
	public static final String PN_CHINESE_YEAR = "chineseYear";
	public static final String PN_DOWNLOAD_DETAILS_LINK = "downloadDetailsLink";
	public static final String PN_QUARTER = "quarter";
	public static final String PN_REPORT_TYPE = "reportType";
	public static final String PN_CONTENT_TYPE= "contentType";
	public static final String PN_IS_DOWNLOADABLE= "isDownloadable";
	public static final String PN_IS_RESTRICTED_DOWNLOAD= "isRestrictedDownload";
	public static final String PN_DOWNLOAD_DETAIL_PAGE_PATH= "downloadDetailPagePath";
	public static final String PN_PUBLISHED_DATE ="publishedDate";
    public static final String PN_TYPE="type";
    

	/**
	 * Tag Names
	 */
	public static final String TN_MANUFACTURER = "manufacturers";
	public static final String TN_PRODUCT_LINE = "product-lines";
	public static final String TN_PRODUCT_FAMILY = "product-families";
	public static final String TN_APPLICATION_CATEGORY = "application-categories";
	public static final String TN_BUSINESS_CATEGORY = "business-categories";
	public static final String TN_PAGE_TYPE = "page-type";
	public static final String TN_FEATURES = "features";
	public static final String TN_EVNENT_STATUS = "event-status";
	public static final String TN_YEAR = "years";
	public static final String TN_QUARTER = "quarter";
	public static final String TN_REPORT_TYPE = "report-type";
	public static final String TN_CONTENT_TYPE= "content-type";
    



    /* Search Filters Specific constants */
    public static final Map<String, String> PAGE_SPECIFIC_SEARCH_FACET_KEYS = new HashMap<String, String>()
    {
        {
            put("business-categories", "jcr:content/businessCategories");
            put("manufacturers", "jcr:content/manufacturer");
            put("product-lines", "jcr:content/productLines");
            put("product-families", "jcr:content/productFamily");
            put("application-categories", "jcr:content/applicationCategories");
            put("features", "jcr:content/features");
            put("location-type", "jcr:content/locationType");
            put("status", "jcr:content/status");
            put("article-source", "jcr:content/articleSource");
        };
    };

    public static final Map<String, String> ASSET_SPECIFIC_SEARCH_FACET_KEYS = new HashMap<String, String>()
    {
        {
            put("business-categories", "jcr:content/metadata/businessCategories");
            put("manufacturers", "jcr:content/metadata/manufacturer");
            put("product-lines", ASSET_METADATA_PRODUCT_LINE_PROPERTY_PATH);
        };
    };
}
