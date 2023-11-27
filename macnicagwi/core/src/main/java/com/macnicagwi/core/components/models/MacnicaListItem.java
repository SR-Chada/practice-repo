package com.macnicagwi.core.components.models;

import com.adobe.cq.wcm.core.components.models.ListItem;

public interface MacnicaListItem extends ListItem {
    
   public enum ItemType{
       PAGE,ASSET;
   }
   public ItemType getItemType();

}
