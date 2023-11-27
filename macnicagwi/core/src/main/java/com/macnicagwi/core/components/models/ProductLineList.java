package com.macnicagwi.core.components.models;

import java.util.Collection;
import java.util.Map;

import com.adobe.cq.wcm.core.components.models.ListItem;

/**
 * {@code ProductLineList} Sling Model 
 * used for the {@code /apps/macnicagwi/components/content/productlinelist} component.
 * @author Sai
 */

public interface ProductLineList {
    
    /**
     * Returns list of Product Line detail pages against a configured rootPath in ProductLineList component 
     * @return List of MacnicaPageListItems 
     */
    public Collection<MacnicaPageListItem> getListItems();
    
    

}
