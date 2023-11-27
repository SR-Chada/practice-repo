package com.macnicagwi.core.components.models;

import java.util.List;
import java.util.Map;

import com.adobe.cq.wcm.core.components.models.Component;

/**
 * {@code ManufacturerList} Sling Model
 * used for the {@code /apps/macnicagwi/components/content/manufacturerlist}
 * component.
 * 
 * @author Sai
 */
public interface ManufacturerList extends Component {

    /**
     * Provides the list of manufacturer detail pages and their corresponding
     * product line detail pages
     * 
     * @return Map of Manufacturer detail pages and their respective Product line detail pages. Each page will be of type {@code MacnicaPageListItem}
     */
    public Map<MacnicaPageListItem, List<MacnicaPageListItem>> getListItems();
    
    public Integer getTotalResultsSize();
    
    public String getResourcePath();
    
    

}
