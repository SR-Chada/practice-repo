package com.macnicagwi.core.components.models;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;

import com.adobe.cq.wcm.core.components.models.Image;
import com.adobe.cq.wcm.core.components.models.ListItem;
import com.drew.lang.annotations.Nullable;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Macnica's extended list item implementation for a page-backed list item.
 * 
 * @implSpec Extends {@link com.adobe.cq.wcm.core.components.models.ListItem}
 * @author Sai
 *
 */
public interface MacnicaPageListItem extends MacnicaListItem {

	/**
	 * Fetches the article date associated with {@code date} property of the page's
	 * {@code jcr:content} node. The date will be a formatted base on the context
	 * aware configuration - {@code DateFormatConfig } of the page
	 * 
	 * @return date associated to the page if authored, blank String otherwise.
	 */
	@JsonProperty("date")
	public default String getArticleDate() {
		return StringUtils.EMPTY;
	}

	/**
	 * Fetches the Event start date of the event details page.The date will be a
	 * formatted base on the context aware configuration - {@code DateFormatConfig }
	 * of the page
	 * 
	 * @return date associated to the page if authored, blank String otherwise.
	 */
	public default String getEventStartDate() {
		return StringUtils.EMPTY;
	}

	/**
	 * Fetches the Event end date of the event details page.The date will be a
	 * formatted base on the context aware configuration - {@code DateFormatConfig }
	 * of the page
	 * 
	 * @return date associated to the page if authored, blank String otherwise.
	 */
	public default String getEventEndDate() {
		return StringUtils.EMPTY;
	}

	/**
	 * Fetches the Event registration start date of the event details page.The date
	 * will be a formatted base on the context aware configuration -
	 * {@code DateFormatConfig } of the page
	 * 
	 * @return date associated to the page if authored, blank String otherwise.
	 */
	public default String getEventRegistrationStartDate() {
		return StringUtils.EMPTY;
	}

	/**
	 * Fetches the Event registration end date of the event details page.The date
	 * will be a formatted base on the context aware configuration -
	 * {@code DateFormatConfig } of the page
	 * 
	 * @return date associated to the page if authored, blank String otherwise.
	 */
	public default String getEventRegistrationEndDate() {
		return StringUtils.EMPTY;
	}

	/**
	 * Fetches the Event location from the event details page.
	 * 
	 * @return date associated to the page if authored, blank String otherwise.
	 */
	@JsonProperty("location")
	public default String getEventLocation() {
		return StringUtils.EMPTY;
	}

	/**
	 * Fetches the Event status from the event details page.
	 * 
	 * @return date associated to the page if authored, blank String otherwise.
	 */
	@JsonProperty("status")
	public default String getEventStatus() {
		return StringUtils.EMPTY;
	}

	/**
	 * This method returns a resource that is an Macnica GWI Image Component
	 * resource (rather than an image binary, such as a DAM asset). This resource is
	 * intended to be rendered via the Macnica GWI Image Component's logic via a
	 * Sling include of this resource.
	 * 
	 * @return the (Sling) resource that represents that image to display in the
	 *         list item. (Typically featured image of a page)
	 */
	@Nullable
	public Resource getImage();
	
	
	public Resource getProductLineLogo();
	
	public Resource getManufacturerLogo();

	public default String getType(){
		return StringUtils.EMPTY;
	}

}
