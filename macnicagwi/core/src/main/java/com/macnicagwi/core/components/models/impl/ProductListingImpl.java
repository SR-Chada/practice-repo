package com.macnicagwi.core.components.models.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.jcr.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.List;
import com.adobe.cq.wcm.core.components.models.ListItem;
import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.designer.Style;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.macnicagwi.core.components.models.ProductListing;
import com.macnicagwi.core.models.MacnicaLinkHandler;
import com.macnicagwi.core.models.MacnicaPredicateHandler;
import com.macnicagwi.core.services.JcrSearchService;

import lombok.Getter;

/**
 * ProductListing model implementation.
 */
@Model(adaptables = SlingHttpServletRequest.class, adapters = { ProductListing.class,
		ComponentExporter.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL, resourceType = ProductListingImpl.RESOURCE_TYPE)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class ProductListingImpl extends AbstractComponentImpl implements ProductListing {

	/**
	 * The resource type.
	 */
	protected static final String RESOURCE_TYPE = "macnicagwi/components/content/productlisting";

	/**
	 * The logger.
	 */

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static final String PRODUCT_TEMPLATE_NAME = "product-detail-page-content";
	
	private static final String MAE_PRODUCT_TEMPLATE_NAME = "macnica-gwi---mae-product-page";

	private static final String PN_CQ_TEMPLATE = "cq:template";

	
	
	
    /**
     * Component properties.
     */
    @ScriptVariable
    protected ValueMap properties;
    
    /**
     * The current style.
     */
    @ScriptVariable
    protected Style currentStyle;

	@Self
	@Via(type = ResourceSuperType.class)
	List list;
	
	@ScriptVariable
	PageManager pageManager;
	
	@Self
	MacnicaLinkHandler linkHandler;
	

	/**
	 * Flag indicating if products should be grouped by family.
	 */
	@ValueMapValue
	private boolean groupByFamily;
	
	/**
	 * The page path configured in the component
	 */

	@ValueMapValue
	@Nullable
	private String parentPage;
	
	@ValueMapValue
	@Nullable
	private String listFrom;

	/**
	 * The current page.
	 */
	@ScriptVariable
	private Page currentPage;

	@SlingObject
	private ResourceResolver resourceResolver;

	/**
	 * The list of product pages
	 */
	private Collection<ListItem> products;

	/**
	 * The mapping of product page items with their product family
	 */

	private java.util.Map<String, Collection<ListItem>> productFamilyMap;

	/**
	 * Two letter Language code of the page in which the component is added
	 */

	private Locale language;
	

    @Self
    private MacnicaPredicateHandler predicateHandler;
	

    
	@OSGiService
	private JcrSearchService searchService;
	
    @Getter
    private String resourcePath; 
	


	/**
	 * Initialize the model.
	 */
	@PostConstruct
	protected void initModel() {
		  resourcePath = request.getResource().getPath();
		  try{

		logger.debug("Initialising product listing component at path {}", resource.getPath());
		language = currentPage.getLanguage();

		// If parent page is not authored, current page is considered as parent page
		if (StringUtils.isBlank(parentPage)) {
			parentPage = currentPage.getPath();
		}

		// Filter product detail pages from all child pages.
		products = list.getListItems().stream().filter(item -> isProductPage(item.getPath()))
				.collect(Collectors.toList());
				

		
		try{
			
			if(!listFrom.equals("static") && !listFrom.equals("tags")){
				logger.info("Initialising product listing component with custom predicates");
				Map<String,String> map = predicateHandler.getProductListPredicates();
				products = generateListItems(map).stream().filter(item -> isProductPage(item.getPath()))
						.collect(Collectors.toList());
				
			}
				
			} catch(Exception e){
				logger.error("Exception in generating predicates for Product list components {}",e);
			}
		
		if (groupByFamily) {
			Collection<ProductListItem> productFamilyItems = products.stream().map(ProductListItem::new)
					.collect(Collectors.toList());
			Map<String, java.util.List<ProductListItem>> productFamilyItemsMap = productFamilyItems.stream()
					.collect(Collectors.groupingBy((item) -> item.getProductFamily()));
			productFamilyMap = new HashMap<>();
			productFamilyItemsMap.forEach((k, v) -> productFamilyMap.put(k, getListItems(v)));
			productFamilyMap.forEach((k, v) -> logger.info("Key {} values {} ", k, v.size()));
		}
		  }
		catch(Exception e) {
			logger.error("Exception in initializing product listing component at path: {}", resourcePath, e);
			products = Collections.emptyList();
			productFamilyMap = Collections.emptyMap();
		}

	}
	
	private java.util.List<ListItem> generateListItems(@Nullable Map<String, String> map) {
		logger.debug("Generating items for predicates {} ", map.entrySet());
		if (map.isEmpty()) {
			logger.warn("No search predicates supplied to generate  items. Returning empty list");
			return Collections.emptyList();
		}
		// JCR lookup to build items
		logger.debug("Executing query to fetch pagelist for builing items");
		Session session = resource.getResourceResolver().adaptTo(Session.class);
		if (searchService == null || session == null) {
			logger.error(
					"Unable to generate items as session or searchSerice object is not available. session : {} searchService {}",
					session, searchService);
			return Collections.emptyList();
		}
		
			java.util.List<Page> pages = searchService.getPages(map, session);
			
			if (pages != null) {
				 
				return pages.stream().filter(Objects::nonNull)
						.map(page -> new MacnicaPageListItemImpl(component, linkHandler, page, getId()))
						.collect(Collectors.toList());
						
			}	
			
			logger.warn("No search results found");
		

		return Collections.emptyList();


	}

	/*
	 * Converts Collection of ProductListItem collection of ListItem
	 */
	private Collection<ListItem> getListItems(Collection<ProductListItem> productListItem) {
		return productListItem.stream().map(ProductListItem::getListItem).collect(Collectors.toList());

	}

	/*
	 * Determines if a given page is a product page or not
	 * 
	 * @Param pagePath: Path of page resource
	 * 
	 * @return: true, if the page template is product page template ; False
	 * otherwise
	 */
	private boolean isProductPage(@NotNull String pagePath) {
		Page page = resourceResolver.getResource(pagePath).adaptTo(Page.class);
		String templatePath = page.getContentResource().getValueMap().get(PN_CQ_TEMPLATE, String.class);
		logger.trace("page {} , Template {}, ",pagePath, templatePath);
		return (templatePath.contains(PRODUCT_TEMPLATE_NAME)|| templatePath.contains(MAE_PRODUCT_TEMPLATE_NAME));
	}


	@Override
	public boolean groupByFamily() {
		return groupByFamily;
	}

	
	@Override
	public Collection<ListItem> getProducts() {
		return products;
	}

	@Override
	public java.util.Map<String, Collection<ListItem>> getProductFamilyMap() {

		return productFamilyMap;
	}
	

	

	/*
	 * Custom list item type to handle product family of a page
	 */
	private class ProductListItem {

		@Getter
		protected ListItem listItem;
		@Getter
		protected String productFamily;
		private static final String PN_PRODUCT_FAMILY = "productFamily";

		ProductListItem(@NotNull ListItem listItem) {
			this.listItem = new MacnicaPageListItemImpl(component,linkHandler, pageManager.getPage(listItem.getPath()), getId());
			this.productFamily = getProductFamily(listItem.getPath()).orElse(StringUtils.EMPTY);
		}

		private Optional<String> getProductFamily(@NotNull String pagePath) {
			if (StringUtils.isBlank(pagePath)) {
				return Optional.empty();
			}
			Page page = resourceResolver.getResource(pagePath).adaptTo(Page.class);
			String productFamilyTagPath = page.getContentResource().getValueMap().get(PN_PRODUCT_FAMILY, String.class);
			TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
			Tag productFamilyTag = tagManager.resolve(productFamilyTagPath);
			if (productFamilyTag != null) {
				return Optional.ofNullable(productFamilyTag.getTitle(language));
			}
			return Optional.empty();
		}

	}
	

  
    
    


}
