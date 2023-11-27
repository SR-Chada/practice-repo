package com.macnicagwi.core.components.models;

import java.util.List;

import com.adobe.cq.wcm.core.components.models.Component;

public interface SupplierList extends Component{

    public List<MacnicaPageListItem> getListItems();
    public Integer getTotalResultsSize();
    public String getResourcePath();
    public String getButtonText();
    public String getSelectCompanyText();
}
