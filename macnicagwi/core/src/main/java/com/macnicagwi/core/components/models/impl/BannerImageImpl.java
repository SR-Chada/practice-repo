package com.macnicagwi.core.components.models.impl;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.factory.ModelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.models.Image;
import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.day.cq.wcm.api.Page;
import com.drew.lang.annotations.Nullable;
import com.macnicagwi.core.components.models.BannerImage;
import com.macnicagwi.core.models.MacnicaLinkHandler;

@Model(adaptables = SlingHttpServletRequest.class, adapters = { BannerImage.class,
		ComponentExporter.class }, resourceType = {
				BannerImageImpl.RESOURCE_TYPE }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)

public class BannerImageImpl extends AbstractComponentImpl implements BannerImage {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public static final String RESOURCE_TYPE = "macnicagwi/components/content/bannerimage";


	@Self
	private MacnicaLinkHandler linkHandler;

	@ValueMapValue
	@Nullable
	private String subText;

	@OSGiService
	private ModelFactory modelFactory;

	@ValueMapValue
	@Nullable
	private boolean isPopup;


	@SuppressWarnings("rawtypes")
	@Nullable
	Link imageLink;

	private Image image;

	/**
	 * Default constructor for container to initialize the object of SlingModel
	 */
	public BannerImageImpl() {
		// DO Nothing
	}

	/**
	 * Constructor for generating Sling model object for usage in listing components
	 * 
	 * @param subText : Sub Text to be displayed on the Banner
	 * @param image:  Image Sling model object
	 */
	public BannerImageImpl(String subText, Image image, Optional<Link<Page>> imageLink) {
		logger.trace("Initializing BannerImage component with SubText: {} Image: {} RedirectLink: {} ", subText, image);
		this.subText = subText;
		this.image = image;
		this.isPopup = false;
		this.imageLink = imageLink.orElse(null);
	}

	@PostConstruct
	private void init() {
		// set the image object
		image = modelFactory.getModelFromWrappedRequest(request, request.getResource(), Image.class);
		logger.trace("Image Src {} fileref {} ", image.getSrc(), image.getFileReference());

	}

	@Override
	public String getSubText() {
		return subText;
	}

	@Override
	public boolean isEmpty() {
		final Image componentImage = getImage();
		return (componentImage == null || StringUtils.isBlank(componentImage.getSrc()));

	}

	/**
	 * @return the Image Sling Model of this resource, or null if the resource
	 *         cannot create a valid Image Sling Model.
	 */

	@Override
	public Image getImage() {
		return image;
	}

	@SuppressWarnings("rawtypes")
	@Nullable
	@Override
	public Link getImageLink() {
		if (image.getImageLink() != null && image.getImageLink().isValid()) {
			return image.getImageLink();
		}
		
		return imageLink;
	}



}
