package com.macnicagwi.globalportal.core.models;

import java.util.List;

import com.adobe.cq.wcm.core.components.models.Component;

	
	public interface SearchResults extends Component {
	    
	    	    
	    public List<PageListItem> getResults();	    
	    public Integer getTotalResultsSize();	    
	    public String getQueryString();	    
	    public String getResourcePath();	    
	    public String getButtonLabel();
	    public String getNoResultsMessage();
	    public String getNoKeywordMessage();
	}



