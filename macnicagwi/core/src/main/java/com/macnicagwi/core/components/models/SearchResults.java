package com.macnicagwi.core.components.models;

import java.util.List;

import com.adobe.cq.wcm.core.components.models.Component;
import com.adobe.cq.wcm.core.components.models.ListItem;

public interface SearchResults extends Component {
    
    public List<MacnicaPageListItem> getPages();
    
    public List<MacnicaAssetListItem> getAssets();
    
    public List<ListItem> getListItems();
    
    public Integer getTotalResultsSize();
    
    public String getQueryString();

}
