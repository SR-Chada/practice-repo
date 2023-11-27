package com.macnicagwi.core.utils;

import static com.macnicagwi.core.constants.MacnicaCoreConstants.ASSET_SPECIFIC_SEARCH_FACET_KEYS;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.AT_SYMBOL;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.NN_PRODUCT_lINE_LOGO;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.PAGE_SPECIFIC_SEARCH_FACET_KEYS;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.PIPE_SEPARATOR;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyType;
import javax.jcr.Value;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.dam.api.Asset;
import com.day.cq.wcm.api.LanguageManager;
import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.google.common.base.Splitter;
import com.macnicagwi.core.components.dtos.SelectedSearchFacetDto;
import com.macnicagwi.core.config.DateFormatConfig;

/**
 * {@code Component Utils} Component Utils
 * Collections of utility methods for manipulating components & its properties.
 * @author Sumit Agarwal
 */
public final class MacnicaComponentUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MacnicaComponentUtils.class);

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private MacnicaComponentUtils() {
        // NOOP
    }

    /**
     * Returns the Page property value.
     *
     * @param page the page
     * @param propertyPath the property Path in format propertyNodePath@propertyName
     * @return the property value
     */
    public static String getPagePropertyValue(@NotNull Page page, String propertyPath) {
        try {
            if (StringUtils.isNotBlank(propertyPath) && propertyPath.contains(AT_SYMBOL)) {
                List<String> propertyDetails = Splitter.on(AT_SYMBOL).trimResults().omitEmptyStrings().splitToList(propertyPath);
                String propertyNodePath = propertyDetails.get(0);
                String propertyName = propertyDetails.get(1);
                
                Resource propertyResource;
                if (StringUtils.isBlank(propertyNodePath)) {
                    propertyResource = page.adaptTo(Resource.class);
                } else if (propertyNodePath.equalsIgnoreCase(NameConstants.NN_CONTENT)) {
                    propertyResource = page.getContentResource();
                } else {
                    propertyResource = page.getContentResource(propertyNodePath);
                }
                if (propertyResource != null) {
                    Node propertyNode = propertyResource.adaptTo(Node.class);
                    if (propertyNode != null && propertyNode.hasProperty(propertyName)) {
                        Property property = propertyNode.getProperty(propertyName);
                        
                        if (!property.isMultiple()) {
                            if (property.getType() == PropertyType.DATE) {
                                return getMacnicaDateFormat(propertyResource, property.getDate());     
                            }
                            return property.getString();
                        } else {
                            String propertyValue ="";
                            Value[] propValues = property.getValues();                        	
                            for (Value propValue : propValues) {                        	
                                propertyValue = propertyValue.concat(propValue.getString()).concat(PIPE_SEPARATOR);
                            }
                            return propertyValue;     
                        }      
                    }
                }
            }
        } catch (Exception e) {
           LOGGER.error("Error in fetching property {} at page {}", propertyPath, page);
        }
        return StringUtils.EMPTY;
    }
    
    /**
     * Returns the asset property value.
     *
     * @param asset the asset
     * @param propertyPath the property Path in format propertyNodePath@propertyName
     * @return the property value
     */
    public static String getAssetPropertyValue(@NotNull Asset asset, String propertyPath) {
        try {
            if (StringUtils.isNotBlank(propertyPath) && propertyPath.contains(AT_SYMBOL)) {
                List<String> propertyDetails = Splitter.on(AT_SYMBOL).trimResults().splitToList(propertyPath);
                String propertyNodePath = propertyDetails.get(0);
                String propertyName = propertyDetails.get(1);
                
                Resource propertyResource;
                if (StringUtils.isNotBlank(propertyNodePath)) {
                    propertyResource = asset.adaptTo(Resource.class).getChild(propertyNodePath);
                } else {
                    propertyResource = asset.adaptTo(Resource.class);
                }
                
                if (propertyResource != null) {
                    Node propertyNode = propertyResource.adaptTo(Node.class);
                    
                    if (propertyNode != null && propertyNode.hasProperty(propertyName)) {
                        Property property = propertyNode.getProperty(propertyName);
                        if (!property.isMultiple()) {
                            if (property.getType() == PropertyType.DATE) {
                                return getMacnicaDateFormat(propertyResource, property.getDate());     
                            }
                            return property.getString();
                        } else {
                            String propertyValue = "";
                            Value[] propValues = property.getValues();                        	
                            for (Value propValue : propValues) {                        	
                                propertyValue = propertyValue.concat(propValue.getString()).concat(PIPE_SEPARATOR);
                            }
                            return propertyValue;     
                        }     
                    }
                }
            }
        } catch (Exception e) {
           LOGGER.error("Error in fetching property {} at asset {}", propertyPath, asset);
        }
        return StringUtils.EMPTY;
    }
    
	/**
	 * Get the list items if using children source.
	 *
	 * @return The child list items if the parent existis. Null otherwise.
	 */
	public static Iterator<Page> getChildListItems(Optional<Page> parentPage) {		
		if(parentPage.isPresent()) {
			return  parentPage.get().listChildren();			
		}
		return null;
    }
    
    /**
	 * Get the list of page properties specific selected search facets.
	 *
	 * @return The list of page properties specific selected search facets.
	 */
	public static List<SelectedSearchFacetDto> getPageSpecificSelectedSearchFacets(List<SelectedSearchFacetDto> selectedSearchFacets) {		
		List<SelectedSearchFacetDto> pageSpecificSelectedSearchFacets = new ArrayList<>();
        for (SelectedSearchFacetDto selectedSearchFacet: selectedSearchFacets) {
            if (PAGE_SPECIFIC_SEARCH_FACET_KEYS.containsKey(selectedSearchFacet.getFacetKey())) {
                SelectedSearchFacetDto pageSpecificSelectedSearchFacet = new SelectedSearchFacetDto(PAGE_SPECIFIC_SEARCH_FACET_KEYS.get(selectedSearchFacet.getFacetKey()), selectedSearchFacet.getFacetValues());
                pageSpecificSelectedSearchFacets.add(pageSpecificSelectedSearchFacet);
            } else {
                pageSpecificSelectedSearchFacets.add(selectedSearchFacet);
            }
        }
		return pageSpecificSelectedSearchFacets;
    }

    /**
	 * Get the list of asset properties specific selected search facets.
	 *
	 * @return The list of asset properties specific selected search facets.
	 */
	public static List<SelectedSearchFacetDto> getAssetSpecificSelectedSearchFacets(List<SelectedSearchFacetDto> selectedSearchFacets) {		
		List<SelectedSearchFacetDto> assetSpecificSelectedSearchFacets = new ArrayList<>();
        for (SelectedSearchFacetDto selectedSearchFacet: selectedSearchFacets) {
            if (ASSET_SPECIFIC_SEARCH_FACET_KEYS.containsKey(selectedSearchFacet.getFacetKey())) {
                SelectedSearchFacetDto assetSpecificSelectedSearchFacet = new SelectedSearchFacetDto(ASSET_SPECIFIC_SEARCH_FACET_KEYS.get(selectedSearchFacet.getFacetKey()), selectedSearchFacet.getFacetValues());
                assetSpecificSelectedSearchFacets.add(assetSpecificSelectedSearchFacet);
            } else {
                assetSpecificSelectedSearchFacets.add(selectedSearchFacet);
            }
        }
		return assetSpecificSelectedSearchFacets;
    }
	
	public static String getRegionName(@NotNull LanguageManager languageManager, @NotNull Resource resource) {
		try {
	    Page page = languageManager.getLanguageRoot(resource);
		return page.getParent().getParent().getName();
		} catch(Exception e) {
		    LOGGER.error("Unable to fetch region name for resource {}", resource.getPath(),e);
		    return StringUtils.EMPTY;
		}
	}
	
	public static String getSubsidiaryName(@NotNull LanguageManager languageManager, @NotNull Resource resource) {
	    try {
		Page page = languageManager.getLanguageRoot(resource);
		return page.getParent().getName();
	    } catch(Exception e) {
            LOGGER.error("Unable to fetch SubsidiaryName  for resource {}", resource.getPath(),e);
            return StringUtils.EMPTY;
        }
	}
	
	public static String getLanguageRootName(@NotNull LanguageManager languageManager, @NotNull Resource resource) {
		Page page = languageManager.getLanguageRoot(resource);
		return page.getName();
	}

    
    private static String getMacnicaDateFormat(Resource resource, Calendar dateVal) {
        DateFormatConfig dateFormatConfig = resource.adaptTo(ConfigurationBuilder.class).as(DateFormatConfig.class);
        Date date =  dateVal.getTime();
        TimeZone timeZone = dateVal.getTimeZone();       
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatConfig.dateFormat());
        dateFormat.setTimeZone(timeZone);
        return dateFormat.format(date);
    }
    
    /**
     * Returns the resource holding the properties of the product line logo of the page
     *
     * @param page the page
     * @return the product line logo image resource
     */
    @Nullable
    public static Resource getProductLineLogo(@NotNull Page page) {
        return page.getContentResource(NN_PRODUCT_lINE_LOGO);
    }
}
