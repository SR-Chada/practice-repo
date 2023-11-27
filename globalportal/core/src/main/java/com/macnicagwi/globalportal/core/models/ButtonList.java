package com.macnicagwi.globalportal.core.models;

import java.util.Collection;



import org.osgi.annotation.versioning.ProviderType;

import com.adobe.cq.wcm.core.components.models.ListItem;
import com.drew.lang.annotations.NotNull;

@ProviderType
public interface ButtonList extends com.adobe.cq.wcm.core.components.models.List{
	
	public @NotNull Collection<ListItem> getListItems();

	public String getTitle();
	public String getType();

}
