package com.macnicagwi.core.components.models.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.jcr.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Required;
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
import com.macnicagwi.core.components.models.ContactCards;
import com.macnicagwi.core.services.DamAssetService;

import lombok.Getter;

@Model(adaptables = {SlingHttpServletRequest.class, Resource.class}, adapters = { ContactCards.class,
		ComponentExporter.class }, resourceType = ContactCardsImpl.RESOURCE_TYPE, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class ContactCardsImpl extends AbstractComponentImpl implements ContactCards {

	public static final String RESOURCE_TYPE = "macnicagwi/components/content/contactcards";
	public static final String OFFICE_LOCATION_CFM_LOACTION_TYPE_PROPERTY_PATH = "jcr:content/data/master/locationType";
	public static final String OFFICE_LOCATION_CFM_RANKING_PROPERTY_PATH = "jcr:content/data/master/ranking";
	public static final Logger LOGGER = LoggerFactory.getLogger(ContactCardsImpl.class);
	
	@Self
	SlingHttpServletRequest request;

	@SlingObject
	@Required
	private ResourceResolver resourceResolver;

	@OSGiService
	private DamAssetService damAssetService;

	@ValueMapValue
	private String parentPath;

	@ValueMapValue
	private String locationType;

	@ValueMapValue
	@Default(values = "OPEN IN GOOGLE MAPS")
	private String googleMapsLabel;

	@ValueMapValue
	private String routeInstructionsLabel;

	@ValueMapValue
	@Default(values = "ROUTE INSTRUCTIONS")
	private String routeInstructionsPopupTitle;

	@ValueMapValue
	@Default(values = "PUBLIC TRANSPORT")
	private String publicTransportTabLabel;

	@ValueMapValue
	@Default(values = "CAR")
	private String carTabLabel;

	@ValueMapValue
	@Default(values = "MOTORCYCLE")
	private String motorcycleTabLabel;

	@ValueMapValue
	@Default(values = "BUSINESS")
	private String businessEmailLabel;

	@ValueMapValue
	@Default(values = "TECHNICAL")
	private String technicalEmailLabel;

	@ValueMapValue
	@Default(values = "SUPPORT")
	private String supportEmailLabel;

	@ValueMapValue
	@Default(values = "READ MORE")
	private String readMoreButtonLabel;

	@ValueMapValue
	private String orderBy;
	
	private List<OfficeLocation> officeLocations;

	@PostConstruct
	public void init() {
		LOGGER.info("Cards initialised");
		try{
		Session session = resourceResolver.adaptTo(Session.class);
		officeLocations = new ArrayList<>();

		if (StringUtils.isNotBlank(parentPath)) {
			List<ContentFragment> contentFragments = damAssetService.getFilteredContentFragments(Collections.singletonList(getLocationType()), OFFICE_LOCATION_CFM_LOACTION_TYPE_PROPERTY_PATH, getParentPath(), session);

			if (!contentFragments.isEmpty()) {
				contentFragments.forEach(contentFragment -> officeLocations.add(new OfficeLocation(contentFragment)));
				 // Sort the officeLocations list based on the ranking property	
				 try{
				 if(orderBy != null && orderBy.contains("ranking")){
				 Collections.sort(officeLocations, new Comparator<OfficeLocation>() {
					@Override
					public int compare(OfficeLocation o1, OfficeLocation o2) {
						
						int ranking1 = Integer.parseInt(o1.getRanking());
						int ranking2 = Integer.parseInt(o2.getRanking());
	
						
						return Integer.compare(ranking1, ranking2);
					}
					 });
					}
				}
				catch(Exception e){
					LOGGER.error("Error in sorting: ", e);
				}
			}
		}
	}
	catch(Exception e){
		LOGGER.error("Exception: ", e);
	}
}


	@Override
	public String getOrderBy() {
		return orderBy;
	}
	@Override
	public List<OfficeLocation> getOfficeLocations() {
		return officeLocations;
	}

	@Override
	public String getParentPath() {
		return parentPath;
	}

	@Override
	public String getLocationType() {
		return locationType;
	}

	@Override
	public String getGoogleMapsLabel() {
		return googleMapsLabel;
	}

	@Override
	public String getRouteInstructionsLabel() {
		return routeInstructionsLabel;
	}

	@Override
	public String getRouteInstructionsPopupTitle() {
		return routeInstructionsPopupTitle;
	}

	@Override
	public String getPublicTransportTabLabel() {
		return publicTransportTabLabel;
	}

	@Override
	public String getCarTabLabel() {
		return carTabLabel;
	}

	@Override
	public String getMotorcycleTabLabel() {
		return motorcycleTabLabel;
	}

	@Override
	public String getBusinessEmailLabel() {
		return businessEmailLabel;
	}

	@Override
	public String getTechnicalEmailLabel() {
		return technicalEmailLabel;
	}

	@Override
	public String getSupportEmailLabel() {
		return supportEmailLabel;
	}

	@Override
	public String getReadMoreButtonLabel() {
		return readMoreButtonLabel;
	}

	public class OfficeLocation {

		public static final String OFFICE_LOCATION_CFM_TITLE_PROPERTY = "title";
		public static final String OFFICE_LOCATION_CFM_COMPANYNAME_PROPERTY = "companyname";
		public static final String OFFICE_LOCATION_CFM_ADDRESS_PROPERTY = "address";
		public static final String OFFICE_LOCATION_CFM_RANKING_PROPERTY = "ranking";
		public static final String OFFICE_LOCATION_CFM_HEAD_QUARTER_PROPERTY = "markAsHeadquarter";
		public static final String OFFICE_LOCATION_CFM_GOOGLE_MAP_LINK_PROPERTY = "googleMapLink";
		public static final String OFFICE_LOCATION_CFM_PUBLIC_TRANSPORT_RI_PROPERTY = "publicTransportRouteInstructions";
		public static final String OFFICE_LOCATION_CFM_CAR_RI_PROPERTY = "carRouteInstructions";
		public static final String OFFICE_LOCATION_CFM_MOTORCYCLE_RI_PROPERTY = "motorcycleRouteInstructions";
		public static final String OFFICE_LOCATION_CFM_MOBILE_NUMBER_PROPERTY = "mobileNumber";
		public static final String OFFICE_LOCATION_CFM_FAX_NUMBER_PROPERTY = "faxNumber";
		public static final String OFFICE_LOCATION_CFM_BUSINESS_EMAIL_PROPERTY = "businessEmail";
		public static final String OFFICE_LOCATION_CFM_TECHNICAL_EMAIL_PROPERTY = "technicalEmail";
		public static final String OFFICE_LOCATION_CFM_SUPPORT_EMAIL_PROPERTY = "supportEmail";

		@Getter
		private String title;

		@Getter
		private String companyname;
		
		@Getter
		private String address;

		@Getter
		private String ranking;

		@Getter
		private String markAsHeadquarter;

		@Getter
		private String googleMapLink;

		@Getter
		private String publicTransportRouteInstructions;

		@Getter
		private String carRouteInstructions;

		@Getter
		private String motorcycleRouteInstructions;

		@Getter
		private String mobileNumber;

		@Getter
		private String faxNumber;

		@Getter
		private String businessEmail;

		@Getter
		private String technicalEmail;

		@Getter
		private String supportEmail;

		public OfficeLocation(ContentFragment officeLocationCfm) {
			this.title = officeLocationCfm.getElement(OFFICE_LOCATION_CFM_TITLE_PROPERTY).getContent();
			this.companyname = officeLocationCfm.getElement(OFFICE_LOCATION_CFM_COMPANYNAME_PROPERTY).getContent();
			this.address = officeLocationCfm.getElement(OFFICE_LOCATION_CFM_ADDRESS_PROPERTY).getContent();
			this.ranking = officeLocationCfm.getElement(OFFICE_LOCATION_CFM_RANKING_PROPERTY).getContent();
			this.markAsHeadquarter = officeLocationCfm.getElement(OFFICE_LOCATION_CFM_HEAD_QUARTER_PROPERTY).getContent();
			this.googleMapLink = officeLocationCfm.getElement(OFFICE_LOCATION_CFM_GOOGLE_MAP_LINK_PROPERTY).getContent();
			this.publicTransportRouteInstructions = officeLocationCfm.getElement(OFFICE_LOCATION_CFM_PUBLIC_TRANSPORT_RI_PROPERTY).getContent();
			this.carRouteInstructions = officeLocationCfm.getElement(OFFICE_LOCATION_CFM_CAR_RI_PROPERTY).getContent();
			this.motorcycleRouteInstructions = officeLocationCfm.getElement(OFFICE_LOCATION_CFM_MOTORCYCLE_RI_PROPERTY).getContent();
			this.mobileNumber = officeLocationCfm.getElement(OFFICE_LOCATION_CFM_MOBILE_NUMBER_PROPERTY).getContent();
			this.faxNumber = officeLocationCfm.getElement(OFFICE_LOCATION_CFM_FAX_NUMBER_PROPERTY).getContent();
			this.businessEmail = officeLocationCfm.getElement(OFFICE_LOCATION_CFM_BUSINESS_EMAIL_PROPERTY).getContent();
			this.technicalEmail = officeLocationCfm.getElement(OFFICE_LOCATION_CFM_TECHNICAL_EMAIL_PROPERTY).getContent();
			this.supportEmail = officeLocationCfm.getElement(OFFICE_LOCATION_CFM_SUPPORT_EMAIL_PROPERTY).getContent();
			
		}
	}
}
