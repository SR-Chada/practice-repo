package com.macnicagwi.core.components.models.impl;

import javax.annotation.PostConstruct;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.util.AbstractComponentImpl;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.macnicagwi.core.components.models.PageProperties;
import com.macnicagwi.core.utils.MacnicaComponentUtils;
import java.util.ArrayList;
import java.util.stream.Stream;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;

import static com.macnicagwi.core.constants.MacnicaCoreConstants.NN_AUTHOR_IMAGE;
import static com.macnicagwi.core.constants.MacnicaCoreConstants.NN_MANUFACTURER_LOGO;

import java.util.stream.Stream;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = {PageProperties.class, ComponentExporter.class},
    resourceType = PagePropertiesImpl.RESOURCE_TYPE,
	defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
@Exporter(
    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
    extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class PagePropertiesImpl extends AbstractComponentImpl implements PageProperties {
	private static final Logger log = LoggerFactory.getLogger(PagePropertiesImpl.class);
	public static final String RESOURCE_TYPE = "macnicagwi/components/content/pageproperties";
	@Self
	private SlingHttpServletRequest request;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Nullable
	private String newsDate;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Nullable
	private String newsDateLabel;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Nullable
	private String eventStartDate;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Nullable
	private String eventStartDateLabel;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Nullable
	private String eventEndDate;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Nullable
	private String eventEndDateLabel;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Nullable
	private String eventLocation;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Nullable
	private String eventLocationLabel;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Nullable
	private String technicalArticleDate;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Nullable
	private String authorName;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Nullable
	private String articleSource;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Nullable
	private String technicalArticleDateLabel;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Nullable
	private String authorNameLabel;

	@ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
	@Nullable
	private String articleSourceLabel;
	
	private Resource authorImage;
	
	private Resource manufacturerLogo;
	
	private String attribute;
	private String isEmpty;

	@PostConstruct
	public void init() {
		ValueMap valueMap = resource.getValueMap();
		String authorimage = valueMap.get("authorImage",String.class);
		String manufacturerlogo = valueMap.get("manufacturerLogo",String.class);

		String[] attributes = {newsDate,eventStartDate,eventEndDate,eventLocation,articleSource,technicalArticleDate,authorName,authorimage,manufacturerlogo};
		ArrayList<String> arr = new ArrayList<String>();
		for(int i = 0;i < attributes.length ; i++) {	   
				String attribute = attributes[i];		 
			   String value = isChecked(attribute).toString();
				arr.add(value);	  
	}
	Stream<ArrayList<String>> stream = Stream.of(arr);
	Boolean match = stream.anyMatch(s -> s.contains("true"));
   
   if(match) {
	   isEmpty = "false";
   }else {
	   isEmpty = "true";
   }
		// TO-DO: Dynmically Inject Prodperties
		PageManager pageManager = request.getResourceResolver().adaptTo(PageManager.class);
		Page containingPage = pageManager.getContainingPage (resource);
		newsDate = MacnicaComponentUtils.getPagePropertyValue(containingPage, newsDate);
		eventStartDate = MacnicaComponentUtils.getPagePropertyValue(containingPage, eventStartDate);
		eventEndDate = MacnicaComponentUtils.getPagePropertyValue(containingPage, eventEndDate);
		eventLocation = MacnicaComponentUtils.getPagePropertyValue(containingPage, eventLocation);
		technicalArticleDate = MacnicaComponentUtils.getPagePropertyValue(containingPage, technicalArticleDate);
		authorName = MacnicaComponentUtils.getPagePropertyValue(containingPage, authorName);
		articleSource = MacnicaComponentUtils.getPagePropertyValue(containingPage, articleSource);
		authorImage = containingPage.getContentResource(NN_AUTHOR_IMAGE);
		manufacturerLogo = containingPage.getContentResource(NN_MANUFACTURER_LOGO);
		
	}
	    
	@Override
	public String getNewsDate() {
	    return newsDate;
	}
	    
	@Override
	public String getNewsDateLabel() {
	    return newsDateLabel;
	}

	@Override
	public String getEventStartDate() {
	    return eventStartDate;
	}
	    
	@Override
	public String getEventStartDateLabel() {
	    return eventStartDateLabel;
	}

	@Override
	public String getEventEndDate() {
	    return eventEndDate;
	}
	    
	@Override
	public String getEventEndDateLabel() {
	    return eventEndDateLabel;
	}

	@Override
	public String getEventLocation() {
	    return eventLocation;
	}
	    
	@Override
	public String getEventLocationLabel() {
	    return eventLocationLabel;
	}

	@Override
	public String getTechnicalArticleDate() {
	    return technicalArticleDate;
	}

	@Override
	public String getAuthorName() {
	    return authorName;
	}
	
	@Override
	public String getArticleSource() {
	    return articleSource;
	}


	@Override
	public String getTechnicalArticleDateLabel() {
	    return technicalArticleDateLabel;
	}

	@Override
	public String getArticleSourceLabel() {
	    return articleSourceLabel;
	}

	@Override
	public String getAuthorNameLabel() {
	    return authorNameLabel;
	}
	
	@Override
	public Resource getAuthorImage() {
	    return authorImage;
	}

	@Override
	public Resource getManufacturerLogo() {
	    return manufacturerLogo;
	}
	
	@NotNull
 	@Override
	public String getExportedType() {
	    return request.getResource().getResourceType();
	}
	
	public String isEmpty(){
		return isEmpty;
	}

	public Boolean isChecked(String attribute){
		if (StringUtils.isBlank(attribute) || attribute.contains("false")) {
            return false;
        }
			return true;
	}

}
