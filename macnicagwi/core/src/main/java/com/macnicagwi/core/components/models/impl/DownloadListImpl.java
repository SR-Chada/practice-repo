package com.macnicagwi.core.components.models.impl;

import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_LIMIT;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.SEARCH_OFFSET;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.Session;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.mime.MimeTypeService;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Required;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.day.cq.dam.api.Asset;
import com.day.cq.search.QueryBuilder;
import com.day.cq.wcm.api.Page;
import com.drew.lang.annotations.Nullable;
import com.macnicagwi.core.components.models.DownloadList;
import com.macnicagwi.core.components.models.MacnicaAssetListItem;
import com.macnicagwi.core.models.MacnicaLinkHandler;
import com.macnicagwi.core.models.MacnicaPredicateHandler;
import com.macnicagwi.core.services.JcrSearchService;
import com.macnicagwi.core.services.MacnicaFacetSearchService;

import lombok.Getter;

@Model(adaptables = SlingHttpServletRequest.class, adapters = { DownloadList.class,
		ComponentExporter.class }, resourceType = DownloadListImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)

public class DownloadListImpl extends AbstractComponentImpl implements DownloadList {

	private static final Logger logger = LoggerFactory.getLogger(DownloadListImpl.class);

	protected static final String RESOURCE_TYPE = "macnicagwi/components/content/downloadlist";


	private static final String NN_SELECTED_PROPERITES = "selectedProperties";
	private static final String PN_SELECTED_PROPERITES_NAME = "name";
	private static final String PN_SELECTED_PROPERITES_LABEL = "label";

	@Self
	private MacnicaLinkHandler linkHandler;
	
	@Self
	private MacnicaPredicateHandler predicateHandler;

	@OSGiService
	@Required
	private QueryBuilder queryBuilder;

	@ScriptVariable
	private Page currentPage;

	@ValueMapValue
	@Nullable
	private Boolean showDescription;

	@ValueMapValue
	@Nullable
	private Boolean showTitle;

	@ValueMapValue
	@Nullable
	@Getter
	private String downloadFormXFPath;

	@ValueMapValue
	@Nullable
	private Integer maxItems;

	private List<MacnicaAssetListItem> listItems;

	@Getter
	private Integer totalResultsSize;
	
    @Inject
    private Boolean onlySupplierAssets;

	@OSGiService
	private JcrSearchService searchService;

	@OSGiService
	private MimeTypeService mimeTypeService;

	@OSGiService
	private MacnicaFacetSearchService macnicaFacetSearchService;

	/**
	 * Path to the current card list component resource
	 */
	@Getter
	private String resourcePath;

	@PostConstruct
	private void init() {
			resourcePath = request.getResource().getPath();
			logger.trace("Initializing Download List component. Resource path {} ", resourcePath);
			Map<String, String> predicates = predicateHandler.getPredicates();			
			listItems = generateListItems(predicates);
			if(BooleanUtils.isTrue(onlySupplierAssets)) {
			    listItems = filterSupplierAssets();
			}
			logger.trace("Generated {} download list items", listItems.size());

	}

	/**
	 * Helper method to generate download list items based on rootPath and Facets
	 * 
	 * @param rootPath: Root directory path for generating download list
	 * @param facetMap
	 */
	private List<MacnicaAssetListItem> generateListItems(@Nullable Map<String, String> predicates) {
		logger.trace("Generating download list for predicates {} ", predicates.entrySet());
		if (predicates.isEmpty()) {
			logger.warn("No search predicates supplied to generate download lsit items. Returning empty list");
			return Collections.emptyList();
		}

		Session session = resource.getResourceResolver().adaptTo(Session.class);
		if (searchService == null || session == null) {
			logger.error(
					"Unable to generate download list items as session or searchSerice object is not available. session : {} searchService {}",
					session, searchService);
			return Collections.emptyList();
		}
		List<Asset> results = this.searchService.getAssets(predicates, session);
		predicates.remove(SEARCH_OFFSET);
		predicates.remove(SEARCH_LIMIT);
		this.totalResultsSize = this.searchService.getTotalCount(predicates, session);
		if (results != null && !results.isEmpty()) {
			logger.trace("Retrieved {} results after JCR lookup", results.size());
			return results.stream().filter(Objects::nonNull).map(asset -> new MacnicaAssetListItemImpl(linkHandler,
					mimeTypeService, asset, currentPage.getLanguage(), getId())).collect(Collectors.toList());
		}
		return Collections.emptyList();

	}
	
	private List<MacnicaAssetListItem> filterSupplierAssets() {
	   return StreamSupport.stream(listItems.spliterator(),false).filter(DownloadListImpl::isSupplierAsset).collect(Collectors.toList());
	}
	private static Boolean isSupplierAsset(MacnicaAssetListItem item) {
	    String[] supplierTags = {item.getManufacturer(),item.getProductLine(),item.getBusinessCategory()};
	   return !StringUtils.isAllBlank(supplierTags);
	}

	@Override
	public Map<String, String> getSelectedProperties() {
		Map<String, String> selectedPropertiesMap = new LinkedHashMap<>();
		Resource selectedPropertiesResource = resource.getChild(NN_SELECTED_PROPERITES);
		if (selectedPropertiesResource != null) {
			for (Resource selectedProperties : selectedPropertiesResource.getChildren()) {
				String name = selectedProperties.getValueMap().get(PN_SELECTED_PROPERITES_NAME, String.class);
				String label = selectedProperties.getValueMap().get(PN_SELECTED_PROPERITES_LABEL, String.class);
				selectedPropertiesMap.put(name, label);
			}
			logger.trace("Selected Properties {} ", selectedPropertiesMap);
			return selectedPropertiesMap;
		}

		return Collections.emptyMap();
	}

	@Override
	public Collection<MacnicaAssetListItem> getAssets() {
		return listItems;
	}

	@Override
	public boolean isEmpty() {
		return listItems.isEmpty();
	}

	@Override
	public Boolean showDescription() {
		return showDescription;
	}

	@Override
	public Boolean showTitle() {
		return showTitle;
	}


}
