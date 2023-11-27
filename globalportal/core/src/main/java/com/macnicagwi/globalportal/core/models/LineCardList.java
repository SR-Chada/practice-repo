package com.macnicagwi.globalportal.core.models;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.adobe.cq.wcm.core.components.models.contentfragment.ContentFragmentList;

public interface LineCardList extends ContentFragmentList  {
	
public static final String DEFAULT_GLOBAL_PORTAL_CF_PATH = "/content/dam/globalportal/public/en/content-fragments/";

public static final String JSON_PN_ITEMS_MAP = "itemsMap";

public static final String JSON_PN_ITEMS = "itemsList";

public default Map<String, List<LineCardItem>> getItemsMap(){
	return Collections.emptyMap();
}

public default List<LineCardItem> getItems(){
	return Collections.emptyList();
}
    
}
