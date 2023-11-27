package com.macnicagwi.core.components.models.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.factory.ModelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.Image;
import com.adobe.cq.wcm.core.components.models.datalayer.ComponentData;
import com.adobe.cq.wcm.core.components.models.datalayer.builder.DataLayerBuilder;
import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.adobe.cq.wcm.core.components.util.ComponentUtils;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.macnicagwi.core.components.models.BannerImage;
import com.macnicagwi.core.components.models.BannerImageList;
import com.macnicagwi.core.models.MacnicaLinkHandler;

@Model(adaptables = SlingHttpServletRequest.class, adapters = { BannerImageList.class,
		ComponentExporter.class }, resourceType = BannerImagListImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class BannerImagListImpl  extends AbstractComponentImpl   implements BannerImageList {

	public static final String RESOURCE_TYPE = "macnicagwi/components/content/bannerimagelist";
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
    @OSGiService
    private ModelFactory modelFactory;

	@Self
	private SlingHttpServletRequest request;


	/**
	 * The current page.
	 */
	@ScriptVariable
	private Page currentPage;
	
	/**
	 * Page Manager reference
	 */
	@ScriptVariable
	private PageManager pageManager;
	
	@Self
	private MacnicaLinkHandler linkHandler;


	/**
	 * The current resource.
	 */

	@ScriptVariable
	private Resource resource;

	@ValueMapValue
	@Nullable
	private String parentPagePath;

	private java.util.List<BannerImage> childPageBanners;

	@PostConstruct
	private void init() {

		childPageBanners = new ArrayList<>();
		Iterator<Page> childPages;
		if (parentPagePath == null) {
			logger.trace("Parent page path not provided for component:  {} at resource: {}",RESOURCE_TYPE, resource.getPath());			
		}

			try {
				Optional<Page> rootPage;
				
				Optional<String> parentPath = Optional.ofNullable(this.parentPagePath).filter(StringUtils::isNotBlank);
				
				if(parentPath.isPresent()) {
					rootPage =  Optional.ofNullable(pageManager.getContainingPage(parentPath.get()));
				} else {
					rootPage = Optional.of(currentPage);
				}
				childPages = com.macnicagwi.core.utils.MacnicaComponentUtils.getChildListItems(rootPage);					

				if (childPages != null) {
					logger.trace("iterating over child pages");					
					childPages.forEachRemaining(childPage -> {
						BannerImage bannerImage = createBannerImageObject(childPage);
						if(bannerImage!=null)
						
						childPageBanners.add(bannerImage);
						});
				} else {
					logger.trace("At resource {}; No child pages found for parent: {} ", resource.getPath(),
							rootPage.get().getPath());
				}

			} catch (Exception e) {
				logger.error("Error in BannerImagListImpl while streaming child page information {} ", e);

			}

		
	}

	@Override
	public String getParentPagePath() {
		return parentPagePath;
	}

	@Override
	public List<BannerImage> getChildPageBanners() {
		return childPageBanners;
	}

	@Override
	@NotNull
	protected ComponentData getComponentData() {
		return DataLayerBuilder.extending(super.getComponentData()).asComponent().withLinkUrl(this::getParentPagePath)
				.build();
	}

	/**
	 * Get the root page.
	 *
	 * The root page is the page referenced by the parentPagePath property, or the
	 * current page if the parentPagePath property is not set or is blank. This
	 * function will return empty only if the fieldName property is set, but the
	 * referenced page does not exist.
	 *
	 * @param fieldName The name of the property containing the path of the root
	 *                  page.
	 * @return The root page, or empty if the page does not exist.
	 */
	@NotNull
	private Optional<Page> getRootPage() {
		Optional<String> parentPath = Optional.ofNullable(this.parentPagePath).filter(StringUtils::isNotBlank);

		// no path is specified, use current page
		if (!parentPath.isPresent()) {
			return Optional.of(this.currentPage);
		}

		// a path is specified, get that page or return null
		return parentPath.map(resource.getResourceResolver()::getResource)
				.map(currentPage.getPageManager()::getContainingPage);
	}



	/**
	 * @param page: Child Page for creating BannerImage Object Creates BannerImage
	 *              object based on page properties of Child Page
	 * @return bannerImage: BannerImage Object for the page passed
	 */
	private BannerImage createBannerImageObject(Page page) {
		
		logger.debug("Fetching properties for page {} ", page.getPath());
		BannerImage bannerImage = null;
		Resource imageResource = ComponentUtils.getFeaturedImage(page);
		if (imageResource!=null) {
			logger.debug("Fetching page properties of resource {} ", imageResource.getPath());
			Image image =  modelFactory.getModelFromWrappedRequest(request, imageResource, Image.class);
			String subText = page.getTitle();
			bannerImage =  image!=null?new BannerImageImpl(subText, image, linkHandler.getLink(page)):null;
		} else {
			logger.debug("Necessary page properties are not configured for Page  {} ", page.getPath());
		}
		
		return bannerImage;

	}

}
