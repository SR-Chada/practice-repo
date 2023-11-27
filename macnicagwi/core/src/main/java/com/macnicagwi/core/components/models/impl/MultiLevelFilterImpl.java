package com.macnicagwi.core.components.models.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.drew.lang.annotations.Nullable;
import com.macnicagwi.core.components.models.MultiLevelFilter;

@Model(adaptables = SlingHttpServletRequest.class, adapters = { MultiLevelFilter.class,
		ComponentExporter.class }, resourceType = MultiLevelFilterImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class MultiLevelFilterImpl extends AbstractComponentImpl implements MultiLevelFilter {

	public static final Logger LOGGER = LoggerFactory.getLogger(MultiLevelFilterImpl.class);
	public static final String RESOURCE_TYPE = "macnicagwi/components/content/multilevelfilter";

	private static final char TAG_NAMESPACE_SEPARATOR = ':';
	private static final char TAG_PATH_SEPARATOR = '/';

	/**
	 * JCR Properties used for storing tag names against pages or assets
	 */
	private static final String PN_MANUFACTURER = "manufacturer";
	private static final String PN_PRODUCT_LINE = "productLines";
	private static final String PN_PRODUCT_FAMILY = "productFamily";
	private static final String PN_APPLICATION_CATEGORY = "applicationCategories";
	private static final String PN_BUSINESS_CATEGORY = "businessCategories";
	private static final String PN_PAGE_TYPE = "pageType";
	private static final String PN_FEATURES = "features";
	private static final String PN_EVNENT_STATUS = "status";
	private static final String PN_YEAR = "year";
	private static final String PN_TYPE = "type";
	private static final String PN_OPTIC_TYPE = "optictype";
	private static final String PN_AR_COATING = "arcoating";
	private static final String PN_SENSOR_TYPE = "sensortype";
	private static final String PN_PIXEL_SIZE = "pixelsize";
	private static final String PN_RESOLUTION = "resolution";
	private static final String PN_OPTICAL_FORMAT = "opticalformat";
	private static final String PN_INTERFACE = "interface";
	private static final String PN_SENSOR_CATEGORY = "sensorcategory";
	private static final String PN_SHUTTER_TYPE = "shuttertype";
	

	/**
	 * Tag Names
	 */
	private static final String TN_MANUFACTURER = "manufacturers";
	private static final String TN_PRODUCT_LINE = "product-lines";
	private static final String TN_PRODUCT_FAMILY = "product-families";
	private static final String TN_APPLICATION_CATEGORY = "application-categories";
	private static final String TN_BUSINESS_CATEGORY = "business-categories";
	private static final String TN_PAGE_TYPE = "page-type";
	private static final String TN_FEATURES = "features";
	private static final String TN_EVNENT_STATUS = "event-status";
	private static final String TN_YEAR = "years";
	private static final String TN_TYPE = "type";
	private static final String TN_OPTIC_TYPE = "optic-type";
	private static final String TN_AR_COATING = "ar-coating";
	private static final String TN_SENSOR_TYPE = "sensor-type";
	private static final String TN_PIXEL_SIZE = "Pixel-size";
	private static final String TN_RESOLUTION = "resolution";
	private static final String TN_OPTICAL_FORMAT = "optical-format";
	private static final String TN_INTERFACE = "interface";
	private static final String TN_SENSOR_CATEGORY = "sensor-category";
	private static final String TN_SHUTTER_TYPE = "shutter-type";

	private static final Map<String, String> PROPERTY_NAME_TAG_MAP = new HashMap<>();

	/**
	 * Static initializer for PROPERTY_NAME_TAG_MAP
	 */
	static {
		PROPERTY_NAME_TAG_MAP.put(TN_MANUFACTURER,PN_MANUFACTURER );
		PROPERTY_NAME_TAG_MAP.put(TN_PRODUCT_LINE, PN_PRODUCT_LINE);
		PROPERTY_NAME_TAG_MAP.put(TN_PRODUCT_FAMILY, PN_PRODUCT_FAMILY);
		PROPERTY_NAME_TAG_MAP.put(TN_APPLICATION_CATEGORY, PN_APPLICATION_CATEGORY);
		PROPERTY_NAME_TAG_MAP.put(TN_BUSINESS_CATEGORY, PN_BUSINESS_CATEGORY);
		PROPERTY_NAME_TAG_MAP.put(TN_PAGE_TYPE, PN_PAGE_TYPE);
		PROPERTY_NAME_TAG_MAP.put(TN_FEATURES, PN_FEATURES);
		PROPERTY_NAME_TAG_MAP.put(TN_EVNENT_STATUS, PN_EVNENT_STATUS);
		PROPERTY_NAME_TAG_MAP.put(TN_YEAR, PN_YEAR);
		PROPERTY_NAME_TAG_MAP.put(PN_TYPE, TN_TYPE);
		PROPERTY_NAME_TAG_MAP.put(TN_OPTIC_TYPE, PN_OPTIC_TYPE);
		PROPERTY_NAME_TAG_MAP.put(TN_OPTIC_TYPE, PN_OPTIC_TYPE);
		PROPERTY_NAME_TAG_MAP.put(TN_AR_COATING, PN_AR_COATING);
		PROPERTY_NAME_TAG_MAP.put(TN_SENSOR_TYPE, PN_SENSOR_TYPE);
		PROPERTY_NAME_TAG_MAP.put(TN_PIXEL_SIZE, PN_PIXEL_SIZE);
		PROPERTY_NAME_TAG_MAP.put(TN_RESOLUTION, PN_RESOLUTION);
		PROPERTY_NAME_TAG_MAP.put(TN_OPTICAL_FORMAT, PN_OPTICAL_FORMAT);
		PROPERTY_NAME_TAG_MAP.put(TN_INTERFACE, PN_INTERFACE);
		PROPERTY_NAME_TAG_MAP.put(TN_SENSOR_CATEGORY, PN_SENSOR_CATEGORY);
		PROPERTY_NAME_TAG_MAP.put(TN_SHUTTER_TYPE, PN_SHUTTER_TYPE);
		
	}

	@Inject
	private String[] tags;

	/**
	 * The current page.
	 */
	@ScriptVariable
	private Page currentPage;
	
	@ValueMapValue
	@Nullable
	private Boolean isfreeTextSearchEnabled;
	
	@ValueMapValue
	@Nullable
	private String filterButtonText; 
	
	@ValueMapValue
	@Nullable
	private String applyButtonText; 
	
	@ValueMapValue
	@Nullable
	private String clearButtonText; 
	
	@ValueMapValue
	@Nullable
	private String noResultsFoundText; 
	
	@ValueMapValue
	@Nullable
	private String loadMoreButtonText; 
	
	@ValueMapValue
	@Nullable
	private String allButtonText;
	
	@ValueMapValue
	@Nullable
	private String freeTextSearchPlaceholder;

	private Map<MacnicaFilterItem, List<MacnicaFilterItem>> filterItemMap;

	/**
	 * Two letter Language code of the page in which the component is added
	 */
	private Locale language;

	@PostConstruct
	private void init() {
		LOGGER.trace("Initializing multilevel filter at for {}. tags {}", resource.getPath(), tags);
		language = currentPage.getLanguage();
		TagManager tagManager = resource.getResourceResolver().adaptTo(TagManager.class);
		if (tags == null) {
			LOGGER.warn("No tags passed to multi level filter. Resource {}", resource.getPath());
			return;
		}
		filterItemMap = Arrays.asList(tags).stream().map(tagManager::resolve)
				.collect(Collectors.toMap(MacnicaFilterItemImpl::new, this::getChildTagFilteritems));
	}

	@Override
	public Map<MacnicaFilterItem, List<MacnicaFilterItem>> getFilterItemMap() {
		return filterItemMap;
	}

	@Override
	public boolean isEmpty() {
		return StringUtils.isAllBlank(tags);
	}

	/**
	 * Helper method to get JCR Property name which is used to store a tag value
	 * 
	 * @param tag : String value of the tag that needs to be looked up (String Format:
	 *            {@code namespace:root-path/child-path})
	 * @return propertyName : JCR property name against which the corresponding tag
	 *         value is stored.
	 */
	public static String getPropertyName(String tag) {
		if (StringUtils.isNotEmpty(tag)) {
			try {
				tag = tag.substring(tag.indexOf(TAG_NAMESPACE_SEPARATOR)+1, tag.lastIndexOf(TAG_PATH_SEPARATOR));
				String propertyName = PROPERTY_NAME_TAG_MAP.get(tag);
				LOGGER.trace("Returning property name {} for tag {}", propertyName, tag);
				if (StringUtils.isNotEmpty(propertyName))
					return propertyName;

			} catch (IndexOutOfBoundsException e) {
				LOGGER.error("Unable to retrieve property name for tag {}. Exception {}", tag, e);
			}
		}
		return StringUtils.EMPTY;
	}

	private List<MacnicaFilterItem> getChildTagFilteritems(@Nullable Tag tag) {
		if (tag == null) {
			return Collections.emptyList();
		}
		Iterable<Tag> childTagIterator = tag::listChildren;
		List<MacnicaFilterItem> filterItems = StreamSupport.stream(childTagIterator.spliterator(), false)
            .map(MacnicaFilterItemImpl::new)
            .collect(Collectors.toList());

    		// Sort the list
    		filterItems.sort(Comparator.comparing(MacnicaFilterItem::getTitle));

   		 return filterItems;
	}

	public class MacnicaFilterItemImpl implements MacnicaFilterItem {

		private String title;
		private String id;
		private String path;
		private Tag tag;

		public MacnicaFilterItemImpl(Tag tag) {
			if (tag == null) {
				LOGGER.warn(
						"Tag reference passed to the MacnicaFilterItemImpl is null. Hence initialising empty object");
			} else {
				this.tag = tag;
				this.title = tag.getTitle(language);
				this.id = tag.getTagID();
				this.path = tag.getPath();

			}

		}

		@Override
		public String getTitle() {
			return title;
		}

		@Override
		public String getId() {
			return id;
		}

		@Override
		public String getPath() {
			return path;
		}

		@Override
		public Tag getTag() {
			return tag;
		}
		
		

	}

	@Override
	public boolean isfreeTextSearchEnabled() {
		return isfreeTextSearchEnabled;
	}

	@Override
	public String getFilterButtonText() {
		return filterButtonText ;
	}

	@Override
	public String getApplyButtonText() {
		return applyButtonText ;
	}

	@Override
	public String getClearButtonText() {
		return clearButtonText ;
	}

	@Override
	public String getNoResultsFoundText() {
		return noResultsFoundText ;
	}

	@Override
	public String getLoadMoreButtonText() {
		return loadMoreButtonText ;
	}
	
	@Override
	public String getAllButtonText() {
		return allButtonText;
		
	}
	
	@Override
	public String getFreeTextSearchPlaceholder() {
		return freeTextSearchPlaceholder;
	}

}
