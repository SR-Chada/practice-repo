package com.macnicagwi.globalportal.core.models;

import java.util.Collection;
import java.util.Collections;

import org.osgi.annotation.versioning.ConsumerType;

import com.adobe.cq.export.json.ComponentExporter;
import com.macnicagwi.globalportal.core.models.impl.PageSectionListingImpl.PageSectionListItem;

@ConsumerType
public interface PageSectionListing extends ComponentExporter {

	default Collection<PageSectionListItem> getListItems() {
		return Collections.emptyList();
	}

}
