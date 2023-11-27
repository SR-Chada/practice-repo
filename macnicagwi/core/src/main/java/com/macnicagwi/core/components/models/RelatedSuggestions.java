package com.macnicagwi.core.components.models;

import java.util.Collection;

import com.adobe.cq.wcm.core.components.models.Component;
import com.adobe.cq.wcm.core.components.models.List;
import com.adobe.cq.wcm.core.components.models.ListItem;

/**
 * {@code ProductLineListing} Sling Model used for the
 * {@code /apps/macnicagwi/components/content/productlisting} component.
 * 
 * @author Sai
 */

public interface RelatedSuggestions extends Component {

	/*
	 * @return : Collection of ListItems for product pages
	 */
	public Collection<ListItem> getListItems();

	public String getOrderBy();
	public String getListFrom();
	public String getMaxItems();
	public String getQuery();
	public String getSortOrder();

}
