package com.macnicagwi.core.components.models;

import java.util.Collection;
import java.util.Map;

import com.adobe.cq.wcm.core.components.models.Component;
import com.adobe.cq.wcm.core.components.models.ListItem;

/**
 * {@code ProductLineListing} Sling Model 
 * used for the {@code /apps/macnicagwi/components/content/productlisting} component.
 * @author Sai
 */
public interface ProductListing extends Component  {
	
	/*
	 * @return : Collection of ListItems for product pages
	 */
	public Collection<ListItem> getProducts(); 
	
	/*
	 * @return: True if the Product Listing component is configured to group product pages by family; False otherwise. 
	 */
	public boolean groupByFamily();

	/*
	 * @return: (Type: Map) Collection of product pages grouped by their product family
	 */
	public Map<String, Collection<ListItem>> getProductFamilyMap();

}
