package com.macnicagwi.core.components.models;

import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang3.StringUtils;
import org.osgi.annotation.versioning.ConsumerType;

import com.adobe.cq.wcm.core.components.models.Component;
import com.macnicagwi.core.components.models.impl.PageSectionListingImpl.PageSectionListItem;

/**
 * {@code PageSectionListing} Sling Model. 
 * used for the {@code /apps/macnicagwi/components/content/pagesectionlisting} component.
 * @author Sai
 */

@ConsumerType
public interface PageSectionListing extends Component {

	default Collection<PageSectionListItem> getListItems(){
		return Collections.emptyList();
	}
	
	

}
