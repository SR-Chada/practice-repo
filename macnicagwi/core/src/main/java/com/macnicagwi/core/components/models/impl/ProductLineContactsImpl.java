package com.macnicagwi.core.components.models.impl;

import static com.macnicagwi.core.constants.MacnicaCoreConstants.LINE_SEPARATOR;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.PRODUCT_LINE_LOGOS_PARENT_FOLDER_PATH;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.jcr.Session;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Required;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.dam.cfm.ContentFragment;
import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.day.cq.wcm.api.LanguageManager;
import com.drew.lang.annotations.NotNull;
import com.macnicagwi.core.components.dtos.ProductLineContactCfmDto;
import com.macnicagwi.core.components.models.ProductLineContacts;
import com.macnicagwi.core.constants.MacnicaCoreConstants;
import com.macnicagwi.core.models.MacnicaPredicateHandler;
import com.macnicagwi.core.services.DamAssetService;
import com.macnicagwi.core.utils.MacnicaComponentUtils;

@Model(adaptables = SlingHttpServletRequest.class, adapters = { ProductLineContacts.class,
		ComponentExporter.class }, resourceType = ProductLineContactsImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class ProductLineContactsImpl extends AbstractComponentImpl implements ProductLineContacts {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductLineContactsImpl.class);

	public static final String RESOURCE_TYPE = "macnicagwi/components/content/productlinecontacts";
	public static final String PRODUCT_LINE_CONTACT_CFM_PARENT_FOLDER_PATH = "/content/dam/macnicagwi/content-fragments/product-line-contacts/%s/%s/%s";
	public static final String PRODUCT_LINE_CONTACT_CFM_PRODUCT_LINES_PROPERTY_PATH = "jcr:content/data/master/productLines";
	public static final String PRODUCT_LINE_CONTACT_CFM_NAME_PROPERTY = "name";
	public static final String PRODUCT_LINE_CONTACT_CFM_PRIMARY_CONTACT_NUMBER_PROPERTY = "primaryContactNumber";
	public static final String PRODUCT_LINE_CONTACT_CFM_SECONDARY_CONTACT_NUMBER_PROPERTY = "secondaryContactNumber";
	public static final String PRODUCT_LINE_CONTACT_CFM_EMAIL_ID_PROPERTY = "emailId";
	public static final String PRODUCT_LINE_CONTACT_CFM_PRODUCT_LINES_PROPERTY = "productLines";

	@Self
	SlingHttpServletRequest request;

	@SlingObject
	@Required
	private ResourceResolver resourceResolver;

	@OSGiService
	private LanguageManager languageManager;

	@OSGiService
	private DamAssetService damAssetService;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	private String[] productLines;

	private List<ProductLineContactCfmDto> productLineContactCfmDtos;

	private String productLineLogosParentFolderPath;

	private Session session;

	@PostConstruct
	public void init() {
	    try {
	        session = resourceResolver.adaptTo(Session.class);
	        String regionName = MacnicaComponentUtils.getRegionName(languageManager, request.getResource());
	        String subsidiaryName = MacnicaComponentUtils.getSubsidiaryName(languageManager, request.getResource());
	        String languageRootName = MacnicaComponentUtils.getLanguageRootName(languageManager, request.getResource());

	        String productLineContactCfmParentFolderPath = String.format(PRODUCT_LINE_CONTACT_CFM_PARENT_FOLDER_PATH, regionName,
	                subsidiaryName, languageRootName);
	        productLineLogosParentFolderPath = String.format(PRODUCT_LINE_LOGOS_PARENT_FOLDER_PATH, regionName,
	                subsidiaryName, languageRootName);

	        productLineContactCfmDtos = new ArrayList<>();
	        if (!getProductLines().isEmpty()) {
	            List<ContentFragment> contentFragments = damAssetService.getFilteredContentFragments(getProductLines(),
	                PRODUCT_LINE_CONTACT_CFM_PRODUCT_LINES_PROPERTY_PATH, productLineContactCfmParentFolderPath, session);

	            if (!contentFragments.isEmpty()) {
	                contentFragments.forEach(contentFragment -> productLineContactCfmDtos.add(getProductLineContactCfmDto(contentFragment)));
	            }

	        }
	        
	    } catch(Exception e) {
	        LOGGER.error("Unable to initialize ProductLineContact component at path {}. ", resource.getPath(),e);
	    }

	}

	@Override
	public List<String> getProductLines() {
		if (productLines == null || productLines.length == 0) {
			return Collections.emptyList();
		}
		return Arrays.asList(productLines);
	}

	@Override
	public List<ProductLineContactCfmDto> getProductLineContactCfmDtos() {
		return productLineContactCfmDtos;
	}

	@NotNull
	@Override
	public String getExportedType() {
		return request.getResource().getResourceType();
	}

	private ProductLineContactCfmDto getProductLineContactCfmDto(ContentFragment contentFragment) {
		ProductLineContactCfmDto productLineContactCfmDto = new ProductLineContactCfmDto();
		productLineContactCfmDto.setName(contentFragment.getElement(PRODUCT_LINE_CONTACT_CFM_NAME_PROPERTY).getContent());
		productLineContactCfmDto
				.setPrimaryContactNumber(contentFragment.getElement(PRODUCT_LINE_CONTACT_CFM_PRIMARY_CONTACT_NUMBER_PROPERTY).getContent());
		productLineContactCfmDto
				.setSecondaryContactNumber(contentFragment.getElement(PRODUCT_LINE_CONTACT_CFM_SECONDARY_CONTACT_NUMBER_PROPERTY).getContent());
		productLineContactCfmDto.setEmailId(contentFragment.getElement(PRODUCT_LINE_CONTACT_CFM_EMAIL_ID_PROPERTY).getContent());
		productLineContactCfmDto.setProductLineLogos(damAssetService.getProductLineLogos(
				Arrays.asList(contentFragment.getElement(PRODUCT_LINE_CONTACT_CFM_PRODUCT_LINES_PROPERTY).getContent().split(LINE_SEPARATOR)),
				MacnicaCoreConstants.ASSET_METADATA_PRODUCT_LINE_PROPERTY_PATH, productLineLogosParentFolderPath, session));

		return productLineContactCfmDto;
	}

}
