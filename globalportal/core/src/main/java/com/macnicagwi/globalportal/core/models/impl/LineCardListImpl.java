package com.macnicagwi.globalportal.core.models.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.jcr.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.contentfragment.ContentFragmentList;
import com.adobe.cq.wcm.core.components.models.contentfragment.DAMContentFragment;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;
import com.day.cq.tagging.TagManager;
import com.drew.lang.annotations.Nullable;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.macnicagwi.globalportal.core.models.LineCardItem;
import com.macnicagwi.globalportal.core.models.LineCardList;

@Model(adaptables = { SlingHttpServletRequest.class, Resource.class }, adapters = { LineCardList.class,
		ComponentExporter.class }, resourceType = LineCardListImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class LineCardListImpl implements LineCardList {

	// points to the the component resource path in ui.apps
	protected static final String RESOURCE_TYPE = "globalportal/components/linecardlist";
	private static final Logger LOG = LoggerFactory.getLogger(LineCardListImpl.class);

	@ValueMapValue(name = ContentFragmentList.PN_MODEL_PATH, injectionStrategy = InjectionStrategy.OPTIONAL)
	@Nullable
	private String modelPath;

	@Self
	@Via(type = ResourceSuperType.class)
	private ContentFragmentList lineCardList;

	List<LineCardItem> lineCardItemList = new ArrayList<>();

	Set<String> keyList = new LinkedHashSet<>();

	Map<String, List<LineCardItem>> lineCardItemMap = new HashMap<>();

	@SlingObject
	private ResourceResolver resourceResolver;

	@ValueMapValue(name = ContentFragmentList.PN_PARENT_PATH, injectionStrategy = InjectionStrategy.OPTIONAL)
	@Nullable
	private String parentPath;

	@PostConstruct
	public void init() {

		if (StringUtils.isEmpty(parentPath)) {
			parentPath = DEFAULT_GLOBAL_PORTAL_CF_PATH;
		}

		if (StringUtils.isEmpty(modelPath)) {
			LOG.warn("Please provide a model path for LineCards Component");
			return;
		}

		Session session = resourceResolver.adaptTo(Session.class);
		TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
		if (session == null) {
			LOG.warn("Session was null therefore no query was executed");
			return;
		}
		if (tagManager == null) {
			LOG.error("TagManager object is null. Hence not generating linecards");
			return;
		}

		QueryBuilder queryBuilder = resourceResolver.adaptTo(QueryBuilder.class);
		if (queryBuilder == null) {
			LOG.warn("Query builder was null therefore no query was executed");
			return;
		}

		// unable to generate results when type = dam:Asset is added. Need to be
		// analysed further.
		Map<String, String> queryParameterMap = new HashMap<>();
		queryParameterMap.put("path", parentPath);
		queryParameterMap.put("limit", "-1");
		queryParameterMap.put("1_property", JcrConstants.JCR_CONTENT + "/data/cq:model");
		queryParameterMap.put("1_property.value", modelPath);

		PredicateGroup predicateGroup = PredicateGroup.create(queryParameterMap);
		Query query = queryBuilder.createQuery(predicateGroup, session);
		query.setHitsPerPage(0);
		SearchResult searchResult = query.getResult();

		LOG.info("LineCards query statement{}", searchResult.getQueryStatement());

		// Query builder has a leaking resource resolver, so the following work around
		// is required.
		ResourceResolver leakingResourceResolver = null;

		try {
			// Iterate over the hits if you need special information
			Iterator<Resource> resourceIterator = searchResult.getResources();
			while (resourceIterator.hasNext()) {
				Resource resource = resourceIterator.next();
				if (leakingResourceResolver == null) {
					// Get a reference to QB's leaking resource resolver
					leakingResourceResolver = resource.getResourceResolver();
				}
				LineCardItem item = new LineCardItem(resource, tagManager);
				lineCardItemList.add(item);
				generateKeys();
			}
		} catch (Exception e) {
			LOG.error("Unable to create linecard item from Content fragment", e);
		} finally {
			if (leakingResourceResolver != null) {
				// Always close the leaking query builder resource resolver
				leakingResourceResolver.close();
			}
		}

	}
	
    /**
     * Groups milestone items with respective decade.
     */
    private void generateKeys() {
    	lineCardItemMap = lineCardItemList.stream()
				.collect(Collectors.groupingBy((item) -> item.getCategory()));
        
    }

	@Override
	public Collection<DAMContentFragment> getListItems() {
		return lineCardList.getListItems();
	}
	
	@Override
	@JsonProperty(JSON_PN_ITEMS)
	public List<LineCardItem> getItems(){
		return lineCardItemList;
	}
	
	@Override
	@JsonProperty(JSON_PN_ITEMS_MAP)
	public Map<String, List<LineCardItem>> getItemsMap(){
		return lineCardItemMap;
	}

	@Override
	public String getAppliedCssClasses() {
		return lineCardList.getAppliedCssClasses();
	}

	@Override
	public String getId() {
		return lineCardList.getId();
	}

	@Override
	public String getExportedType() {
		return RESOURCE_TYPE;
	}

}
