package com.macnicagwi.core.services.impl;

import static com.macnicagwi.core.constants.MacnicaCoreConstants.AT_SYMBOL;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_ALL_RESULTS;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_CONTENT_TYPE;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_LIMIT;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_PATH;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_PROPERTY;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_PROPERTY_VALUE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Session;

import org.apache.sling.api.resource.Resource;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.dam.cfm.ContentFragment;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.DamConstants;
import com.macnicagwi.core.services.DamAssetService;
import com.macnicagwi.core.services.JcrSearchService; 

/**
 * DAM Asset Service Implementation
 * <p>
 * This service allows users to perform DAM Assets related implemnetations.
 * @author Sumit Kumar Agarwal
 */
@Component(service = DamAssetService.class, immediate = true)
@ServiceDescription("DAM Asset Service")
public class DamAssetServiceImpl implements DamAssetService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DamAssetServiceImpl.class);

    @Reference
    private JcrSearchService jcrSearchService;

    @Activate
    protected void activate() {
        LOGGER.trace("DAM Asset Service Activated!");
    }

    @Override
    public List<ContentFragment> getFilteredContentFragments(List<String> filters, String filterProperty, String cfmParentFolderPath, Session session) {
        List<Resource> filteredContentFragmentResources = getResourcesByMetadata(filters, filterProperty, cfmParentFolderPath, session);
		return convertResources(filteredContentFragmentResources, ContentFragment.class);
    }

    @Override
    public List<Asset> getProductLineLogos(List<String> productLines, String assetProductLineProperty, String assetParentFolderPath, Session session) {
        List<Resource> productLineLogoResources = getResourcesByMetadata(productLines, assetProductLineProperty, assetParentFolderPath, session);
        return convertResources(productLineLogoResources, Asset.class);
    }

    @Override
    public List<Asset> getAssetByDamSha(String assetDamSha, String damShaProperty, String assetParentFolderPath,
            Session session) {
        List<Resource> assetList = getResourcesByMetadata(Collections.singletonList(assetDamSha), damShaProperty, assetParentFolderPath, session);
        return convertResources(assetList, Asset.class);
    }


    private List<Resource> getResourcesByMetadata(List<String> propertyValues, String propertyName, String assetParentFolderPath, Session session) {
        final Map<String, String> searchQuerypredicates = new LinkedHashMap<>();
        searchQuerypredicates.put(SEARCH_PATH, assetParentFolderPath);
        searchQuerypredicates.put(SEARCH_CONTENT_TYPE, DamConstants.NT_DAM_ASSET);
        
		searchQuerypredicates.put(String.format(SEARCH_PROPERTY, "1"), AT_SYMBOL + propertyName);
		for (String propertyValue: propertyValues) {
			searchQuerypredicates.put(String.format(SEARCH_PROPERTY_VALUE, 1, propertyValues.indexOf(propertyValue) + 1), propertyValue);
		}
            
		searchQuerypredicates.put(SEARCH_LIMIT, SEARCH_ALL_RESULTS);
        return jcrSearchService.getResources(searchQuerypredicates, session);
    }

    private static <T> List<T> convertResources(List<Resource> resources, Class<T> classType) {
        List<T> results = new ArrayList<>();
        resources.forEach(resource -> {
            T element = resource.adaptTo(classType);
            if (element != null) {
                results.add(element);
            }
        });
        return results;
    }

}