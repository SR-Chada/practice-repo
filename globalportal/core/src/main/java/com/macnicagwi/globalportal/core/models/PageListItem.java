package com.macnicagwi.globalportal.core.models;

import org.apache.sling.api.resource.Resource;

import com.adobe.cq.wcm.core.components.models.ListItem;

public interface PageListItem extends ListItem {
	
	/**
	 * Returns the resource holding the properties of the featured image of the page.
	 * @return the featured image resource associated with the page list item
	 */
	public Resource getFeaturedImage();

	/**
	 * Returns the SVG icon resource configured in page properties 
	 * @return the SVG icon resource
	 */
	Resource getPageIcon();

	/**
	 * Returns the subtitle in page properties 
	 * @return the subtitle 
	 */
	public String getPageSubtitle();
	
	/**
	 * Returns the teaserTitle in page properties 
	 * @return the teaserTitle property if configured of page; jcr:title otherwise 
	 */
	public String getTeaserTitle();

}
